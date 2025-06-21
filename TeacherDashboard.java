package gui;

import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TeacherDashboard extends JFrame {
    private String teacherUsername;

    public TeacherDashboard(String username) {
        this.teacherUsername = username;

        setTitle("Teacher Dashboard - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Add Marks", new MarksPanel());
        tabs.add("Add Attendance", new TeacherAttendancePanel()); // ✅ CORRECT
        add(tabs);
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 10, 10));
        sidebar.setBackground(new Color(76, 175, 80));
        sidebar.setPreferredSize(new Dimension(200, 0));

        String[] actions = {"Enter Marks","Attendance", "View All Marks", "Logout"};

        for (String action : actions) {
            JButton btn = new JButton(action);
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btn.addActionListener(e -> handleAction(action));
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        JLabel label = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleAction(String action) {
        switch (action) {
            case "Enter Marks" -> enterMarks();
            case "View All Marks" -> viewAllMarks();
            case "Attendance" -> openAttendanceWindow();
            case "Logout" -> {
                dispose();
                new LoginFrame();
            }
        }
    }
    private void openAttendanceWindow() {
        JFrame frame = new JFrame("Add Attendance");
        frame.setContentPane(new TeacherAttendancePanel());
        frame.setSize(500, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void enterMarks() {
        List<String> students = FileManager.readFile("data/students.txt");
        List<String> subjects = FileManager.readFile("data/subjects.txt");

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found.");
            return;
        }

        if (subjects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subjects defined.");
            return;
        }

        // Ask teacher to enter student username
        String studentUsername = JOptionPane.showInputDialog(this, "Enter student username:");
        if (studentUsername == null || studentUsername.trim().isEmpty()) {
            return;
        }

        boolean studentExists = students.stream().anyMatch(line -> line.split("\\|")[0].equals(studentUsername.trim()));
        if (!studentExists) {
            JOptionPane.showMessageDialog(this, "Student not found.");
            return;
        }

        JPanel markPanel = new JPanel(new GridLayout(subjects.size(), 2, 5, 5));
        Map<String, JTextField> subjectFields = new HashMap<>();

        for (String subject : subjects) {
            markPanel.add(new JLabel(subject));
            JTextField field = new JTextField();
            subjectFields.put(subject, field);
            markPanel.add(field);
        }

        int result = JOptionPane.showConfirmDialog(this, markPanel, "Enter Marks", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            StringBuilder sb = new StringBuilder(studentUsername);
            for (String subject : subjects) {
                String mark = subjectFields.get(subject).getText().trim();
                sb.append("|").append(subject).append("=").append(mark);
            }
            FileManager.appendLine("data/marks.txt", sb.toString());
            JOptionPane.showMessageDialog(this, "Marks saved successfully.");
        }
    }

    private void viewAllMarks() {
        List<String> marks = FileManager.readFile("data/marks.txt");

        if (marks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No marks found.");
            return;
        }

        JTextArea textArea = new JTextArea(15, 40);
        textArea.setEditable(false);
        StringBuilder sb = new StringBuilder("Student Marks:\n\n");

        for (String line : marks) {
            String[] parts = line.split("\\|");
            sb.append("Student: ").append(parts[0]).append("\n");
            for (int i = 1; i < parts.length; i++) {
                sb.append("  ").append(parts[i]).append("\n");
            }
            sb.append("\n");
        }

        textArea.setText(sb.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "All Marks", JOptionPane.INFORMATION_MESSAGE);
    }
}
