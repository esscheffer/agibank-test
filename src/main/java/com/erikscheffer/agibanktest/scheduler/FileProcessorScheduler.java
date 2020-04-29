package com.erikscheffer.agibanktest.scheduler;

import com.erikscheffer.agibanktest.handler.FileHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileProcessorScheduler {

    private final FileHandler fileHandler;

    public FileProcessorScheduler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Scheduled(fixedDelay = 60000) // Every minute
    public void process() {
        File[] files = fileHandler.getAllFiles();

        for (File file : files) {
            System.out.println(fileHandler.readFileLines(file));
        }
    }
}
