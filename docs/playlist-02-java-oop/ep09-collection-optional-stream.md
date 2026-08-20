# EP 2.9 — Collection, Optional และ Stream

## เป้าหมาย

- เพิ่มข้อมูลด้วย `List`
- สื่อผลค้นหาที่อาจไม่พบด้วย `Optional`
- นับข้อมูลที่ตรงเงื่อนไขด้วย Stream

ทำตามทีละส่วน โดยเริ่มจาก List ให้ทำงานก่อนแล้วจึงเพิ่มการค้นหาและการนับ

## 1. เก็บ Machine ใน List

เพิ่ม Field ใน Service:

```java
private final List<Machine> machines = new ArrayList<>();
```

เพิ่ม Method:

```java
public void addMachine(Machine machine) {
    machines.add(machine);
}
```

`List` เพิ่มข้อมูลภายหลังได้ ต่างจาก Array ที่กำหนดขนาดคงที่

## 2. ค้นหาด้วย Optional

```java
public Optional<Machine> findById(String id) {
    return machines.stream()
            .filter(machine -> machine.getId().equalsIgnoreCase(id))
            .findFirst();
}
```

`Optional` บอกผู้เรียกอย่างชัดเจนว่าผลค้นหาอาจไม่มีค่า หลีกเลี่ยง `optional.get()` โดยไม่ตรวจ ควรใช้ `ifPresent`, `orElse` หรือ `orElseThrow`

## 3. นับด้วย Stream

```java
long warningCount = machines.stream()
        .filter(machine -> machine.getStatus() == MachineStatus.WARNING)
        .count();
```

อ่านลำดับเป็น “นำ Machine ทั้งหมดมา กรองเฉพาะ WARNING แล้วนับจำนวน”

## Challenge

เปลี่ยนเงื่อนไขใน `filter` ให้เลือกเครื่องที่ทำงานครบ 500 ชั่วโมง แล้วนับจำนวน

ถัดไป: [EP 2.10 — Service, Exception และ Test](ep10-service-exception-test.md)
