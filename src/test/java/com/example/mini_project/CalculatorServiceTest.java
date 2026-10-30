
package com.example.mini_project;

import com.example.mini_project.service.CalculatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private final CalculatorService calculatorService =
            new CalculatorService();

    @Test
    void shouldAddTwoNumbers() {
        assertEquals(15, calculatorService.add(10, 5));
    }

    @Test
    void shouldSubtractTwoNumbers() {
        assertEquals(5, calculatorService.sub(10, 5));
    }

    @Test
    void shouldMultiplyTwoNumbers() {
        assertEquals(50, calculatorService.mul(10, 5));
    }

    @Test
    void shouldDivideTwoNumbers() {
        assertEquals(2, calculatorService.div(10, 5));
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculatorService.div(10, 0)
        );
    }
}
