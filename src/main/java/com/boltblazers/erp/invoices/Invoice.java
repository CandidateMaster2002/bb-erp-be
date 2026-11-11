package com.boltblazers.erp.invoices;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "terms")
    private String terms;

    @Column(name = "status")
    private String status;

    // Biller info
    @Column(name = "biller_name")
    private String billerName;

    @Column(name = "biller_company")
    private String billerCompany;

    @Column(name = "biller_address", columnDefinition = "TEXT")
    private String billerAddress;

    @Column(name = "biller_city")
    private String billerCity;

    @Column(name = "biller_state")
    private String billerState;

    @Column(name = "biller_pincode")
    private String billerPincode;

    @Column(name = "biller_country")
    private String billerCountry;

    @Column(name = "biller_gstin")
    private String billerGstin;

    @Column(name = "biller_phone")
    private String billerPhone;

    @Column(name = "biller_email")
    private String billerEmail;

    // Client info
    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_company")
    private String clientCompany;

    @Column(name = "client_address", columnDefinition = "TEXT")
    private String clientAddress;

    @Column(name = "client_city")
    private String clientCity;

    @Column(name = "client_state")
    private String clientState;

    @Column(name = "client_pincode")
    private String clientPincode;

    @Column(name = "client_country")
    private String clientCountry;

    @Column(name = "client_gstin")
    private String clientGstin;

    @Column(name = "client_phone")
    private String clientPhone;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "place_of_supply")
    private String placeOfSupply;

    // Bank details
    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "upi_id")
    private String upiId;

    // Financial calculations
    @Column(name = "currency")
    private String currency;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "tax_type")
    private String taxType;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "balance_due")
    private BigDecimal balanceDue;

    @Column(name = "total_in_words", columnDefinition = "TEXT")
    private String totalInWords;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(name = "items_json", columnDefinition = "TEXT", nullable = false)
    private String itemsJson;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
