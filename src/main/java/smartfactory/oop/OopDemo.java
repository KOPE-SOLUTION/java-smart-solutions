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
        showObjectsAndComposition(service);
        showPolymorphism(service);
        showServiceCapabilities(service);
        showBusinessRule(service);
        showExceptionHandling(service);
        printClosing();
    }

    private static void printHeader() {
        System.out.println("=".repeat(72));
        System.out.println("        SMART FACTORY OOP CORE — FINAL DEMO");
        System.out.println("=".repeat(72));
        System.out.println("แยกข้อมูล กฎธุรกิจ และการจัดการออกจาก Console หรือหน้าจอ\n");
    }

    private static void showObjectsAndComposition(SmartFactoryService service) {
        printSection("1", "OBJECT + ENCAPSULATION + COMPOSITION");
        for (Machine machine : service.getMachines()) {
            printMachine(machine);
        }
    }

    private static void showPolymorphism(SmartFactoryService service) {
        printSection("2", "INHERITANCE + INTERFACE + POLYMORPHISM");
        Machine machine = service.findRequired("M-002");
        printAsDevice(machine);
        printAsMaintainable(machine);
        System.out.println("Object เดียวกันถูกใช้งานผ่าน FactoryDevice และ Maintainable ได้");
    }

    private static void showServiceCapabilities(SmartFactoryService service) {
        printSection("3", "COLLECTION + OPTIONAL + STREAM + SERVICE");
        service.findById("m-003").ifPresent(machine ->
                System.out.printf("ค้นหา m-003: พบ %s ที่ %s%n", machine.getName(), machine.getLocation())
        );
        System.out.printf("กำลังทำงาน: %d เครื่อง%n", service.countByStatus(MachineStatus.RUNNING));
        System.out.printf(
                "ทั้งหมด: %d เครื่อง | ต้องบำรุงรักษา: %d เครื่อง%n",
                service.getMachines().size(),
                service.countRequiringMaintenance()
        );
    }

    private static void showBusinessRule(SmartFactoryService service) {
        printSection("4", "BUSINESS RULE อยู่ใน OBJECT");
        Machine mixer = service.findRequired("M-001");
        System.out.printf("ก่อนรับค่าผิดปกติ: %s%n", formatState(mixer));

        service.updateSensor("M-001", 105.0, 8.2);
        System.out.printf("หลังรับค่า 105.0 °C: %s%n", formatState(mixer));

        service.performMaintenance("M-001");
        System.out.printf("หลังบำรุงรักษา: %s%n", formatState(mixer));
    }

    private static void showExceptionHandling(SmartFactoryService service) {
        printSection("5", "VALIDATION + EXCEPTION");
        try {
            service.addMachine(new Machine("m-001", "เครื่องรหัสซ้ำ", "Line B"));
        } catch (IllegalArgumentException exception) {
            System.out.println("ป้องกันข้อมูลไม่ถูกต้อง: " + exception.getMessage());
        }
    }

    private static void printMachine(Machine machine) {
        System.out.printf(
                "%s | %s | %s%n",
                machine.getId(),
                machine.getName(),
                machine.getLocation()
        );
        System.out.printf(
                "  Sensor %.1f °C, %.1f mm/s | %s | %d ชั่วโมง | %s%n",
                machine.getLatestReading().getTemperature(),
                machine.getLatestReading().getVibration(),
                machine.getStatus().getDisplayName(),
                machine.getOperatingHours(),
                machine.requiresMaintenance() ? "ควรบำรุง" : "ปกติ"
        );
    }

    private static void printAsDevice(FactoryDevice device) {
        System.out.printf(
                "FactoryDevice: %s | %s | ชนิด %s%n",
                device.getId(),
                device.getName(),
                device.getDeviceType()
        );
    }

    private static void printAsMaintainable(Maintainable maintainable) {
        System.out.println(
                "Maintainable: "
                        + (maintainable.requiresMaintenance() ? "ควรวางแผนบำรุงรักษา" : "ยังไม่ถึงกำหนด")
        );
    }

    private static String formatState(Machine machine) {
        return "%s | %d ชั่วโมง | %s".formatted(
                machine.getStatus().getDisplayName(),
                machine.getOperatingHours(),
                machine.requiresMaintenance() ? "ควรบำรุง" : "ปกติ"
        );
    }

    private static void printSection(String number, String title) {
        System.out.println(DIVIDER);
        System.out.printf("[%s] %s%n", number, title);
        System.out.println(DIVIDER);
    }

    private static void printClosing() {
        System.out.println(DIVIDER);
        System.out.println("OOP Core พร้อมนำไปใช้ต่อกับ Console, JavaFX, REST API และ IoT");
        System.out.println("=".repeat(72));
    }
}
