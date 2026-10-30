package com.example.mini_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class CalculatorResponse {

    private String operation;
    private int a;
    private int b;
    private int result;

}
