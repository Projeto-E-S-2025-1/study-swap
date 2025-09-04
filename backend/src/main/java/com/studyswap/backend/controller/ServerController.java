package com.studyswap.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
    @GetMapping("/")
    public String healthCheck() {
        return "Servidor em funcionamento ✅";
    }
}
