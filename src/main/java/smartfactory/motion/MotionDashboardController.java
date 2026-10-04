package smartfactory.motion;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;
import java.util.Locale;

/** Coordinates the JavaFX dashboard while keeping serial IO outside the UI thread. */
public final class MotionDashboardController {
    private static final int CHART_WINDOW = 60;

    @FXML private ImageView brandLogo;
    @FXML private ComboBox<MotionSerialClient.PortInfo> portCombo;
    @FXML private Button connectButton;
    @FXML private Button streamButton;
    @FXML private Label connectionBadge;
    @FXML private Label sourceLabel;
    @FXML private Label headingValue;
    @FXML private Label cardinalValue;
    @FXML private Label pitchValue;
    @FXML private Label rollValue;
    @FXML private Label angularRateValue;
    @FXML private Label magneticValue;
    @FXML private Label sampleRateValue;
    @FXML private Label receivedValue;
    @FXML private Label deviceErrorsValue;
    @FXML private Label rawAccelValue;
    @FXML private Label rawGyroValue;
    @FXML private Label rawMagValue;
    @FXML private Label statusMessage;
    @FXML private CompassView compassView;
    @FXML private LineChart<Number, Number> accelerationChart;
    @FXML private LineChart<Number, Number> gyroscopeChart;

    private final MotionSerialClient serialClient = new MotionSerialClient();
    private final XYChart.Series<Number, Number> accelX = series("X");
    private final XYChart.Series<Number, Number> accelY = series("Y");
    private final XYChart.Series<Number, Number> accelZ = series("Z");
    private final XYChart.Series<Number, Number> gyroX = series("X");
    private final XYChart.Series<Number, Number> gyroY = series("Y");
    private final XYChart.Series<Number, Number> gyroZ = series("Z");
    private Timeline demoTimeline;
    private long chartIndex;
    private long receivedSamples;
    private long previousSampleNanos;
    private double smoothedRate;
    private double demoHeading;
    private boolean demoMode;
    private boolean streamPaused;

    @FXML
    private void initialize() {
        loadBrandLogo();
        accelerationChart.getData().setAll(accelX, accelY, accelZ);
        gyroscopeChart.getData().setAll(gyroX, gyroY, gyroZ);
        accelerationChart.setCreateSymbols(false);
        gyroscopeChart.setCreateSymbols(false);
        refreshPorts();
        startDemo();
    }

    private void loadBrandLogo() {
        var logoUrl = MotionDashboardController.class.getResource(
                "/smartfactory/motion/kope-solution-logo-2027-light.png"
        );
        if (logoUrl == null) {
            brandLogo.setManaged(false);
            brandLogo.setVisible(false);
            return;
        }
        brandLogo.setImage(new Image(logoUrl.toExternalForm(), true));
    }

    private static XYChart.Series<Number, Number> series(String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        return series;
    }

    @FXML
    private void handleRefreshPorts() {
        refreshPorts();
        showStatus("สแกน Serial port ใหม่แล้ว");
    }

    private void refreshPorts() {
        String previous = portCombo.getValue() == null ? null : portCombo.getValue().systemName();
        List<MotionSerialClient.PortInfo> ports = MotionSerialClient.listPorts();
        portCombo.getItems().setAll(ports);
        ports.stream()
                .filter(port -> port.systemName().equalsIgnoreCase(previous == null ? "" : previous))
                .findFirst()
                .or(() -> ports.stream().filter(port ->
                        port.description().toLowerCase(Locale.ROOT).contains("stlink")
                                || port.description().toLowerCase(Locale.ROOT).contains("stmicro"))
                        .findFirst())
                .or(() -> ports.stream()
                        .filter(port -> !port.systemName().equalsIgnoreCase("COM1"))
                        .findFirst())
                .or(() -> ports.stream().findFirst())
                .ifPresent(portCombo::setValue);
    }

