package smartfactory.ui;

import smartfactory.model.Machine;
import smartfactory.model.MachineStatus;
import smartfactory.service.SmartFactoryService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Random;

/** หน้าต่าง Dashboard: UI รับ event แล้วเรียก service; ไม่เก็บ business logic ไว้ในหน้าจอ */
public class SmartFactoryFrame extends JFrame {
    private static final Color NAVY = new Color(20, 48, 74);
    private static final Color GREEN = new Color(31, 122, 75);
    private static final Color ORANGE = new Color(214, 120, 31);
    private static final Color RED = new Color(185, 52, 52);

    private final SmartFactoryService service;
    private final Random random = new Random();
    private final DefaultTableModel tableModel;
    private final JTable machineTable;
    private final JLabel totalValue = createSummaryValue();
    private final JLabel runningValue = createSummaryValue();
    private final JLabel warningValue = createSummaryValue();
    private final JLabel maintenanceValue = createSummaryValue();
    private final JButton autoButton = new JButton("เริ่มจำลองอัตโนมัติ");
    private final Timer simulationTimer;

    public SmartFactoryFrame(SmartFactoryService service) {
        super("Smart Factory Machine Monitor");
        this.service = service;

        String[] columns = {
                "รหัส", "ชื่อเครื่องจักร", "ตำแหน่ง", "สถานะ",
                "อุณหภูมิ (°C)", "การสั่น (mm/s)", "ชั่วโมง", "ควรบำรุง"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        machineTable = new JTable(tableModel);
        simulationTimer = new Timer(2_000, event -> simulateReadings());

        configureWindow();
        setContentPane(buildContent());
        refreshDashboard();
    }

    private void configureWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 580));
        setSize(1_120, 650);
        setLocationRelativeTo(null);
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(buildSummaryPanel(), BorderLayout.NORTH);
        center.add(buildTablePanel(), BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);
        root.add(buildActionPanel(), BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(NAVY);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel("SMART FACTORY MACHINE MONITOR");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Java Basic • OOP • Swing Desktop App");
        subtitle.setForeground(new Color(207, 222, 235));
        panel.add(subtitle, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.add(createSummaryCard("เครื่องจักรทั้งหมด", totalValue, NAVY));
        panel.add(createSummaryCard("กำลังทำงาน", runningValue, GREEN));
        panel.add(createSummaryCard("ต้องตรวจสอบ", warningValue, RED));
        panel.add(createSummaryCard("ควรบำรุง", maintenanceValue, ORANGE));
        return panel;
    }

    private JPanel createSummaryCard(String label, JLabel value, Color color) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 223, 228)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        JLabel caption = new JLabel(label, SwingConstants.CENTER);
        caption.setForeground(color);
        card.add(value, BorderLayout.CENTER);
        card.add(caption, BorderLayout.SOUTH);
        return card;
    }

    private static JLabel createSummaryValue() {
        JLabel label = new JLabel("0", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 28f));
        return label;
    }

    private JScrollPane buildTablePanel() {
        machineTable.setRowHeight(30);
        machineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        machineTable.setFillsViewportHeight(true);
        machineTable.getTableHeader().setReorderingAllowed(false);
        machineTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        machineTable.getColumnModel().getColumn(7).setCellRenderer(new MaintenanceCellRenderer());
        return new JScrollPane(machineTable);
    }

    private JPanel buildActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton addButton = new JButton("+ เพิ่มเครื่องจักร");
        JButton sensorButton = new JButton("กรอกค่าเซนเซอร์");
        JButton simulateButton = new JButton("จำลองค่า 1 ครั้ง");
        JButton maintainButton = new JButton("บำรุงรักษาเสร็จแล้ว");
        JButton deleteButton = new JButton("ลบ");

        addButton.addActionListener(event -> showAddMachineDialog());
        sensorButton.addActionListener(event -> showSensorDialog());
        simulateButton.addActionListener(event -> simulateReadings());
        maintainButton.addActionListener(event -> performMaintenance());
        deleteButton.addActionListener(event -> deleteMachine());
        autoButton.addActionListener(event -> toggleAutomaticSimulation());

        panel.add(addButton);
        panel.add(sensorButton);
        panel.add(simulateButton);
        panel.add(autoButton);
        panel.add(maintainButton);
        panel.add(deleteButton);
        return panel;
    }

    private void showAddMachineDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField locationField = new JTextField();
        JPanel form = createFormPanel(
                new String[]{"รหัส:", "ชื่อเครื่องจักร:", "ตำแหน่ง:"},
                new JTextField[]{idField, nameField, locationField}
        );

        int answer = JOptionPane.showConfirmDialog(
                this, form, "เพิ่มเครื่องจักร", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (answer != JOptionPane.OK_OPTION) {
            return;
        }

        runUiAction(() -> {
            service.addMachine(new Machine(idField.getText(), nameField.getText(), locationField.getText()));
            refreshDashboard();
        });
    }

    private void showSensorDialog() {
        Machine selected = getSelectedMachine();
        if (selected == null) {
            return;
        }

        JTextField temperatureField = new JTextField(
                String.format("%.1f", selected.getLatestReading().getTemperature())
        );
        JTextField vibrationField = new JTextField(
                String.format("%.1f", selected.getLatestReading().getVibration())
        );
        JPanel form = createFormPanel(
                new String[]{"อุณหภูมิ (°C):", "การสั่น (mm/s):"},
                new JTextField[]{temperatureField, vibrationField}
        );

        int answer = JOptionPane.showConfirmDialog(
                this, form, "ค่าเซนเซอร์: " + selected.getName(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (answer != JOptionPane.OK_OPTION) {
            return;
        }

        runUiAction(() -> {
            double temperature = Double.parseDouble(temperatureField.getText().trim());
            double vibration = Double.parseDouble(vibrationField.getText().trim());
            service.updateSensor(selected.getId(), temperature, vibration);
            refreshDashboard();
        });
    }

    private JPanel createFormPanel(String[] labels, JTextField[] fields) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        for (int index = 0; index < labels.length; index++) {
            panel.add(new JLabel(labels[index]));
            fields[index].setColumns(16);
            panel.add(fields[index]);
        }
        return panel;
    }

    private void simulateReadings() {
        service.simulateSensorReadings(random);
        refreshDashboard();
    }

    private void toggleAutomaticSimulation() {
        if (simulationTimer.isRunning()) {
            simulationTimer.stop();
            autoButton.setText("เริ่มจำลองอัตโนมัติ");
        } else {
            simulationTimer.start();
            autoButton.setText("หยุดจำลองอัตโนมัติ");
        }
    }

    private void performMaintenance() {
        Machine selected = getSelectedMachine();
        if (selected == null) {
            return;
        }
        service.performMaintenance(selected.getId());
        refreshDashboard();
    }

    private void deleteMachine() {
        Machine selected = getSelectedMachine();
        if (selected == null) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(
                this,
                "ลบ " + selected.getName() + " หรือไม่?",
                "ยืนยันการลบ",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (answer == JOptionPane.YES_OPTION) {
            service.removeMachine(selected.getId());
            refreshDashboard();
        }
    }

    private Machine getSelectedMachine() {
        int selectedRow = machineTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกเครื่องจักรในตารางก่อน");
            return null;
        }
        String id = tableModel.getValueAt(selectedRow, 0).toString();
        return service.findRequired(id);
    }

    private void runUiAction(Runnable action) {
        try {
            action.run();
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                    this, "กรุณากรอกตัวเลขให้ถูกต้อง", "ข้อมูลไม่ถูกต้อง", JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this, exception.getMessage(), "ข้อมูลไม่ถูกต้อง", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshDashboard() {
        tableModel.setRowCount(0);
        for (Machine machine : service.getMachines()) {
            tableModel.addRow(new Object[]{
                    machine.getId(),
                    machine.getName(),
                    machine.getLocation(),
                    machine.getStatus(),
                    String.format("%.1f", machine.getLatestReading().getTemperature()),
                    String.format("%.1f", machine.getLatestReading().getVibration()),
                    machine.getOperatingHours(),
                    machine.requiresMaintenance() ? "YES" : "NO"
            });
        }

        totalValue.setText(String.valueOf(service.getMachines().size()));
        runningValue.setText(String.valueOf(service.countByStatus(MachineStatus.RUNNING)));
        warningValue.setText(String.valueOf(service.countByStatus(MachineStatus.WARNING)));
        maintenanceValue.setText(String.valueOf(service.countRequiringMaintenance()));
    }

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            MachineStatus status = (MachineStatus) value;
            Component component = super.getTableCellRendererComponent(
                    table, status.getDisplayName(), isSelected, hasFocus, row, column
            );
            if (!isSelected) {
                component.setForeground(switch (status) {
                    case RUNNING -> GREEN;
                    case WARNING -> RED;
                    case MAINTENANCE -> ORANGE;
                    case OFFLINE -> Color.DARK_GRAY;
                });
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(getFont().deriveFont(Font.BOLD));
            return component;
        }
    }

    private static class MaintenanceCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
            );
            if (!isSelected) {
                component.setForeground("YES".equals(value) ? RED : GREEN);
            }
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(getFont().deriveFont(Font.BOLD));
            return component;
        }
    }
}

