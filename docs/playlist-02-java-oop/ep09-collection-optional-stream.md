# EP 2.9 — Collection, Optional และ Stream

## เป้าหมาย

- ใช้ `List` แทน Array ที่ขนาดคงที่
- สื่อผลการค้นหาที่อาจไม่พบด้วย `Optional`
- กรองและนับข้อมูลด้วย Stream

```java
private final List<Machine> machines = new ArrayList<>();

public void addMachine(Machine machine) {
    machines.add(machine);
}
```

ค้นหาด้วย Optional:

```java
public Optional<Machine> findById(String id) {
    return machines.stream()
            .filter(machine -> machine.getId().equalsIgnoreCase(id))
            .findFirst();
}
```

นับสถานะ:

```java
long warningCount = machines.stream()
        .filter(machine -> machine.getStatus() == MachineStatus.WARNING)
        .count();
```

หลีกเลี่ยงการเรียก `optional.get()` โดยไม่ตรวจ ควรใช้ `orElse`, `orElseThrow`, `ifPresent` หรือ method อื่นที่สื่อกรณีไม่พบ

## Challenge

สร้าง Stream หาเครื่องที่ทำงานเกิน 500 ชั่วโมง และแปลงผลเป็น List ของชื่อเครื่อง

ถัดไป: [EP 2.10 — Service, Exception และ Test](ep10-service-exception-test.md)

