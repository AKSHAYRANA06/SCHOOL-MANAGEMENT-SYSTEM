package gui;

import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TeacherAttendancePanel extends JPanel {
    private JComboBox<String> studentBox, subjectBox, statusBox;
    private JButton submit;
    private JLabel statusIndicator;

    public TeacherAttendancePanel() {
        setLayout(null);

        JLabel title = new JLabel("Mark Attendance");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBounds(130, 20, 200, 25);
        add(title);

        JLabel studentLabel = new JLabel("Select Student:");
        studentLabel.setBounds(40, 70, 120, 25);
        add(studentLabel);

        studentBox = new JComboBox<>();
        List<String> students = FileManager.readFile("data/students.txt");
        for (String line : students) {
            String[] parts = line.split("\\|");
            if (parts.length > 0) {
                studentBox.addItem(parts[0]);
            }
        }
        studentBox.setBounds(160, 70, 180, 25);
        add(studentBox);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(40, 110, 120, 25);
        add(subjectLabel);

        subjectBox = new JComboBox<>();
        List<String> subjects = FileManager.readFile("data/subjects.txt");
        for (String subject : subjects) {
            subjectBox.addItem(subject);
        }
        subjectBox.setBounds(160, 110, 180, 25);
        add(subjectBox);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(40, 150, 120, 25);
        add(statusLabel);

        statusBox = new JComboBox<>(new String[]{"Present", "Absent"});
        statusBox.setBounds(160, 150, 180, 25);
        add(statusBox);

        submit = new JButton("Submit");
        submit.setBounds(140, 200, 100, 30);
        add(submit);

        statusIndicator = new JLabel("", SwingConstants.CENTER);
        statusIndicator.setBounds(100, 240, 200, 25);
        add(statusIndicator);

        studentBox.addActionListener(e -> checkAttendanceStatus());
        subjectBox.addActionListener(e -> checkAttendanceStatus());

        submit.addActionListener(e -> submitAttendance());
    }

    private void checkAttendanceStatus() {
        String student = (String) studentBox.getSelectedItem();
        String subject = (String) subjectBox.getSelectedItem();
        String today = LocalDate.now().toString();

        if (student == null || subject == null) return;

        List<String> attendance = FileManager.readFile("data/attendance.txt");
        boolean alreadyMarked = attendance.stream()
                .anyMatch(line -> line.equals(student + "|" + subject + "|" + today + "|Present") ||
                        line.equals(student + "|" + subject + "|" + today + "|Absent"));

        if (alreadyMarked) {
            submit.setBackground(Color.LIGHT_GRAY);
            statusIndicator.setText("Already marked for today");
        } else {
            submit.setBackground(new JButton().getBackground());
            statusIndicator.setText("");
        }
    }

    private void submitAttendance() {
        String student = (String) studentBox.getSelectedItem();
        String subject = (String) subjectBox.getSelectedItem();
        String status = (String) statusBox.getSelectedItem();
        String date = LocalDate.now().toString();

        if (student == null || subject == null) {
            JOptionPane.showMessageDialog(this, "Please select both student and subject.");
            return;
        }

        String record = student + "|" + subject + "|" + date + "|" + status;
        List<String> attendance = FileManager.readFile("data/attendance.txt");
        boolean alreadyMarked = attendance.stream()
                .anyMatch(line -> line.startsWith(student + "|" + subject + "|" + date));

        if (alreadyMarked) {
            JOptionPane.showMessageDialog(this, "Attendance already marked for this subject today.");
            return;
        }

        FileManager.appendLine("data/attendance.txt", record);
        JOptionPane.showMessageDialog(this, "Attendance saved.");
        checkAttendanceStatus();
    }
}
