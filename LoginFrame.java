package gui;

import utils.AuthManager;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("School Management System - Login");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Custom Styles
        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color background = new Color(240, 240, 240);
        Color buttonColor = new Color(33, 150, 243);
        Color textColor = Color.WHITE;

        JPanel panel = new JPanel(null);
        panel.setBackground(background);

        JLabel title = new JLabel("Login to SMS");
        title.setFont(titleFont);
        title.setBounds(120, 30, 200, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        userLabel.setBounds(50, 90, 100, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 90, 180, 30);
        panel.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        passLabel.setBounds(50, 140, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 140, 180, 30);
        panel.add(passField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(labelFont);
        roleLabel.setBounds(50, 190, 100, 25);
        panel.add(roleLabel);

        String[] roles = {"Admin", "Teacher", "Student"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setBounds(150, 190, 180, 30);
        panel.add(roleCombo);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(labelFont);
        loginBtn.setBackground(buttonColor);
        loginBtn.setForeground(textColor);
        loginBtn.setFocusPainted(false);
        loginBtn.setBounds(130, 260, 120, 40);
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String role = roleCombo.getSelectedItem().toString().toLowerCase();
            String path = "data/" + role + "s.txt";

            if (AuthManager.authenticate(path, username, password)) {
                dispose();
                switch (role) {
                    case "admin" -> new AdminDashboard(username);
                    case "teacher" -> new TeacherDashboard(username);
                    case "student" -> new StudentDashboard(username);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
        setVisible(true);
    }
}
