package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void writeFile(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void appendLine(String filename, String line) {
        try {
            File file = new File(filename);
            boolean needsNewline = false;

            if (file.exists() && file.length() > 0) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(file.length() - 1);
                int lastChar = raf.read();
                raf.close();
                if (lastChar != '\n') needsNewline = true;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                if (needsNewline) writer.newLine();
                writer.write(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}