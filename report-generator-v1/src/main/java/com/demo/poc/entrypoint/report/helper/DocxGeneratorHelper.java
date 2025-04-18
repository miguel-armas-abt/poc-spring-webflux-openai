package com.demo.poc.entrypoint.report.helper;

import com.demo.poc.commons.custom.exceptions.DocxReadException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DocxGeneratorHelper {

  public Mono<byte[]> generateReport(String templatePath, Map<String, String> data) {
    return Mono.fromCallable(() -> {
      InputStream template = getClass().getResourceAsStream(templatePath);
      return processTemplate(template, data);
    });
  }

  private static byte[] processTemplate(InputStream templateInputStream,
                                       Map<String, String> variables) {

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try (XWPFDocument doc = new XWPFDocument(templateInputStream)) {

      for (XWPFParagraph p : doc.getParagraphs()) {
        replaceVariablesInParagraph(p, variables);
      }

      for (XWPFTable table : doc.getTables()) {
        for (XWPFTableRow row : table.getRows()) {
          for (XWPFTableCell cell : row.getTableCells()) {
            for (XWPFParagraph p : cell.getParagraphs()) {
              replaceVariablesInParagraph(p, variables);
            }
          }
        }
      }

      doc.write(out);

    } catch (IOException exception) {
      throw new DocxReadException(exception.getMessage());
    }
    return out.toByteArray();
  }

  private static void replaceVariablesInParagraph(XWPFParagraph paragraph, Map<String, String> variables) {
    StringBuilder fullText = new StringBuilder();
    for (XWPFRun run : paragraph.getRuns()) {
      fullText.append(run.getText(0));
    }

    String replacedText = fullText.toString();
    for (Map.Entry<String, String> entry : variables.entrySet()) {
      replacedText = replacedText.replace(entry.getKey(), entry.getValue());
    }

    int runCount = paragraph.getRuns().size();
    for (int i = runCount - 1; i >= 0; i--) {
      paragraph.removeRun(i);
    }

    String[] lines = replacedText.split("\n");
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i].trim();

      XWPFRun run = paragraph.createRun();
      run.setText(line);

      if (i < lines.length - 1) {
        run.addBreak();
      }
    }
  }
}