    @FXML
    private void handleConnect() {
        if (serialClient.isConnected()) {
            disconnect();
            return;
        }
        MotionSerialClient.PortInfo selectedPort = portCombo.getValue();
        if (selectedPort == null) {
            showStatus("ไม่พบพอร์ต กรุณาเสียบบอร์ดแล้วกดสแกนใหม่");
            return;
        }

        stopDemo();
        try {
            serialClient.connect(selectedPort, new MotionSerialClient.Listener() {
                @Override
                public void onLine(String line) {
                    Platform.runLater(() -> acceptSerialLine(line));
                }

                @Override
                public void onDisconnected() {
                    Platform.runLater(() -> {
                        setDisconnected("สาย USB หรือพอร์ตถูกถอด");
                        startDemo();
                    });
                }

                @Override
                public void onError(String message) {
                    Platform.runLater(() -> showStatus("Serial error: " + message));
                }
            });
            connectButton.setText("ตัดการเชื่อมต่อ");
            connectionBadge.setText("● LIVE USB");
            connectionBadge.getStyleClass().setAll("connection-badge", "badge-live");
            sourceLabel.setText(selectedPort.systemName() + " · 115200 8-N-1");
            streamButton.setDisable(false);
            streamPaused = false;
            streamButton.setText("หยุด Stream");
            resetSessionCounters();
            serialClient.sendCommand("hello");
            serialClient.sendCommand("start");
            showStatus("เชื่อมต่อแล้ว กำลังรอ DATA frame จาก STM32");
        } catch (RuntimeException exception) {
            serialClient.close();
            setDisconnected(exception.getMessage());
            startDemo();
        }
    }

    @FXML
    private void handleDemoMode() {
        disconnect();
        startDemo();
    }

    @FXML
    private void handleToggleStream() {
        if (!serialClient.isConnected()) {
            showStatus("คำสั่งนี้ใช้ได้เมื่อเชื่อมต่อบอร์ด");
            return;
        }
        streamPaused = !streamPaused;
        serialClient.sendCommand(streamPaused ? "stop" : "start");
        streamButton.setText(streamPaused ? "เริ่ม Stream" : "หยุด Stream");
    }

    @FXML
    private void handleZeroGyro() {
        sendBoardCommand("zero", "วางบอร์ดนิ่งประมาณ 20 วินาทีเพื่อหาค่า Gyro bias");
    }

    @FXML
    private void handleSetNorth() {
        sendBoardCommand("north", "ตั้งทิศปัจจุบันเป็น 0° สำหรับการสาธิตแล้ว");
    }

    private void sendBoardCommand(String command, String successMessage) {
        if (!serialClient.isConnected()) {
            showStatus("กรุณาเชื่อมต่อ STM32 ก่อนส่งคำสั่ง");
            return;
        }
        try {
            serialClient.sendCommand(command);
            showStatus(successMessage);
        } catch (RuntimeException exception) {
            showStatus(exception.getMessage());
        }
    }

    private void acceptSerialLine(String line) {
        MotionSample.parse(line).ifPresentOrElse(
                sample -> acceptSample(sample, false),
                () -> {
                    if (line.startsWith("HELLO,")) {
                        showStatus("Handshake สำเร็จ: " + line);
                    } else if (line.startsWith("ACK,")) {
                        showStatus(line.replace(',', ' '));
                    } else if (line.startsWith("ERR,")) {
                        showStatus("STM32: " + line);
                    }
                }
        );
    }

    private void startDemo() {
        if (demoTimeline != null) {
            return;
        }
        demoMode = true;
        connectionBadge.setText("● DEMO DATA");
        connectionBadge.getStyleClass().setAll("connection-badge", "badge-demo");
        sourceLabel.setText("ข้อมูลจำลอง · เลือก Serial port เพื่อรับค่าจริงจากบอร์ด");
        streamButton.setDisable(true);
        resetSessionCounters();
        demoTimeline = new Timeline(new KeyFrame(Duration.millis(100), event -> emitDemoSample()));
        demoTimeline.setCycleCount(Timeline.INDEFINITE);
        demoTimeline.play();
        showStatus("Demo ทำงานอยู่ — UI ใช้ได้แม้ยังไม่ Flash firmware");
    }

    private void emitDemoSample() {
        demoHeading = (demoHeading + 1.6) % 360.0;
        double radians = Math.toRadians(demoHeading);
        long tick = Math.round(chartIndex * 10.0);
        MotionSample sample = new MotionSample(
                tick,
                (int) Math.round(demoHeading) % 360,
                ((int) Math.round((demoHeading + 22.5) / 45.0)) % 8,
                (int) Math.round(Math.sin(radians * 0.7) * 120),
                (int) Math.round(Math.cos(radians * 0.5) * 90),
                990 + (int) Math.round(Math.sin(radians) * 25),
                (int) Math.round(Math.sin(radians * 1.3) * 4_500),
                (int) Math.round(Math.cos(radians * 0.9) * 3_200),
                16_000,
                (int) Math.round(Math.sin(radians) * 340 - 167),
                (int) Math.round(Math.cos(radians) * 330 + 37),
                -28,
                0
        );
        acceptSample(sample, true);
    }

    private void stopDemo() {
        demoMode = false;
        if (demoTimeline != null) {
            demoTimeline.stop();
            demoTimeline = null;
        }
    }

