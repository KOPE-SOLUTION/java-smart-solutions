package smartfactory.oop;

import smartfactory.model.FactoryDevice;
import smartfactory.model.Machine;
import smartfactory.model.Maintainable;
import smartfactory.service.SmartFactoryService;

/** ตอนที่ 2: นำ Object, Encapsulation, Inheritance, Interface และ Polymorphism มาใช้ */
public class OopDemo {
    public static void main(String[] args) {
        SmartFactoryService service = SmartFactoryService.createWithSampleData();

        System.out.println("=== SMART FACTORY OOP DEMO ===");
        for (Machine machine : service.getMachines()) {
            printDevice(machine); // Machine ถูกมองเป็น FactoryDevice ได้
            System.out.printf(
                    "  สถานะ: %-18s อุณหภูมิ: %5.1f °C สั่น: %4.1f mm/s ชั่วโมง: %d%n",
                    machine.getStatus(),
                    machine.getLatestReading().getTemperature(),
                    machine.getLatestReading().getVibration(),
                    machine.getOperatingHours()
            );

            Maintainable maintainable = machine; // Polymorphism ผ่าน interface
            if (maintainable.requiresMaintenance()) {
                System.out.println("  -> ควรวางแผนบำรุงรักษา");
            }
        }

        System.out.printf(
                "%nทั้งหมด %d เครื่อง | ต้องตรวจ/บำรุงรักษา %d เครื่อง%n",
                service.getMachines().size(),
                service.countRequiringMaintenance()
        );
    }

    private static void printDevice(FactoryDevice device) {
        System.out.printf(
                "%s | %s | %s | %s%n",
                device.getId(),
                device.getName(),
                device.getDeviceType(),
                device.getLocation()
        );
    }
}

