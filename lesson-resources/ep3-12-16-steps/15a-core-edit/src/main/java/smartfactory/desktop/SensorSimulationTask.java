package smartfactory.desktop;

import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorSimulationTask extends Task<List<SensorUpdate>> {
    private final List<String> machineIds;

    public SensorSimulationTask(List<String> machineIds) {
        this.machineIds = List.copyOf(machineIds);
    }

    @Override
    protected List<SensorUpdate> call() {
        Random random = new Random();
        List<SensorUpdate> results = new ArrayList<>();
        for (String id : machineIds) {
            if (isCancelled()) {
                break;
            }
            double temperature = 50.0 + random.nextDouble() * 60.0;
            double vibration = 1.0 + random.nextDouble() * 8.0;
            results.add(new SensorUpdate(id, temperature, vibration));
        }
        return results;
    }
}
