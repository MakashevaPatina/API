package ru.hogwarts.school.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngoController {
    @Value("${server.port}")
    private String port;

    @GetMapping("/port")
    public String getPort() {
        return "Application is running on port: " + port;
    }

}