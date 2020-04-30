package com.erikscheffer.agibanktest.handler;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileHandler {
    public File[] getAllFiles() {
        String userHome = System.getenv("HOMEPATH");
        File folder = new File(userHome + "\\data\\in");

        if (!folder.exists()) {
            throw new RuntimeException("Input folder does not exists!");
        }

        if (!folder.isDirectory()) {
            throw new RuntimeException("Input path is not a folder!");
        }

        return folder.listFiles();
    }

    public List<String> readFileLines(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public void writeToFile(List<String> lines, String fileName) {
        try {
            String userHome = System.getenv("HOMEPATH");
            Path file = Paths.get(userHome + "\\data\\out\\" + fileName);
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
