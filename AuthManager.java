package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AuthManager {

    public static boolean authenticate(String path, String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\|");
                if (parts.length >= 2) {
                    String storedUsername = parts[0].trim();
                    String storedPassword = parts[1].trim();
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + path);
            e.printStackTrace();
        }
        return false;
    }
}
