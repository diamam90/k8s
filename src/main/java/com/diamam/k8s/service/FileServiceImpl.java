package com.diamam.k8s.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@EnableConfigurationProperties(FileServiceImpl.AppProperties.class)
public class FileServiceImpl implements FileService {

    private final AppProperties properties;

    @Override
    public void addToFile(String payload) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(properties.filePath, true))) {
            writer.append(String.valueOf(LocalDateTime.now())).append("\n").append(payload);
            writer.append("\n==============================\n");
        }
    }

    @Override
    public String readFromFile() throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(properties.filePath))) {
            return lines.collect(Collectors.joining("\n"));
        }
    }


    @Override
    public void clean() throws IOException {
        try (OutputStreamWriter writer = new FileWriter(properties.getFilePath())) {
            writer.flush();
        }
    }


    @Data
    @ConfigurationProperties(prefix = "app")
    public static class AppProperties {
        private String filePath;
    }

    public FileServiceImpl(AppProperties properties) {
        this.properties = properties;
        try {
            Files.createDirectories(Path.of(properties.getFilePath()).getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