    private void acceptSample(MotionSample sample, boolean simulated) {
        long now = System.nanoTime();
        if (previousSampleNanos != 0) {
            double instantRate = 1_000_000_000.0 / (now - previousSampleNanos);
            smoothedRate = smoothedRate == 0 ? instantRate : smoothedRate * 0.8 + instantRate * 0.2;
        }
        previousSampleNanos = now;
        receivedSamples++;

        compassView.setHeading(sample.headingDegrees());
        headingValue.setText(sample.headingDegrees() + "°");
        cardinalValue.setText(cardinal(sample.headingDegrees()));
        pitchValue.setText(formatSigned(sample.pitchDegrees(), "°"));
        rollValue.setText(formatSigned(sample.rollDegrees(), "°"));
        angularRateValue.setText(String.format(Locale.US, "%.1f °/s", sample.angularRateDegreesPerSecond()));
        magneticValue.setText(String.format(Locale.US, "%.0f raw", sample.magneticMagnitude()));
        sampleRateValue.setText(String.format(Locale.US, "%.1f Hz", smoothedRate));
        receivedValue.setText(Long.toString(receivedSamples));
        deviceErrorsValue.setText(Long.toString(sample.errors()));
        rawAccelValue.setText("X " + sample.accelerationXMillig() + "  Y "
                + sample.accelerationYMillig() + "  Z " + sample.accelerationZMillig() + " mg");
        rawGyroValue.setText("X " + formatDps(sample.gyroXMillidegreesPerSecond()) + "  Y "
                + formatDps(sample.gyroYMillidegreesPerSecond()) + "  Z "
                + formatDps(sample.gyroZMillidegreesPerSecond()) + " °/s");
        rawMagValue.setText("X " + sample.magneticX() + "  Y " + sample.magneticY()
                + "  Z " + sample.magneticZ());

        addChartPoint(accelX, chartIndex, sample.accelerationXMillig());
        addChartPoint(accelY, chartIndex, sample.accelerationYMillig());
        addChartPoint(accelZ, chartIndex, sample.accelerationZMillig());
        addChartPoint(gyroX, chartIndex, sample.gyroXMillidegreesPerSecond() / 1_000.0);
        addChartPoint(gyroY, chartIndex, sample.gyroYMillidegreesPerSecond() / 1_000.0);
        addChartPoint(gyroZ, chartIndex, sample.gyroZMillidegreesPerSecond() / 1_000.0);
        chartIndex++;

        if (!simulated && demoMode) {
            stopDemo();
        }
    }

    private static String formatDps(int millidegreesPerSecond) {
        return String.format(Locale.US, "%+.1f", millidegreesPerSecond / 1_000.0);
    }

    private static String formatSigned(double value, String suffix) {
        return String.format(Locale.US, "%+.1f%s", value, suffix);
    }

    private static void addChartPoint(
            XYChart.Series<Number, Number> series,
            long index,
            double value
    ) {
        series.getData().add(new XYChart.Data<>(index, value));
        if (series.getData().size() > CHART_WINDOW) {
            series.getData().removeFirst();
        }
    }

    private static String cardinal(int degrees) {
        String[] directions = {"เหนือ · N", "ตะวันออกเฉียงเหนือ · NE", "ตะวันออก · E",
                "ตะวันออกเฉียงใต้ · SE", "ใต้ · S", "ตะวันตกเฉียงใต้ · SW",
                "ตะวันตก · W", "ตะวันตกเฉียงเหนือ · NW"};
        return directions[((degrees + 22) / 45) % 8];
    }

    private void resetSessionCounters() {
        chartIndex = 0;
        receivedSamples = 0;
        previousSampleNanos = 0;
        smoothedRate = 0;
        for (XYChart.Series<Number, Number> series : List.of(accelX, accelY, accelZ, gyroX, gyroY, gyroZ)) {
            series.getData().clear();
        }
    }

    private void disconnect() {
        stopDemo();
        serialClient.close();
        setDisconnected("ตัดการเชื่อมต่อแล้ว");
    }

    private void setDisconnected(String message) {
        connectButton.setText("เชื่อมต่อ USB");
        connectionBadge.setText("● OFFLINE");
        connectionBadge.getStyleClass().setAll("connection-badge", "badge-offline");
        sourceLabel.setText("ยังไม่ได้เชื่อมต่อบอร์ด");
        streamButton.setDisable(true);
        showStatus(message);
    }

    private void showStatus(String message) {
        statusMessage.setText(message == null || message.isBlank() ? "พร้อมใช้งาน" : message);
    }

    public void shutdown() {
        stopDemo();
        serialClient.close();
    }
}
