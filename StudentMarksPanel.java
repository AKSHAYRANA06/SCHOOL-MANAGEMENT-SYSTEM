package gui;

import utils.FileManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentMarksPanel extends JPanel {
    public StudentMarksPanel(String studentUsername) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Marks", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Subject", "Marks"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        List<String> lines = FileManager.readFile("data/marks.txt");
        boolean found = false;

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3 && parts[0].equals(studentUsername)) {
                model.addRow(new Object[]{parts[1], parts[2]});
                found = true;
            }
        }

        if (!found) {
            JLabel msg = new JLabel("Your result is not released yet.", JLabel.CENTER);
            msg.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            add(msg, BorderLayout.SOUTH);
        }
    }
}
