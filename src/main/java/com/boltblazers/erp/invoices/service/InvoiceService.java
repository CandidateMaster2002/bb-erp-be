package com.boltblazers.erp.invoices.service;

import com.boltblazers.erp.invoices.Invoice;
import com.boltblazers.erp.invoices.InvoiceRepository;
import com.boltblazers.erp.invoices.dto.InvoiceRequest;
import com.boltblazers.erp.invoices.dto.InvoiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return mapToResponse(invoice);
    }

    public InvoiceResponse createInvoice(InvoiceRequest request) {
        Invoice invoice = new Invoice();
        applyRequestToEntity(invoice, request);
        Invoice saved = invoiceRepository.save(invoice);
        return mapToResponse(saved);
    }

    public InvoiceResponse updateInvoice(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        applyRequestToEntity(invoice, request);
        Invoice updated = invoiceRepository.save(invoice);
        return mapToResponse(updated);
    }

    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new RuntimeException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }

    private void applyRequestToEntity(Invoice invoice, InvoiceRequest req) {
        invoice.setInvoiceNumber(req.getInvoiceNumber());
        invoice.setInvoiceDate(req.getInvoiceDate());
        invoice.setDueDate(req.getDueDate());
        invoice.setTerms(req.getTerms());
        invoice.setStatus(req.getStatus() != null ? req.getStatus() : "SAVED");

        // Biller
        invoice.setBillerName(req.getBillerName());
        invoice.setBillerCompany(req.getBillerCompany());
        invoice.setBillerAddress(req.getBillerAddress());
        invoice.setBillerCity(req.getBillerCity());
        invoice.setBillerState(req.getBillerState());
        invoice.setBillerPincode(req.getBillerPincode());
        invoice.setBillerCountry(req.getBillerCountry());
        invoice.setBillerGstin(req.getBillerGstin());
        invoice.setBillerPhone(req.getBillerPhone());
        invoice.setBillerEmail(req.getBillerEmail());

        // Client
        invoice.setClientName(req.getClientName());
        invoice.setClientCompany(req.getClientCompany());
        invoice.setClientAddress(req.getClientAddress());
        invoice.setClientCity(req.getClientCity());
        invoice.setClientState(req.getClientState());
        invoice.setClientPincode(req.getClientPincode());
        invoice.setClientCountry(req.getClientCountry());
        invoice.setClientGstin(req.getClientGstin());
        invoice.setClientPhone(req.getClientPhone());
        invoice.setClientEmail(req.getClientEmail());
        invoice.setPlaceOfSupply(req.getPlaceOfSupply());

        // Bank
        invoice.setAccountHolderName(req.getAccountHolderName());
        invoice.setBankName(req.getBankName());
        invoice.setAccountNumber(req.getAccountNumber());
        invoice.setIfscCode(req.getIfscCode());
        invoice.setUpiId(req.getUpiId());

        // Calculations
        invoice.setCurrency(req.getCurrency() != null ? req.getCurrency() : "INR");
        invoice.setSubtotal(req.getSubtotal());
        invoice.setTaxType(req.getTaxType());
        invoice.setTaxRate(req.getTaxRate());
        invoice.setTaxAmount(req.getTaxAmount());
        invoice.setTotalAmount(req.getTotalAmount());
        invoice.setBalanceDue(req.getBalanceDue());
        invoice.setTotalInWords(req.getTotalInWords());
        invoice.setNotes(req.getNotes());
        invoice.setTermsAndConditions(req.getTermsAndConditions());

        invoice.setItemsJson(req.getItemsJson() != null ? req.getItemsJson() : "[]");
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .invoiceDate(invoice.getInvoiceDate())
                .dueDate(invoice.getDueDate())
                .terms(invoice.getTerms())
                .status(invoice.getStatus())
                .billerName(invoice.getBillerName())
                .billerCompany(invoice.getBillerCompany())
                .billerAddress(invoice.getBillerAddress())
                .billerCity(invoice.getBillerCity())
                .billerState(invoice.getBillerState())
                .billerPincode(invoice.getBillerPincode())
                .billerCountry(invoice.getBillerCountry())
                .billerGstin(invoice.getBillerGstin())
                .billerPhone(invoice.getBillerPhone())
                .billerEmail(invoice.getBillerEmail())
                .clientName(invoice.getClientName())
                .clientCompany(invoice.getClientCompany())
                .clientAddress(invoice.getClientAddress())
                .clientCity(invoice.getClientCity())
                .clientState(invoice.getClientState())
                .clientPincode(invoice.getClientPincode())
                .clientCountry(invoice.getClientCountry())
                .clientGstin(invoice.getClientGstin())
                .clientPhone(invoice.getClientPhone())
                .clientEmail(invoice.getClientEmail())
                .placeOfSupply(invoice.getPlaceOfSupply())
                .accountHolderName(invoice.getAccountHolderName())
                .bankName(invoice.getBankName())
                .accountNumber(invoice.getAccountNumber())
                .ifscCode(invoice.getIfscCode())
                .upiId(invoice.getUpiId())
                .currency(invoice.getCurrency())
                .subtotal(invoice.getSubtotal())
                .taxType(invoice.getTaxType())
                .taxRate(invoice.getTaxRate())
                .taxAmount(invoice.getTaxAmount())
                .totalAmount(invoice.getTotalAmount())
                .balanceDue(invoice.getBalanceDue())
                .totalInWords(invoice.getTotalInWords())
                .notes(invoice.getNotes())
                .termsAndConditions(invoice.getTermsAndConditions())
                .itemsJson(invoice.getItemsJson())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }
}
