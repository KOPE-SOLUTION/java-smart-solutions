package smartfactory.model;

public enum MachineStatus {
    OFFLINE("ปิดเครื่อง"),
    RUNNING("กำลังทำงาน"),
    WARNING("Sensor ผิดปกติ"),
    EMERGENCY_STOP("หยุดฉุกเฉิน"),
    MAINTENANCE("กำลังบำรุงรักษา");

    private final String displayName;

    MachineStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
