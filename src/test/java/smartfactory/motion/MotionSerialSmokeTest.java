package smartfactory.motion;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/** Hardware smoke test; run explicitly because CI does not have a serial device. */
public final class MotionSerialSmokeTest {
    private MotionSerialSmokeTest() {
    }

    public static void main(String[] args) throws InterruptedException {
        String requestedPort = args.length == 0 ? "COM14" : args[0];
        List<MotionSerialClient.PortInfo> ports = MotionSerialClient.listPorts();
        MotionSerialClient.PortInfo port = ports.stream()
                .filter(candidate -> candidate.systemName().equalsIgnoreCase(requestedPort))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "ไม่พบ " + requestedPort + "; ports=" + ports
                ));

        CountDownLatch dataFrames = new CountDownLatch(5);
        AtomicReference<String> failure = new AtomicReference<>();
        AtomicReference<MotionSample> latest = new AtomicReference<>();

        try (MotionSerialClient client = new MotionSerialClient()) {
            client.connect(port, new MotionSerialClient.Listener() {
                @Override
                public void onLine(String line) {
                    MotionSample.parse(line).ifPresent(sample -> {
                        latest.set(sample);
                        dataFrames.countDown();
                    });
                }

                @Override
                public void onDisconnected() {
                    failure.compareAndSet(null, "พอร์ตถูกตัดระหว่างทดสอบ");
                }

                @Override
                public void onError(String message) {
                    failure.compareAndSet(null, message);
                }
            });
            client.sendCommand("hello");
            client.sendCommand("start");

            boolean complete = dataFrames.await(5, TimeUnit.SECONDS);
            if (!complete) {
                throw new AssertionError("ไม่ได้รับ DATA ครบ 5 frame ภายใน 5 วินาที");
            }
            if (failure.get() != null) {
                throw new AssertionError("Serial error: " + failure.get());
            }
        }

        MotionSample sample = latest.get();
        if (sample == null) {
            throw new AssertionError("ไม่มี MotionSample ที่ parse สำเร็จ");
        }
        System.out.printf(
                "PASS: 5 DATA frames from %s; heading=%d deg, accel=(%d,%d,%d) mg, errors=%d%n",
                requestedPort,
                sample.headingDegrees(),
                sample.accelerationXMillig(),
                sample.accelerationYMillig(),
                sample.accelerationZMillig(),
                sample.errors()
        );
    }
}
