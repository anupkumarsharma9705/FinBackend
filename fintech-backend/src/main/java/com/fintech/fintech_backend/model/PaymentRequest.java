package com.fintech.fintech_backend.model;

public class PaymentRequest {
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private String currency;
    private String idempotencyKey; // we'll use this heavily later

    // Getters and Setters (Spring needs setters to parse incoming JSON)
    public String getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(String fromAccountId) { this.fromAccountId = fromAccountId; }

    public String getToAccountId() { return toAccountId; }
    public void setToAccountId(String toAccountId) { this.toAccountId = toAccountId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
}