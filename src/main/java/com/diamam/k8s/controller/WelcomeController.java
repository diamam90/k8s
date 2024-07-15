package com.diamam.k8s.controller;

import com.diamam.k8s.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class WelcomeController {

    private final FileService service;

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
        try {
            return service.readFromFile();
        } catch (IOException e) {
            return "";
        }
    }


    @Operation(method = "POST", description = "Ввод сообщения",
            parameters = @Parameter(name = "payload", example = "some text"),
            responses = @ApiResponse(responseCode = "200"))
    @PostMapping("input")
    public String input(@RequestParam String payload) {
        try {
            service.addToFile(payload);
            return "redirect:/payload";
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return "redirect:/welcome?name=username";
        }
    }

    @Operation(method = "DELETE", description = "Удаление сообщений",
            responses = @ApiResponse(responseCode = "204"))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/clean")
    public void clean() {
        try {
            service.clean();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
