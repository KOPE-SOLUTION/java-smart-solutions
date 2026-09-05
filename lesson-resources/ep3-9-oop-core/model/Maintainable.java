package smartfactory.model;

/**
 * Interface บอกความสามารถของอุปกรณ์ที่บำรุงรักษาได้
 */
public interface Maintainable {
    boolean requiresMaintenance();

    void performMaintenance();
}

