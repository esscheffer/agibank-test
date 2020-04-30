package com.erikscheffer.agibanktest.scheduler;

import com.erikscheffer.agibanktest.handler.FileHandler;
import com.erikscheffer.agibanktest.processor.FileProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileProcessorScheduler {

    private final FileHandler fileHandler;
    private final FileProcessor fileProcessor;

    public FileProcessorScheduler(FileHandler fileHandler, FileProcessor fileProcessor) {
        this.fileHandler = fileHandler;
        this.fileProcessor = fileProcessor;
    }

    @Scheduled(fixedDelay = 60000) // Every minute
    public void process() {
        File[] files = fileHandler.getAllFiles();

        for (File file : files) {
            fileProcessor.processFile(file);
        }
    }
}
