package smartfactory.model;

/**
 * Encapsulation: สถานะภายในเปลี่ยนผ่าน method เท่านั้น
 * Inheritance: Machine สืบทอด FactoryDevice
 * Interface: Machine ทำตามสัญญา Maintainable
 */
public class Machine extends FactoryDevice implements Maintainable {
    public static final double MAX_TEMPERATURE = 80.0;
    public static final double EMERGENCY_TEMPERATURE = 100.0;
    public static final double MAX_VIBRATION = 7.0;
    public static final int MAINTENANCE_HOURS = 500;

    private MachineStatus status;
    private SensorReading latestReading;
    private int operatingHours;

    public Machine(String id, String name, String location) {
        this(id, name, location, MachineStatus.OFFLINE, 0);
    }

    public Machine(
            String id,
            String name,
            String location,
            MachineStatus status,
            int operatingHours
    ) {
        super(id, name, location);
        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }
        if (operatingHours < 0) {
            throw new IllegalArgumentException("operatingHours must not be negative");
        }
        this.status = status;
        this.operatingHours = operatingHours;
        this.latestReading = new SensorReading(0, 0);
    }

    @Override
    public String getDeviceType() {
        return "Machine";
    }

    public void updateReading(SensorReading reading) {
        if (reading == null) {
            throw new IllegalArgumentException("reading must not be null");
        }

        this.latestReading = reading;
        this.operatingHours++;

        if (reading.getTemperature() >= EMERGENCY_TEMPERATURE) {
            this.status = MachineStatus.EMERGENCY_STOP;
        } else if (reading.getTemperature() >= MAX_TEMPERATURE
                || reading.getVibration() >= MAX_VIBRATION) {
            this.status = MachineStatus.WARNING;
        } else {
            this.status = MachineStatus.RUNNING;
        }
    }

    @Override
    public boolean requiresMaintenance() {
        return operatingHours >= MAINTENANCE_HOURS
                || status == MachineStatus.WARNING
                || status == MachineStatus.EMERGENCY_STOP;
    }

    @Override
    public void performMaintenance() {
        this.operatingHours = 0;
        this.status = MachineStatus.OFFLINE;
    }

    public MachineStatus getStatus() {
        return status;
    }

    public SensorReading getLatestReading() {
        return latestReading;
    }

    public int getOperatingHours() {
        return operatingHours;
    }
}
