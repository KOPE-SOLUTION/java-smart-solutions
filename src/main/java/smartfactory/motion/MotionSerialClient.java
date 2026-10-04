package smartfactory.motion;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Owns the native serial port and converts incoming bytes into UTF-8 lines. */
public final class MotionSerialClient implements AutoCloseable {
    public interface Listener {
        void onLine(String line);

        void onDisconnected();

        void onError(String message);
    }

    public record PortInfo(String systemName, String description) {
        @Override
        public String toString() {
            return systemName + "  ·  " + description;
        }
    }

    private final Object lock = new Object();
    private final StringBuilder receivedText = new StringBuilder();
    private SerialPort port;
    private Listener listener;
    private boolean closing;

    public static List<PortInfo> listPorts() {
        return Arrays.stream(SerialPort.getCommPorts())
                .map(port -> new PortInfo(
                        port.getSystemPortName(),
                        usefulDescription(port)
                ))
                .toList();
    }

    private static String usefulDescription(SerialPort port) {
        String description = port.getDescriptivePortName();
        if (description == null || description.isBlank()) {
            description = port.getPortDescription();
        }
        return description == null || description.isBlank() ? "Serial port" : description;
    }

    public void connect(PortInfo selectedPort, Listener eventListener) {
        Objects.requireNonNull(selectedPort, "selectedPort");
        Objects.requireNonNull(eventListener, "eventListener");
        close();

        SerialPort candidate = SerialPort.getCommPort(selectedPort.systemName());
        candidate.setComPortParameters(115_200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        candidate.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        candidate.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 1_000);
        if (!candidate.openPort(1_500)) {
            throw new IllegalStateException("เปิด " + selectedPort.systemName() + " ไม่สำเร็จ");
        }

        synchronized (lock) {
            port = candidate;
            listener = eventListener;
            closing = false;
            receivedText.setLength(0);
        }

        candidate.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE
                        | SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                int eventType = event.getEventType();
                if ((eventType & SerialPort.LISTENING_EVENT_PORT_DISCONNECTED) != 0) {
                    notifyDisconnected();
                    return;
                }
                if ((eventType & SerialPort.LISTENING_EVENT_DATA_AVAILABLE) != 0) {
                    readAvailable(candidate);
                }
            }
        });
    }

    private void readAvailable(SerialPort currentPort) {
        try {
            int available = currentPort.bytesAvailable();
            while (available > 0) {
                byte[] bytes = new byte[Math.min(available, 512)];
                int count = currentPort.readBytes(bytes, bytes.length);
                if (count <= 0) {
                    return;
                }
                acceptText(new String(bytes, 0, count, StandardCharsets.US_ASCII));
                available = currentPort.bytesAvailable();
            }
        } catch (RuntimeException exception) {
            Listener currentListener = listener;
            if (!closing && currentListener != null) {
                currentListener.onError(exception.getMessage());
            }
        }
    }

    private void acceptText(String text) {
        synchronized (lock) {
            for (int index = 0; index < text.length(); index++) {
                char character = text.charAt(index);
                if (character == '\n') {
                    String line = receivedText.toString().strip();
                    receivedText.setLength(0);
                    if (!line.isEmpty() && listener != null) {
                        listener.onLine(line);
                    }
                } else if (character != '\r') {
                    if (receivedText.length() < 512) {
                        receivedText.append(character);
                    } else {
                        receivedText.setLength(0);
                    }
                }
            }
        }
    }

    public void sendCommand(String command) {
        byte[] bytes = (command.strip() + "\n").getBytes(StandardCharsets.US_ASCII);
        synchronized (lock) {
            if (port == null || !port.isOpen()) {
                throw new IllegalStateException("ยังไม่ได้เชื่อมต่อ Serial port");
            }
            int written = port.writeBytes(bytes, bytes.length);
            if (written != bytes.length) {
                throw new IllegalStateException("ส่งคำสั่งได้ไม่ครบ");
            }
        }
    }

    public boolean isConnected() {
        synchronized (lock) {
            return port != null && port.isOpen();
        }
    }

    private void notifyDisconnected() {
        Listener currentListener;
        synchronized (lock) {
            if (closing) {
                return;
            }
            currentListener = listener;
        }
        if (currentListener != null) {
            currentListener.onDisconnected();
        }
    }

    @Override
    public void close() {
        SerialPort currentPort;
        synchronized (lock) {
            closing = true;
            currentPort = port;
            port = null;
            listener = null;
            receivedText.setLength(0);
        }

        // Native listener shutdown may wait for an in-flight callback. Do not
        // hold lock here or that callback could wait on us forever.
        if (currentPort != null) {
            currentPort.removeDataListener();
            if (currentPort.isOpen()) {
                currentPort.closePort();
            }
        }
    }
}
