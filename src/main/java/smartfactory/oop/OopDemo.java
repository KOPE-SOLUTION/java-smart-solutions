package smartfactory.oop;

import smartfactory.model.FactoryDevice;
import smartfactory.model.Machine;
import smartfactory.model.MachineStatus;
import smartfactory.model.Maintainable;
import smartfactory.service.SmartFactoryService;

/** Final Demo ของ Playlist 2: แสดง OOP Core และกฎธุรกิจโดยไม่ผูกกับ UI */
public class OopDemo {
    private static final String DIVIDER = "-".repeat(72);

    public static void main(String[] args) {
        SmartFactoryService service = SmartFactoryService.createWithSampleData();

        printHeader();
        showInitialFactoryStatus(service);
        showMaintenancePlan(service);
        simulateHighTemperature(service);
        completeMaintenance(service);
        rejectDuplicateMachineId(service);
        printClosing();
    }

    private static void printHeader() {
        System.out.println("=".repeat(72));
        System.out.println("        SMART FACTORY OOP CORE — FINAL DEMO");
        System.out.println("=".repeat(72));
        System.out.println("สถานการณ์: ตรวจสอบโรงงานก่อนเริ่มกะและรับมือค่าผิดปกติ\n");
    }

    private static void showInitialFactoryStatus(SmartFactoryService service) {
        printScene("สถานะโรงงานก่อนเริ่มกะ");
        for (Machine machine : service.getMachines()) {
            printMachine(machine);
        }
        System.out.printf(
                "สรุป: ทั้งหมด %d | กำลังทำงาน %d | Sensor ผิดปกติ %d | ควรบำรุง %d%n",
                service.getMachines().size(),
                service.countByStatus(MachineStatus.RUNNING),
                service.countByStatus(MachineStatus.WARNING)
                        + service.countByStatus(MachineStatus.EMERGENCY_STOP),
                service.countRequiringMaintenance()
        );
    }

    private static void showMaintenancePlan(SmartFactoryService service) {
        printScene("ตรวจสอบเครื่องจักรที่ควรวางแผนบำรุงรักษา");
        for (Machine machine : service.getMachines()) {
            if (requiresMaintenance(machine)) {
                printMaintenanceCandidate(machine, maintenanceReason(machine));
            }
        }
    }

    private static void simulateHighTemperature(SmartFactoryService service) {
        printScene("จำลองเหตุการณ์: M-001 มีอุณหภูมิสูง 105.0 °C");
        Machine mixer = service.findRequired("M-001");
        System.out.printf("ก่อนรับค่า: %s%n", formatState(mixer));

        service.updateSensor("M-001", 105.0, 8.2);
        System.out.printf("หลังรับค่า: %s%n", formatState(mixer));
    }

    private static void completeMaintenance(SmartFactoryService service) {
        printScene("บันทึกการบำรุงรักษา M-001");
        Machine mixer = service.findRequired("M-001");
        service.performMaintenance("M-001");
        System.out.printf("ผลลัพธ์: %s%n", formatState(mixer));
    }

    private static void rejectDuplicateMachineId(SmartFactoryService service) {
        printScene("ทดลองเพิ่มเครื่องจักรรหัส m-001 ซ้ำ");
        try {
            service.addMachine(new Machine("m-001", "เครื่องรหัสซ้ำ", "Line B"));
        } catch (IllegalArgumentException exception) {
            System.out.println("ระบบปฏิเสธข้อมูล: " + exception.getMessage());
        }
    }

    private static void printMachine(Machine machine) {
        System.out.printf(
                "%s | %s | %s | Sensor %.1f °C, %.1f mm/s | %s | %d ชั่วโมง | %s%n",
                machine.getId(),
                machine.getName(),
                machine.getLocation(),
                machine.getLatestReading().getTemperature(),
                machine.getLatestReading().getVibration(),
                machine.getStatus().getDisplayName(),
                machine.getOperatingHours(),
                machine.requiresMaintenance() ? "ควรบำรุง" : "ปกติ"
        );
    }

    private static boolean requiresMaintenance(Maintainable maintainable) {
        return maintainable.requiresMaintenance();
    }

    private static void printMaintenanceCandidate(FactoryDevice device, String reason) {
        System.out.printf(
                "%s | %s | เหตุผล: %s%n",
                device.getId(),
                device.getName(),
                reason
        );
    }

    private static String maintenanceReason(Machine machine) {
        boolean sensorAbnormal = machine.getStatus() == MachineStatus.WARNING
                || machine.getStatus() == MachineStatus.EMERGENCY_STOP;
        boolean hoursDue = machine.getOperatingHours() >= Machine.MAINTENANCE_HOURS;

        if (sensorAbnormal && hoursDue) {
            return "Sensor ผิดปกติและชั่วโมงทำงานครบกำหนด";
        }
        if (sensorAbnormal) {
            return "Sensor ผิดปกติ";
        }
        return "ทำงาน %d ชั่วโมง ครบกำหนด %d ชั่วโมง".formatted(
                machine.getOperatingHours(),
                Machine.MAINTENANCE_HOURS
        );
    }

    private static String formatState(Machine machine) {
        return "%s | %d ชั่วโมง | %s".formatted(
                machine.getStatus().getDisplayName(),
                machine.getOperatingHours(),
                machine.requiresMaintenance() ? "ควรบำรุง" : "ปกติ"
        );
    }

    private static void printScene(String title) {
        System.out.println(DIVIDER);
        System.out.println(title);
        System.out.println(DIVIDER);
    }

    private static void printClosing() {
        System.out.println(DIVIDER);
        System.out.println("สรุป: OOP Core ดูแลข้อมูล สถานะ การค้นหา และเงื่อนไขของระบบ");
        System.out.println("OOP Core พร้อมนำไปใช้ต่อกับ Console, JavaFX, REST API และ IoT");
        System.out.println("=".repeat(72));
    }
}
