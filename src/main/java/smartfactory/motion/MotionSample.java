package smartfactory.motion;

import java.util.Optional;

/** One validated DATA frame emitted by lesson12-javafx-motion-stream. */
public record MotionSample(
        long tick,
        int headingDegrees,
        int sector,
        int accelerationXMillig,
        int accelerationYMillig,
        int accelerationZMillig,
        int gyroXMillidegreesPerSecond,
        int gyroYMillidegreesPerSecond,
        int gyroZMillidegreesPerSecond,
        int magneticX,
        int magneticY,
        int magneticZ,
        long errors
) {
    private static final int FIELD_COUNT = 14;

    public MotionSample {
        if (headingDegrees < 0 || headingDegrees >= 360) {
            throw new IllegalArgumentException("heading must be in [0, 359]");
        }
        if (sector < 0 || sector > 7) {
            throw new IllegalArgumentException("sector must be in [0, 7]");
        }
    }

    public static Optional<MotionSample> parse(String line) {
        if (line == null || !line.startsWith("DATA,")) {
            return Optional.empty();
        }
        String[] fields = line.strip().split(",", -1);
        if (fields.length != FIELD_COUNT) {
            return Optional.empty();
        }
        try {
            return Optional.of(new MotionSample(
                    Long.parseLong(fields[1]),
                    Integer.parseInt(fields[2]),
                    Integer.parseInt(fields[3]),
                    Integer.parseInt(fields[4]),
                    Integer.parseInt(fields[5]),
                    Integer.parseInt(fields[6]),
                    Integer.parseInt(fields[7]),
                    Integer.parseInt(fields[8]),
                    Integer.parseInt(fields[9]),
                    Integer.parseInt(fields[10]),
                    Integer.parseInt(fields[11]),
                    Integer.parseInt(fields[12]),
                    Long.parseLong(fields[13])
            ));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public double rollDegrees() {
        return Math.toDegrees(Math.atan2(accelerationYMillig, accelerationZMillig));
    }

    public double pitchDegrees() {
        double yz = Math.hypot(accelerationYMillig, accelerationZMillig);
        return Math.toDegrees(Math.atan2(-accelerationXMillig, yz));
    }

    public double angularRateDegreesPerSecond() {
        double x = gyroXMillidegreesPerSecond / 1_000.0;
        double y = gyroYMillidegreesPerSecond / 1_000.0;
        double z = gyroZMillidegreesPerSecond / 1_000.0;
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double magneticMagnitude() {
        return Math.sqrt((double) magneticX * magneticX
                + (double) magneticY * magneticY
                + (double) magneticZ * magneticZ);
    }
}
