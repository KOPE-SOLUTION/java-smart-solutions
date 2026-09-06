package smartfactory.service;

import smartfactory.model.Machine;
import smartfactory.model.MachineStatus;
import smartfactory.model.SensorReading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/** Service แยก business logic ออกจาก Console และ JavaFX UI */
public class SmartFactoryService {
    private final List<Machine> machines = new ArrayList<>();

    public void addMachine(Machine machine) {
        if (machine == null) {
            throw new IllegalArgumentException("machine must not be null");
        }
        if (findById(machine.getId()).isPresent()) {
            throw new IllegalArgumentException("รหัสเครื่องจักรซ้ำ: " + machine.getId());
        }
        machines.add(machine);
    }

    public void removeMachine(String id) {
        Machine machine = findRequired(id);
        machines.remove(machine);
    }

    public Optional<Machine> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return machines.stream()
                .filter(machine -> machine.getId().equalsIgnoreCase(id.trim()))
                .findFirst();
    }

    public Machine findRequired(String id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบเครื่องจักร: " + id));
    }

    public void updateSensor(String id, double temperature, double vibration) {
        findRequired(id).updateReading(new SensorReading(temperature, vibration));
    }

    public void performMaintenance(String id) {
        findRequired(id).performMaintenance();
    }

    public List<Machine> getMachines() {
        return Collections.unmodifiableList(machines);
    }

    public long countByStatus(MachineStatus status) {
        return machines.stream()
                .filter(machine -> machine.getStatus() == status)
                .count();
    }

    public long countRequiringMaintenance() {
        return machines.stream()
                .filter(Machine::requiresMaintenance)
                .count();
    }

    public void simulateSensorReadings(Random random) {
        for (Machine machine : machines) {
            double temperature = 50.0 + random.nextDouble() * 40.0;
            double vibration = 1.0 + random.nextDouble() * 8.0;
            machine.updateReading(new SensorReading(temperature, vibration));
        }
    }

    public static SmartFactoryService createWithSampleData() {
        SmartFactoryService service = new SmartFactoryService();

        Machine mixer = new Machine("M-001", "เครื่องผสม", "Line A", MachineStatus.RUNNING, 120);
        mixer.updateReading(new SensorReading(65.5, 3.1));

        Machine conveyor = new Machine("M-002", "สายพาน", "Line A", MachineStatus.RUNNING, 480);
        conveyor.updateReading(new SensorReading(82.3, 6.2));

        Machine pump = new Machine("M-003", "ปั๊มน้ำ", "Utility Room", MachineStatus.RUNNING, 520);
        pump.updateReading(new SensorReading(58.0, 2.4));

        service.addMachine(mixer);
        service.addMachine(conveyor);
        service.addMachine(pump);
        return service;
    }
}
