package com.example.legalAI.services;


import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class DocxService {

    public byte[] fillTemplate(String templateName, Map<String, String> values) throws IOException {

        String path = "/templates/docx/" + templateName + ".docx";

        InputStream is = getClass().getResourceAsStream(path);

        if (is == null) {
            throw new FileNotFoundException("Template not found: " + path);
        }

        XWPFDocument doc = new XWPFDocument(is);

        // Replace text
        for (XWPFParagraph p : doc.getParagraphs()) {
            replaceInParagraph(values, p);
        }

        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(values, p);
                    }
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.write(baos);
        return baos.toByteArray();
    }

    private static void replaceInParagraph(Map<String, String> map, XWPFParagraph paragraph) {

        String paragraphText = paragraph.getText();
        if (paragraphText == null) return;

        for (String key : map.keySet()) {
            paragraphText = paragraphText.replace("{{" + key + "}}", map.get(key));
        }

        // Remove all runs then add updated one
        for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        XWPFRun newRun = paragraph.createRun();
        newRun.setText(paragraphText);
    }

}
