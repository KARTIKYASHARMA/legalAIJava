package com.example.legalAI.dto;


import lombok.Data;

import java.util.Map;

@Data
public class DocRequest {
    private String template;        // "consumer", "rental", "rti"
    private Map<String, String> data;
}
