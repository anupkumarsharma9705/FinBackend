package com.fintech.fintech_backend.model;

public class AccountBalance {
    private String accountId;
    private double balance;
    private String currency;

    public AccountBalance(String accountId, double balance, String currency) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
    }

    // Getters
    public String getAccountId() { return accountId; }
    public double getBalance() { return balance; }
    public String getCurrency() { return currency; }
}