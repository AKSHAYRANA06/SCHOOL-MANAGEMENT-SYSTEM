package gui;

import utils.FileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class























































































































































































































AdminDashboard extends JFrame {
    public AdminDashboard(String username) {
        setTitle("Admin Dashboard - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 10, 10));
        sidebar.setBackground(new Color(33, 150, 243));
        sidebar.setPreferredSize(new Dimension(200, 0));

        String[] actions = {"Add Student", "Add Teacher", "Define Subjects", "Logout"};

        for (String action : actions) {
            JButton btn = new JButton(action);
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            btn.addActionListener(e -> handleAction(action));
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);

        // Welcome label
        JLabel welcome = new JLabel("Welcome, Admin!", SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(welcome, BorderLayout.CENTER);

        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleAction(String action) {
        switch (action) {
            case "Add Student" -> addUser("Student", "data/students.txt");
            case "Add Teacher" -> addUser("Teacher", "data/teachers.txt");
            case "Define Subjects" -> defineSubject();
            case "Logout" -> {
                dispose();
                new LoginFrame();
            }
        }
    }

    private void addUser(String role, String filepath) {
        JTextField unameField = new JTextField();
        JPasswordField passField = new JPasswordField();
        Object[] fields = {
                role + " Username:", unameField,
                role + " Password:", passField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Add " + role, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String uname = unameField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (!uname.isEmpty() && !pass.isEmpty()) {
                FileManager.appendLine(filepath, uname + "|" + pass);
                JOptionPane.showMessageDialog(this, role + " added!");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter all fields.");
            }
        }
    }

    private void defineSubject() {
        String subject = JOptionPane.showInputDialog(this, "Enter new subject:");
        if (subject != null && !subject.trim().isEmpty()) {
            FileManager.appendLine("data/subjects.txt", subject.trim());
            JOptionPane.showMessageDialog(this, "Subject added!");
        }
    }
}
