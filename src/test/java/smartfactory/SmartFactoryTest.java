package smartfactory;

import smartfactory.model.Machine;
import smartfactory.model.MachineStatus;
import smartfactory.model.SensorReading;
import smartfactory.service.SmartFactoryService;
import smartfactory.ui.ThaiUiSupport;

import javax.swing.UIManager;

/** ทดสอบด้วย Java ล้วนโดยไม่ต้องติดตั้ง JUnit หรือ Maven */
public class SmartFactoryTest {
    public static void main(String[] args) {
        testSafeReadingSetsRunning();
        testHighTemperatureSetsWarning();
        testMaintenanceResetsMachine();
        testDuplicateIdIsRejected();
        testThaiUiConfiguration();
        System.out.println("PASS: 5 tests");
    }

    private static void testSafeReadingSetsRunning() {
        Machine machine = new Machine("T-001", "Test Machine", "Lab");
        machine.updateReading(new SensorReading(60, 2));
        assertEquals(MachineStatus.RUNNING, machine.getStatus(), "safe reading");
    }

    private static void testHighTemperatureSetsWarning() {
        Machine machine = new Machine("T-002", "Hot Machine", "Lab");
        machine.updateReading(new SensorReading(85, 2));
        assertEquals(MachineStatus.WARNING, machine.getStatus(), "high temperature");
        assertTrue(machine.requiresMaintenance(), "warning requires maintenance");
    }

    private static void testMaintenanceResetsMachine() {
        Machine machine = new Machine("T-003", "Old Machine", "Lab", MachineStatus.RUNNING, 600);
        machine.performMaintenance();
        assertEquals(0, machine.getOperatingHours(), "reset hours");
        assertEquals(MachineStatus.OFFLINE, machine.getStatus(), "maintenance status");
    }

    private static void testDuplicateIdIsRejected() {
        SmartFactoryService service = new SmartFactoryService();
        service.addMachine(new Machine("T-004", "First", "Lab"));
        try {
            service.addMachine(new Machine("t-004", "Second", "Lab"));
            throw new AssertionError("duplicate id should fail");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("ซ้ำ"), "duplicate error message");
        }
    }

    private static void testThaiUiConfiguration() {
        ThaiUiSupport.configure();
        assertTrue(ThaiUiSupport.defaultFontsCanDisplayThai(), "default Swing fonts display Thai");
        assertEquals("ตกลง", UIManager.getString("OptionPane.okButtonText"), "Thai OK button");
        assertEquals("ยกเลิก", UIManager.getString("OptionPane.cancelButtonText"), "Thai cancel button");
        assertEquals("ใช่", UIManager.getString("OptionPane.yesButtonText"), "Thai yes button");
        assertEquals("ไม่", UIManager.getString("OptionPane.noButtonText"), "Thai no button");
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
