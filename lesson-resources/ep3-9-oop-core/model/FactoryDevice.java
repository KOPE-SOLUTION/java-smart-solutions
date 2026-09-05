package smartfactory.model;

/**
 * Abstract class เก็บข้อมูลร่วมของอุปกรณ์ทุกชนิดในโรงงาน
 */
public abstract class FactoryDevice {
    private final String id;
    private String name;
    private String location;

    protected FactoryDevice(String id, String name, String location) {
        this.id = requireText(id, "id");
        this.name = requireText(name, "name");
        this.location = requireText(location, "location");
    }

    public abstract String getDeviceType();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = requireText(name, "name");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = requireText(location, "location");
    }

    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value.trim();
    }
}

