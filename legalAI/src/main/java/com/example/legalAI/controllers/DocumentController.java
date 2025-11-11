package com.example.legalAI.controllers;

import com.example.legalAI.dto.DocRequest;
import com.example.legalAI.services.DocxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/doc")
public class DocumentController {

    private final DocxService docxService;

    public DocumentController(DocxService docxService) {
        this.docxService = docxService;
    }

    @PostMapping(
            value = "/generate",
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    )
    public ResponseEntity<byte[]> generate(@RequestBody DocRequest request) throws IOException {

        // Template file name WITHOUT extension
        String templateName = switch (request.getTemplate()) {
            case "consumer" -> "ConsumerComplaint";
            case "rental"   -> "RentalAgreement";
            case "rti"      -> "RTITemplate";
            default -> throw new RuntimeException("Invalid template type: " + request.getTemplate());
        };

        byte[] filledDoc = docxService.fillTemplate(templateName, request.getData());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + templateName + "_Filled.docx")
                .body(filledDoc);
    }
}
