package smartfactory.model;

import java.time.LocalDateTime;

/** Value Object แทนค่าที่อ่านได้จากเซนเซอร์หนึ่งครั้ง */
public class SensorReading {
    private final double temperature;
    private final double vibration;
    private final LocalDateTime recordedAt;

    public SensorReading(double temperature, double vibration) {
        this(temperature, vibration, LocalDateTime.now());
    }

    public SensorReading(double temperature, double vibration, LocalDateTime recordedAt) {
        if (!Double.isFinite(temperature) || !Double.isFinite(vibration)) {
            throw new IllegalArgumentException("Sensor values must be finite numbers");
        }
        if (vibration < 0) {
            throw new IllegalArgumentException("Vibration must not be negative");
        }
        if (recordedAt == null) {
            throw new IllegalArgumentException("recordedAt must not be null");
        }
        this.temperature = temperature;
        this.vibration = vibration;
        this.recordedAt = recordedAt;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getVibration() {
        return vibration;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
}

