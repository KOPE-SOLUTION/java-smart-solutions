# Case Study — STM32F3 Motion Dashboard ผ่าน USB Serial

Case Study นี้นำความรู้จาก Playlist 3 มาเชื่อมกับบอร์ด STM32F3DISCOVERY จริง โดยให้ Rust firmware
อ่าน Accelerometer, Gyroscope และ Magnetometer แล้วส่งข้อมูลผ่าน ST-LINK Virtual COM Port ไปยัง
JavaFX Dashboard

```text
LSM303AGR ── I²C ─┐
                  ├─ STM32F303 ─ USART1 ─ ST-LINK USB ─ COM port ─ JavaFX
I3G4250D  ── SPI ─┘
```

## สิ่งที่เห็นใน Dashboard

- เข็มทิศต่อเนื่อง `0–359°` ไม่ได้จำกัดเพียง LED 8 ทิศ
- Pitch และ Roll ที่คำนวณจาก Accelerometer
- อัตราการหมุนรวมและกราฟ Gyroscope 3 แกน
- กราฟ Accelerometer 3 แกนและค่า Magnetometer ดิบ
- Sample rate, จำนวน frame และจำนวน I²C/SPI error
- Demo Mode สำหรับเรียน UI โดยไม่ต้องเสียบบอร์ด
- คำสั่ง Start/Stop stream, ตั้งศูนย์ Gyro และตั้งทิศปัจจุบันเป็น 0°

## แนวทางออกแบบหน้าจอและแบรนด์

Dashboard ใช้โลโก้ **KOPE SOLUTION** เวอร์ชันพื้นดำสำหรับ Dark UI โดยเก็บสำเนาที่ปรับขนาดเหมาะกับ
Desktop Application ไว้ที่
`src/main/resources/smartfactory/motion/kope-solution-logo-2027-light.png`
และแสดงผ่าน JavaFX `ImageView`

Visual system ตั้งใจให้ใกล้กับเครื่องมือวิศวกรรมที่ใช้งานจริง:

- ใช้ graphite/navy เป็นพื้นเพื่อให้ค่าจากเซนเซอร์อ่านได้นานโดยไม่ล้าตา
- ใช้น้ำเงิน KOPE กับ action และ data หลัก ส่วนสีแดงใช้กับ North และ error
- ใช้ขอบบาง รัศมีมุม และระยะห่างชุดเดียวกัน แทน gradient, glow และสี accent หลายชุด
- แบ่งลำดับเป็น Brand → Connection → KPI → Instrument → Trend → System status
- ใช้คำกำกับสั้นและมีหน่วยเสมอ เพื่อให้ผู้ใช้มองแล้วตัดสินใจได้โดยไม่ต้องอ่านคำโปรย

## 1. Flash firmware

เปิด Terminal ที่ repo `STM32F3Discover` แล้วรัน:

```powershell
cargo run --release --bin lesson12-javafx-motion-stream
```

เมื่อ Programming สำเร็จให้กด `Ctrl+C` ได้ Firmware จะยังทำงานจาก Flash ต่อ เพราะ `Ctrl+C`
หยุดเฉพาะ `probe-rs` ที่เฝ้า debug session

Firmware ใช้ `115200 baud, 8-N-1` และส่งข้อมูล 10 ครั้งต่อวินาทีผ่าน USART1 PC4/PC5 ที่เชื่อมกับ
ST-LINK VCP บนบอร์ด E02

## 2. ตรวจ COM port

```powershell
[System.IO.Ports.SerialPort]::GetPortNames()
```

เครื่องที่ใช้สร้างบทนี้แสดงเป็น `COM14` แต่หมายเลขอาจเปลี่ยนเมื่อย้ายช่อง USB

ห้ามเปิด Serial Terminal และ JavaFX บน COM port เดียวกันพร้อมกัน เพราะหนึ่งพอร์ตมีเจ้าของได้ครั้งละหนึ่งโปรแกรม

## 3. เปิด Dashboard

จาก repo `Java_OOP_DesktopApp`:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-motion-dashboard.ps1
```

จากนั้น:

1. เลือกพอร์ต STMicroelectronics/STLink เช่น `COM14`
2. กด **เชื่อมต่อ USB**
3. รอให้ Badge เปลี่ยนเป็น `LIVE USB`
4. วางบอร์ดราบแล้วหมุนบนโต๊ะ สังเกตเข็มและค่า Heading
5. เอียงซ้าย/ขวาและก้ม/เงย สังเกต Roll/Pitch
6. หมุนบอร์ดรอบแกนต่าง ๆ สังเกตกราฟ Gyroscope

ทดสอบเส้นทาง Java → jSerialComm → USB → STM32 โดยไม่เปิด UI:

```powershell
.\mvnw.cmd -q test-compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java `
  "-Dexec.mainClass=smartfactory.motion.MotionSerialSmokeTest" `
  "-Dexec.classpathScope=test" "-Dexec.args=COM14"
