package com.diamam.k8s.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@EnableConfigurationProperties(WelcomeController.AppProperties.class)
@RequiredArgsConstructor
@Controller
public class WelcomeController {
    private final AppProperties properties;

    @GetMapping("/")
    public String toSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

    @Operation(description = "Тестовое API для работы k8s", parameters = @Parameter(name = "name", example = "ADMIN"),
            responses = @ApiResponse(responseCode = "200", content = @Content(examples = @ExampleObject(value = "WELCOME ADMIN"))))
    @GetMapping("/welcome")
    @ResponseBody
    public String welcome(@RequestParam("name") String name) {
        return "WELCOME %s".formatted(name);
    }

    @Operation(description = "Показать содержимое файла",
            responses = @ApiResponse(responseCode = "200"))
    @GetMapping("/payload")
    @ResponseBody
    public String payload() {
        try (Stream<String> lines = Files.lines(Paths.get(properties.filePath))) {
            return lines.reduce("", String::join);
        } catch (IOException e) {
            return "";
        }
    }


    @Operation(method = "POST", description = "Ввод сообщения",
            parameters = @Parameter(name = "payload", example = "some text"),
            responses = @ApiResponse(responseCode = "200"))
    @PostMapping("input")
    public String input(@RequestParam String payload) {
        try (OutputStreamWriter writer = new FileWriter(properties.filePath, true);) {
            writer.write(payload);
            return "redirect:/payload";
        } catch (IOException e) {
            return "redirect:/welcome";
        }
    }

    @Operation(method = "DELETE", description = "Удаление сообщений",
            responses = @ApiResponse(responseCode = "204"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/clean")
    public void clean() {
        try (OutputStreamWriter writer = new FileWriter(properties.getFilePath())) {
//            writer.write("");
            writer.flush();
        } catch (IOException e) {
        }
    }

    @Data
//    @Configuration
    @ConfigurationProperties(prefix = "app")
    public static class AppProperties {
        private String filePath;
    }
}
