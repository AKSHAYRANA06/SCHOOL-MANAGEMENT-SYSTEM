package gui;

import utils.FileManager;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDashboard extends JFrame {
    private String studentUsername;

    public StudentDashboard(String username) {
        this.studentUsername = username;

        setTitle("Student Dashboard - " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Header
        JLabel title = new JLabel("Welcome, " + username + "!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(new EmptyBorder(20, 20, 10, 20));
        add(title, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabs.add("📚 View Marks", createMarksCard());
        tabs.add("📈 View Attendance", createAttendanceCard());
        add(tabs, BorderLayout.CENTER);

        // Logout
        JPanel footer = new JPanel();
        footer.setBackground(new Color(245, 245, 245));
        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        footer.add(logout);
        add(footer, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createMarksCard() {
        JPanel card = createCardPanel();

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textArea.setEditable(false);
        textArea.setBackground(new Color(250, 250, 250));

        List<String> marks = FileManager.readFile("data/marks.txt");
        Optional<String> studentMarks = marks.stream()
                .filter(line -> line.startsWith(studentUsername + "|"))
                .findFirst();

        if (studentMarks.isPresent()) {
            String[] parts = studentMarks.get().split("\\|");
            StringBuilder sb = new StringBuilder("Subject-wise Marks:\n\n");
            for (int i = 1; i < parts.length; i++) {
                sb.append("• ").append(parts[i].replace("=", " : ")).append("\n");
            }
            textArea.setText(sb.toString());
        } else {
            textArea.setText("🟡 Your result is not released yet.");
        }

        card.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return card;
    }

    private JPanel createAttendanceCard() {
        JPanel card = createCardPanel();

        JLabel label = new JLabel("Subject-wise Attendance");
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(label, BorderLayout.NORTH);

        String[] columns = {"Subject", "Total Classes", "Attended", "Percentage"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        card.add(scrollPane, BorderLayout.CENTER);

        List<String> records = FileManager.readFile("data/attendance.txt");
        Map<String, Integer> total = new HashMap<>();
        Map<String, Integer> attended = new HashMap<>();

        for (String line : records) {
            String[] parts = line.split("\\|");
            if (parts.length != 4) continue;
            String student = parts[0];
            String subject = parts[1];
            String status = parts[3];

            if (!student.equals(studentUsername)) continue;

            total.put(subject, total.getOrDefault(subject, 0) + 1);
            if (status.equalsIgnoreCase("Present")) {
                attended.put(subject, attended.getOrDefault(subject, 0) + 1);
            }
        }

        int totalAll = 0, attendedAll = 0;

        for (String subject : total.keySet()) {
            int t = total.get(subject);
            int a = attended.getOrDefault(subject, 0);
            totalAll += t;
            attendedAll += a;
            int percent = (t == 0) ? 0 : (a * 100 / t);
            model.addRow(new Object[]{subject, t, a, percent + "%"});
        }

        JLabel overall = new JLabel("📊 Overall Attendance: " +
                (totalAll == 0 ? "0%" : (attendedAll * 100 / totalAll) + "%"));
        overall.setFont(new Font("Segoe UI", Font.BOLD, 16));
        overall.setBorder(new EmptyBorder(10, 0, 0, 0));
        overall.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(overall, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new EmptyBorder(15, 15, 15, 15),
                new LineBorder(new Color(220, 220, 220), 1, true)
        ));
        return panel;
    }
}
