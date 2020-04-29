package com.erikscheffer.agibanktest;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileProcessorScheduler {

    @Scheduled(fixedDelay = 60000) // Every minute
    public void process() {
        System.out.println("Processing...");
    }
}
