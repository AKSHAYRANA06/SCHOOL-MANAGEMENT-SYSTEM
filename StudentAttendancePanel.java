package gui;

import utils.FileManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StudentAttendancePanel extends JPanel {
    public StudentAttendancePanel(String studentUsername) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Attendance", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Subject", "Total Classes", "Attended", "Percentage"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        List<String> lines = FileManager.readFile("data/attendance.txt");
        Map<String, int[]> subjectAttendance = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4 && parts[0].equals(studentUsername)) {
                String subject = parts[1];
                String status = parts[3];
                int[] data = subjectAttendance.getOrDefault(subject, new int[2]); // [total, attended]
                data[0]++;
                if (status.equals("present")) data[1]++;
                subjectAttendance.put(subject, data);
            }
        }

        int totalClasses = 0, totalAttended = 0;

        for (Map.Entry<String, int[]> entry : subjectAttendance.entrySet()) {
            int total = entry.getValue()[0];
            int attended = entry.getValue()[1];
            double percent = total > 0 ? (attended * 100.0 / total) : 0;
            model.addRow(new Object[]{entry.getKey(), total, attended, String.format("%.2f%%", percent)});
            totalClasses += total;
            totalAttended += attended;
        }

        if (totalClasses > 0) {
            double overall = totalAttended * 100.0 / totalClasses;
            JLabel overallLabel = new JLabel("Overall Attendance: " + String.format("%.2f%%", overall), JLabel.CENTER);
            overallLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            add(overallLabel, BorderLayout.SOUTH);
        } else {
            JLabel noData = new JLabel("No attendance records yet.", JLabel.CENTER);
            noData.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            add(noData, BorderLayout.SOUTH);
        }
    }
}
