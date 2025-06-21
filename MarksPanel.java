package gui;

import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MarksPanel extends JPanel {
    public MarksPanel() {
        setLayout(null);

        JLabel title = new JLabel("Add Attendance");
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

        JComboBox<String> subjectBox = new JComboBox<>();
        Set<String> subjects = new HashSet<>();

        // Optional: Load subjects from marks.txt so teachers don’t retype
        List<String> marksLines = FileManager.readFile("data/marks.txt");
        for (String line : marksLines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2) {
                subjects.add(parts[1]);
            }
        }

        // Add default subjects
        subjects.add("Maths");
        subjects.add("Science");
        subjects.add("English");

        for (String sub : subjects) {
            subjectBox.addItem(sub);
        }
        subjectBox.setBounds(160, 110, 180, 25);
        add(subjectBox);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(40, 150, 120, 25);
        add(statusLabel);

        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Present", "Absent"});
        statusBox.setBounds(160, 150, 180, 25);
        add(statusBox);

        JButton submit = new JButton("Submit");
        submit.setBounds(140, 200, 100, 30);
        add(submit);

        submit.addActionListener(e -> {
            String student = (String) studentBox.getSelectedItem();
            String subject = (String) subjectBox.getSelectedItem();
            String status = (String) statusBox.getSelectedItem();
            String date = LocalDate.now().toString();

            if (student == null || subject == null || status == null) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            String line = student + "|" + subject + "|" + date + "|" + status;
            FileManager.appendLine("data/attendance.txt", line);
            JOptionPane.showMessageDialog(this, "Attendance recorded.");
        });
    }
}
