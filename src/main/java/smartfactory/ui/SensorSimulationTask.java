package smartfactory.ui;

import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** สร้างข้อมูล Sensor จำลองนอก JavaFX Application Thread */
public final class SensorSimulationTask extends Task<List<SensorUpdate>> {
    private final List<String> machineIds;

    public SensorSimulationTask(List<String> machineIds) {
        this.machineIds = List.copyOf(machineIds);
    }

    @Override
    protected List<SensorUpdate> call() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return machineIds.stream()
                .map(id -> new SensorUpdate(
                        id,
                        50.0 + random.nextDouble(60.0),
                        1.0 + random.nextDouble(8.0)
                ))
                .toList();
    }
}
