package com.boltblazers.erp.invoices.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private String terms;
    private String status;

    // Biller info
    private String billerName;
    private String billerCompany;
    private String billerAddress;
    private String billerCity;
    private String billerState;
    private String billerPincode;
    private String billerCountry;
    private String billerGstin;
    private String billerPhone;
    private String billerEmail;

    // Client info
    private String clientName;
    private String clientCompany;
    private String clientAddress;
    private String clientCity;
    private String clientState;
    private String clientPincode;
    private String clientCountry;
    private String clientGstin;
    private String clientPhone;
    private String clientEmail;
    private String placeOfSupply;

    // Bank details
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String upiId;

    // Calculations
    private String currency;
    private BigDecimal subtotal;
    private String taxType;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal balanceDue;
    private String totalInWords;
    private String notes;
    private String termsAndConditions;

    private String itemsJson;
    private Instant createdAt;
    private Instant updatedAt;
}
