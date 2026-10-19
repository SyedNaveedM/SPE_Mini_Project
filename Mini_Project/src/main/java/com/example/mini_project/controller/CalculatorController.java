
package com.example.mini_project.controller;

import com.example.mini_project.dto.CalculatorResponse;
import com.example.mini_project.service.CalculatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CalculatorController {

    private final CalculatorService calculatorService;

    // Constructor injection
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/add")
    public CalculatorResponse add(
            @RequestParam("a") int a,
            @RequestParam("b") int b) {

        int result = calculatorService.add(a, b);
        return new CalculatorResponse("add", a, b, result);
    }

    @GetMapping("/subtract")
    public CalculatorResponse sub(
            @RequestParam("a") int a,
            @RequestParam("b") int b) {

        int result = calculatorService.sub(a, b);
        return new CalculatorResponse("subtract", a, b, result);
    }

    @GetMapping("/multiply")
    public CalculatorResponse multiply(
            @RequestParam("a") int a,
            @RequestParam("b") int b) {

        int result = calculatorService.mul(a, b);
        return new CalculatorResponse("multiply", a, b, result);
    }

    @GetMapping("/divide")
    public CalculatorResponse divide(
            @RequestParam("a") int a,
            @RequestParam("b") int b) {

        int result = calculatorService.div(a, b);
        return new CalculatorResponse("divide", a, b, result);
    }
}
