package com.example.mini_project.controller;

import com.example.mini_project.service.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class CalculatorController {
    private CalculatorService calculatorService;

    // constructor injection
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping(("/add"))
    public int add(@RequestParam("a") int a,@RequestParam("b") int b) {
        return calculatorService.add(a, b);
    }

    @GetMapping("/subtract")
    public int sub(@RequestParam("a") int a,@RequestParam("b") int b) {
        return calculatorService.sub(a, b);
    }

    @GetMapping("/multiply")
    public int multiply(@RequestParam("a") int a,@RequestParam("b") int b) {
        return calculatorService.mul(a, b);
    }

    @GetMapping("/divide")
    public int divide(@RequestParam("a") int a,@RequestParam("b") int b) {
        return calculatorService.div(a, b);
    }
}
