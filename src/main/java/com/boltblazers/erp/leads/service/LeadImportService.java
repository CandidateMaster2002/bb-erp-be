package com.boltblazers.erp.leads.service;

import com.boltblazers.erp.leads.*;
import com.boltblazers.erp.leads.dto.ImportSummaryResponse;
import com.opencsv.CSVReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Propagation;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Service
public class LeadImportService {

    private final ImportRepository importRepository;
    private final LeadRepository leadRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;
    private final LeadContactRepository contactRepository;
    private final LeadLinkedinRepository linkedinRepository;
    private final LeadJobRepository jobRepository;
    private final LeadEducationRepository educationRepository;
    private final BatchImportProcessor batchProcessor;

    public LeadImportService(
            ImportRepository importRepository,
            LeadRepository leadRepository,
            CategoryRepository categoryRepository,
            CompanyRepository companyRepository,
            LeadContactRepository contactRepository,
            LeadLinkedinRepository linkedinRepository,
            LeadJobRepository jobRepository,
            LeadEducationRepository educationRepository,
            BatchImportProcessor batchProcessor) {
        this.importRepository = importRepository;
        this.leadRepository = leadRepository;
        this.categoryRepository = categoryRepository;
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
        this.linkedinRepository = linkedinRepository;
        this.jobRepository = jobRepository;
        this.educationRepository = educationRepository;
        this.batchProcessor = batchProcessor;
    }

    public List<Import> getAllImports() {
        return importRepository.findAll();
    }

    public Import getImport(Long id) {
        return importRepository.findById(id).orElseThrow();
    }

    public ImportSummaryResponse processImport(MultipartFile file, List<Long> categoryIds) {
        ImportSummaryResponse summary = new ImportSummaryResponse();
        summary.setErrors(new ArrayList<>());
        
        Import imp = new Import();
        imp.setFileName(file.getOriginalFilename());
        importRepository.save(imp);
        
        summary.setImportId(imp.getId());

        List<Map<String, String>> rows = parseFile(file);
        summary.setTotalRows(rows.size());

        int batchSize = 100;
        List<Map<String, String>> batch = new ArrayList<>();
        
        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> row = rows.get(i);
            row.put("_row_index", String.valueOf(i + 2)); // 1-based + header
            batch.add(row);
            
            if (batch.size() >= batchSize || i == rows.size() - 1) {
                batchProcessor.processBatch(batch, imp, categoryIds, summary);
                batch.clear();
            }
        }
        
        imp.setTotalRows(summary.getTotalRows());
        imp.setNotes("Created: " + summary.getCreated() + ", Updated: " + summary.getUpdated());
        importRepository.save(imp);
        return summary;
    }

    private List<Map<String, String>> parseFile(MultipartFile file) {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            if (file.getOriginalFilename() != null && file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
                try (Reader reader = new InputStreamReader(file.getInputStream());
                     CSVReader csvReader = new CSVReader(reader)) {
                    String[] headers = csvReader.readNext();
                    if (headers == null) return result;
                    String[] line;
                    while ((line = csvReader.readNext()) != null) {
                        Map<String, String> row = new HashMap<>();
                        for (int i = 0; i < headers.length && i < line.length; i++) {
                            row.put(headers[i].trim().toLowerCase(), line[i].trim());
                        }
                        result.add(row);
                    }
                }
            } else {
                // Assuming Excel
                try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
                    Sheet sheet = workbook.getSheetAt(0);
                    Row headerRow = sheet.getRow(0);
                    if (headerRow == null) return result;
                    List<String> headers = new ArrayList<>();
                    for (Cell cell : headerRow) {
                        headers.add(cell.getStringCellValue().trim().toLowerCase());
                    }
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (row == null) continue;
                        Map<String, String> rowData = new HashMap<>();
                        for (int j = 0; j < headers.size(); j++) {
                            Cell cell = row.getCell(j);
                            rowData.put(headers.get(j), getCellValue(cell));
                        }
                        result.add(rowData);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse file", e);
        }
        return result;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
