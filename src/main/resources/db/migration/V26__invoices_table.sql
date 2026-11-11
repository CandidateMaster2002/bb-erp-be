-- V26: Invoices table for invoice generator and management
CREATE TABLE IF NOT EXISTS invoices (
    id BIGSERIAL PRIMARY KEY,
    invoice_number VARCHAR(100) NOT NULL,
    invoice_date DATE,
    due_date DATE,
    terms VARCHAR(100),
    status VARCHAR(50) DEFAULT 'DRAFT',
    
    -- Biller details
    biller_name VARCHAR(255),
    biller_company VARCHAR(255),
    biller_address TEXT,
    biller_city VARCHAR(100),
    biller_state VARCHAR(100),
    biller_pincode VARCHAR(20),
    biller_country VARCHAR(100),
    biller_gstin VARCHAR(50),
    biller_phone VARCHAR(50),
    biller_email VARCHAR(100),
    
    -- Client details
    client_name VARCHAR(255),
    client_company VARCHAR(255),
    client_address TEXT,
    client_city VARCHAR(100),
    client_state VARCHAR(100),
    client_pincode VARCHAR(20),
    client_country VARCHAR(100),
    client_gstin VARCHAR(50),
    client_phone VARCHAR(50),
    client_email VARCHAR(100),
    place_of_supply VARCHAR(100),
    
    -- Bank / Payment details
    account_holder_name VARCHAR(255),
    bank_name VARCHAR(255),
    account_number VARCHAR(100),
    ifsc_code VARCHAR(50),
    upi_id VARCHAR(100),
    
    -- Financial summary
    currency VARCHAR(10) DEFAULT 'INR',
    subtotal NUMERIC(15, 2) DEFAULT 0,
    tax_type VARCHAR(50) DEFAULT 'IGST',
    tax_rate NUMERIC(5, 2) DEFAULT 18,
    tax_amount NUMERIC(15, 2) DEFAULT 0,
    total_amount NUMERIC(15, 2) DEFAULT 0,
    balance_due NUMERIC(15, 2) DEFAULT 0,
    total_in_words TEXT,
    notes TEXT,
    terms_and_conditions TEXT,
    
    -- Items stored as JSON string
    items_json TEXT NOT NULL DEFAULT '[]',
    
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_invoices_invoice_number ON invoices(invoice_number);
CREATE INDEX IF NOT EXISTS idx_invoices_created_at ON invoices(created_at DESC);
