
package com.example.FinTech_Digital_Banking.Controller;




import com.example.FinTech_Digital_Banking.Service.StatementService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/statements")
public class StatementController {

    private final StatementService statementService;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("/monthly")
    public ResponseEntity<byte[]> getMonthlyStatement(
            @RequestParam Long accountId,
            @RequestParam String month) throws Exception {

        // Parse "YYYY-MM" string into YearMonth
        YearMonth ym;
        try {
            ym = YearMonth.parse(month);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid month format. Use YYYY-MM");
        }

        byte[] pdfBytes = statementService.generateMonthlyStatement(accountId, ym);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=VaultCore_Statement_" + accountId + "_" + month + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