```

ผลผ่านต้องได้รับ `5 DATA frames`, parse เป็น `MotionSample` และมี `errors=0`

## Serial protocol version 1

Firmware ส่ง ASCII หนึ่ง record ต่อบรรทัด จึงเปิดดูด้วย PuTTY/Tera Term ได้และ parse ใน Java ได้ง่าย:

```text
HELLO,STM32F3DISCOVERY,MOTION_STREAM,1,115200
DATA,1250,273,6,-12,18,1004,35,-61,17,-220,45,-31,0
```

ตำแหน่ง field ของ `DATA`:

| ลำดับ | Field | หน่วย |
|---:|---|---|
| 1 | tick | TIM2 tick ที่ 100 Hz |
| 2 | heading | degree `0–359` |
| 3 | sector | `0–7` สำหรับ LED |
| 4–6 | ax, ay, az | milli-g |
| 7–9 | gx, gy, gz | milli-degree/second |
| 10–12 | mx, my, mz | raw magnetometer |
| 13 | errors | จำนวนครั้งที่อ่าน I²C/SPI ไม่สำเร็จ |

Java แยกหน้าที่เป็น:

| Class | ความรับผิดชอบ |
|---|---|
| `MotionSample` | Parse และ validate DATA frame พร้อมคำนวณ Pitch/Roll |
| `MotionSerialClient` | เปิด/ปิด COM port และประกอบ byte เป็นบรรทัด |
| `MotionDashboardController` | ประสาน Event กับ JavaFX Application Thread |
| `CompassView` | วาดเข็มทิศด้วย Canvas |
| `MotionDashboardApp` | โหลด FXML/CSS และจัด lifecycle ของหน้าต่าง |

Callback ของ jSerialComm ไม่ใช่ JavaFX Application Thread ดังนั้น Controller ต้องใช้
`Platform.runLater(...)` ก่อนแก้ Label, Chart หรือ Canvas หากแก้ Control ตรงจาก callback อาจเกิด
race condition หรือ exception แบบเกิดบ้างไม่เกิดบ้าง

## ความละเอียดและข้อจำกัด

เซนเซอร์ Magnetometer ส่งตัวเลขละเอียดหลายระดับ และโปรแกรมคำนวณ Heading เป็น `0–359°` ได้
แต่ **ความละเอียดของตัวเลขไม่เท่ากับความแม่นยำจริง** เพราะยังมีผลจาก:

- hard-iron offset จากโลหะและแม่เหล็กใกล้บอร์ด
- soft-iron distortion ทำให้วงกลมของ X/Y กลายเป็นวงรี
- สาย USB, ลำโพง, มอเตอร์ และโต๊ะโลหะ
- การเอียงบอร์ด เพราะ firmware รุ่นนี้ยังเป็น flat compass ไม่ได้ทำ tilt compensation
- ค่าชดเชยใน firmware มาจากบอร์ดตัวอย่างเพียงหนึ่งตัว

ดังนั้น Dashboard นี้เหมาะกับ Demo และการเรียน data pipeline ก่อน ส่วนงานนำทางจริงควรเพิ่ม calibration
แบบเก็บค่าใหม่, tilt compensation, low-pass filter และ magnetic declination

## คำสั่งที่ Dashboard ส่งกลับไป

| คำสั่ง | ผล |
|---|---|
| `hello` | ขอข้อมูล protocol/device ซ้ำ |
| `start` | เปิด DATA stream |
| `stop` | หยุด DATA stream แต่ sensor ยังถูกอ่าน |
| `status` | อ่าน counters และ Gyro bias |
| `zero` | เฉลี่ย Gyro 200 sample ต้องวางนิ่งประมาณ 20 วินาทีที่ stream 10 Hz |
| `north` | ใช้มุมปัจจุบันเป็น 0° จนกว่าจะ Reset |

## ปัญหาที่พบบ่อย

### เปิด COM port ไม่ได้

ปิด PuTTY, Tera Term, PowerShell serial console และ JavaFX หน้าต่างอื่นที่ใช้พอร์ตเดียวกัน แล้วกด
**สแกนใหม่** ก่อนเชื่อมต่ออีกครั้ง

### เชื่อมต่อได้แต่ไม่มี DATA

ตรวจว่า flash `lesson12-javafx-motion-stream` ไม่ใช่ `lesson11-vcp-console` จากนั้นกด Reset บนบอร์ด
และกดเชื่อมต่อใหม่ Dashboard จะส่ง `hello` กับ `start` ให้อัตโนมัติ

### เข็มชี้ไม่ตรงทิศจริง

ย้ายบอร์ดออกจากโลหะและแม่เหล็ก วางราบ แล้วหมุนครบ 360° หากต้องการ Demo ที่ทิศเริ่มต้นเป็นศูนย์
ให้กด **ตั้งทิศนี้เป็น 0°** การทำเช่นนี้เป็นการหมุนหน้าปัด ไม่ใช่ calibration เต็มรูปแบบ

## แบบฝึกหัด

เหตุใดจึงไม่ควรอ่าน Serial port ด้วย `readLine()` บน JavaFX Application Thread แม้ข้อมูลมีเพียง 10 Hz?

<details>
  <summary>ดูแนวคิดและเฉลย</summary>

`readLine()` อาจรอข้อมูลโดยไม่รู้ว่าจะเสร็จเมื่อใด หากรอบหนึ่งช้าหรือถอดสาย Thread ที่ใช้วาดหน้าจอจะถูกบล็อก
ทำให้ปุ่ม กราฟ และการลากหน้าต่างค้าง โครงการจึงให้ jSerialComm รับข้อมูลนอก UI thread แล้วส่งเฉพาะ
model ที่ parse สำเร็จกลับมาด้วย `Platform.runLater(...)`

</details>

## ไอเดียต่อยอด

1. เพิ่มหน้าจอ calibration ที่แสดงจุด X/Y เป็น scatter plot และคำนวณ offset/range
2. บันทึก calibration ลง Flash พร้อม version และ checksum
3. เพิ่ม tilt-compensated compass โดยรวม Accelerometer กับ Magnetometer
4. บันทึก CSV และ replay session โดยไม่ต้องต่อบอร์ด
5. ส่งข้อมูลจาก JavaFX ต่อเข้า MQTT เพื่อเชื่อมกับ Rust IoT backend

กลับไป [README ของ Playlist 3](README.md)
