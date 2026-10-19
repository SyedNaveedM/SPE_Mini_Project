package com.example.mini_project.controller;

import com.example.mini_project.service.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {
    private CalculatorService calculatorService;

    // constructor injection
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }
}
