package smartfactory.ui;

/** ข้อมูล Sensor ที่สร้างบน Background Thread ก่อนส่งกลับไปอัปเดต Model บน JavaFX Thread */
public record SensorUpdate(String machineId, double temperature, double vibration) {
}
