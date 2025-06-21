package gui;

import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AttendancePanel extends JPanel {
    public AttendancePanel() {
        setLayout(null);

        JLabel title = new JLabel("Mark Attendance");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBounds(130, 20, 200, 25);
        add(title);

        JLabel studentLabel = new JLabel("Select Student:");
        studentLabel.setBounds(40, 70, 120, 25);
        add(studentLabel);

        JComboBox<String> studentBox = new JComboBox<>();
        List<String> studentLines = FileManager.readFile("data/students.txt");
        for (String line : studentLines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 1) {
                studentBox.addItem(parts[0]);
            }
        }
        studentBox.setBounds(160, 70, 180, 25);
        add(studentBox);

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectLabel.setBounds(40, 110, 120, 25);
        add(subjectLabel);

        String[] subjects = {"Maths", "English", "Science"};
        JComboBox<String> subjectBox = new JComboBox<>(subjects);
        subjectBox.setBounds(160, 110, 180, 25);
        add(subjectBox);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(40, 150, 120, 25);
        add(statusLabel);

        JRadioButton presentBtn = new JRadioButton("Present");
        JRadioButton absentBtn = new JRadioButton("Absent");
        presentBtn.setBounds(160, 150, 80, 25);
        absentBtn.setBounds(240, 150, 80, 25);
        ButtonGroup group = new ButtonGroup();
        group.add(presentBtn);
        group.add(absentBtn);
        add(presentBtn);
        add(absentBtn);

        JButton submit = new JButton("Submit");
        submit.setBounds(140, 200, 100, 30);
        add(submit);

        submit.addActionListener(e -> {
            String student = (String) studentBox.getSelectedItem();
            String subject = (String) subjectBox.getSelectedItem();
            String status = presentBtn.isSelected() ? "present" : "absent";
            String date = LocalDate.now().toString();

            if (student == null || subject == null || (!presentBtn.isSelected() && !absentBtn.isSelected())) {
                JOptionPane.showMessageDialog(this, "Please select all options");
                return;
            }

            String line = student + "|" + subject + "|" + date + "|" + status;
            FileManager.appendLine("data/attendance.txt", line);
            JOptionPane.showMessageDialog(this, "Attendance marked!");
        });
    }
}
