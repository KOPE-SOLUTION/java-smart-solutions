package smartfactory;

import smartfactory.model.Machine;
import smartfactory.model.MachineStatus;
import smartfactory.model.SensorReading;
import smartfactory.service.SmartFactoryService;

/** ทดสอบด้วย Java ล้วนโดยไม่ต้องติดตั้ง JUnit หรือ Maven */
public class SmartFactoryTest {
    public static void main(String[] args) {
        testSafeReadingSetsRunning();
        testHighTemperatureSetsWarning();
        testEmergencyTemperatureStopsMachine();
        testMaintenanceResetsMachine();
        testStatusAndMaintenanceAreCountedSeparately();
        testDuplicateIdIsRejected();
        System.out.println("PASS: 6 tests");
    }

    private static void testSafeReadingSetsRunning() {
        Machine machine = new Machine("T-001", "Test Machine", "Test Area");
        machine.updateReading(new SensorReading(60, 2));
        assertEquals(MachineStatus.RUNNING, machine.getStatus(), "safe reading");
    }

    private static void testHighTemperatureSetsWarning() {
        Machine machine = new Machine("T-002", "Hot Machine", "Test Area");
        machine.updateReading(new SensorReading(85, 2));
        assertEquals(MachineStatus.WARNING, machine.getStatus(), "high temperature");
        assertTrue(machine.requiresMaintenance(), "warning requires maintenance");
    }

    private static void testEmergencyTemperatureStopsMachine() {
        Machine machine = new Machine("T-005", "Emergency Machine", "Test Area");
        machine.updateReading(new SensorReading(100.0, 2));
        assertEquals(MachineStatus.EMERGENCY_STOP, machine.getStatus(), "emergency temperature");
        assertTrue(machine.requiresMaintenance(), "emergency stop requires maintenance");
    }

    private static void testMaintenanceResetsMachine() {
        Machine machine = new Machine("T-003", "Old Machine", "Test Area", MachineStatus.RUNNING, 600);
        machine.performMaintenance();
        assertEquals(0, machine.getOperatingHours(), "reset hours");
        assertEquals(MachineStatus.OFFLINE, machine.getStatus(), "maintenance status");
    }

    private static void testStatusAndMaintenanceAreCountedSeparately() {
        SmartFactoryService service = SmartFactoryService.createWithSampleData();
        Machine pump = service.findRequired("M-003");

        assertEquals(1L, service.countByStatus(MachineStatus.WARNING), "abnormal sensor count");
        assertEquals(2L, service.countRequiringMaintenance(), "initial maintenance count");
        assertEquals(MachineStatus.RUNNING, pump.getStatus(), "overdue machine can still be running");
        assertTrue(pump.requiresMaintenance(), "operating hours require maintenance");

        service.performMaintenance("M-002");
        assertEquals(1L, service.countRequiringMaintenance(), "one machine still requires maintenance");

        service.updateSensor("M-002", 65, 3);
        assertEquals(1L, service.countRequiringMaintenance(), "normal reading keeps the other machine in count");

        service.performMaintenance("M-003");
        service.updateSensor("M-003", 60, 2);
        assertEquals(0L, service.countRequiringMaintenance(), "all maintenance completed");
    }

    private static void testDuplicateIdIsRejected() {
        SmartFactoryService service = new SmartFactoryService();
        service.addMachine(new Machine("T-004", "First", "Test Area"));
        try {
            service.addMachine(new Machine("t-004", "Second", "Test Area"));
            throw new AssertionError("duplicate id should fail");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("ซ้ำ"), "duplicate error message");
        }
    }

    private static void assertTrue(boolean actual, String message) {
        if (!actual) {
            throw new AssertionError(message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " expected=" + expected + " actual=" + actual);
        }
    }
}
