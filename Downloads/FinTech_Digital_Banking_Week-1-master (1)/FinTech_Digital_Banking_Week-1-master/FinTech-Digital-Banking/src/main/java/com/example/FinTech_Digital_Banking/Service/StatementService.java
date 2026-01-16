package com.example.FinTech_Digital_Banking.Service;

import com.example.FinTech_Digital_Banking.Entity.LedgerEntry;
import com.example.FinTech_Digital_Banking.Repository.LedgerEntryRepository;
import com.example.FinTech_Digital_Banking.Repository.AuditLogRepository;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;


import java.io.ByteArrayOutputStream;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
@Service
public class StatementService {

    private final LedgerEntryRepository ledgerRepo;
    private final AuditLogRepository auditRepo;

    public StatementService(LedgerEntryRepository ledgerRepo,AuditLogRepository auditRepo) {
        this.ledgerRepo = ledgerRepo;
        this.auditRepo=auditRepo;
    }

    public byte[] generateMonthlyStatement(Long accountId, YearMonth month) {

        OffsetDateTime start = month.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime end = month.atEndOfMonth().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);

        List<LedgerEntry> entries =
                ledgerRepo.findMonthlyEntries(accountId, start, end);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate()); // LANDSCAPE
            PdfWriter.getInstance(document, out);
            document.open();

            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font font = new Font(Font.HELVETICA, 9);

            document.add(new Paragraph("VaultCore Financial", headerFont));
            document.add(new Paragraph("Monthly Statement", headerFont));
            document.add(new Paragraph("Account ID: " + accountId));
            document.add(new Paragraph("Month: " + month));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSplitRows(true);   // ✅ CRITICAL
            table.setSplitLate(false);  // ✅ CRITICAL

            table.addCell(new PdfPCell(new Phrase("Date", font)));
            table.addCell(new PdfPCell(new Phrase("Description", font)));
            table.addCell(new PdfPCell(new Phrase("Type", font)));
            table.addCell(new PdfPCell(new Phrase("Amount", font)));

            for (LedgerEntry e : entries) {
                table.addCell(new PdfPCell(new Phrase(e.getEntryTimestamp().toString(), font)));
                table.addCell(new PdfPCell(new Phrase(
                        e.getDescription() == null ? "-" : e.getDescription(), font)));
                table.addCell(new PdfPCell(new Phrase(e.getEntryType(), font)));
                table.addCell(new PdfPCell(new Phrase(e.getAmount().toString(), font)));
            }

            document.add(table);
            document.close();
                // 🔹 Save audit log
       

            System.out.println("ROWS WRITTEN = " + entries.size());
            System.out.println("PDF SIZE = " + out.size());

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
