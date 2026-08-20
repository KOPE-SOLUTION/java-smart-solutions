package smartfactory.basic;

import java.util.Scanner;

/**
 * ตอนที่ 1: ตัวแปร, array, loop, method, if/else และ switch
 */
public class BasicDemo {
    private static final double WARNING_TEMPERATURE = 80.0;

    public static void main(String[] args) {
        String factoryName = "KOPES Smart Factory";
        String[] machineNames = {"Mixer", "Conveyor", "Water Pump"};
        double[] temperatures = {65.5, 82.3, 58.0};

        System.out.println("=== " + factoryName + " ===");
        for (int index = 0; index < machineNames.length; index++) {
            String status = checkTemperature(temperatures[index]);
            System.out.printf(
                    "%d. %-12s อุณหภูมิ %5.1f °C -> %s%n",
                    index + 1,
                    machineNames[index],
                    temperatures[index],
                    status
            );
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("\nเลือกเครื่องจักรที่ต้องการดู (1-3): ");
            int selectedNumber = scanner.nextInt();

            switch (selectedNumber) {
                case 1, 2, 3 -> {
                    int selectedIndex = selectedNumber - 1;
                    System.out.println("คุณเลือก: " + machineNames[selectedIndex]);
                }
                default -> System.out.println("ไม่พบเครื่องจักรหมายเลขนี้");
            }
        }
    }

    private static String checkTemperature(double temperature) {
        if (temperature >= WARNING_TEMPERATURE) {
            return "WARNING";
        }
        return "NORMAL";
    }
}

