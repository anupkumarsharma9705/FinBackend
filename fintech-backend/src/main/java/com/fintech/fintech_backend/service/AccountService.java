package com.fintech.fintech_backend.service;

import com.fintech.fintech_backend.model.AccountBalance;
import com.fintech.fintech_backend.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    public AccountBalance getBalance(String accountId) {
        // Hardcoded for now — simulating a DB call
        return new AccountBalance(accountId, 50000.00, "INR");
    }

    public UserProfile getProfile(String userId) {
        // Hardcoded for now — simulating a DB call
        return new UserProfile(userId, "Anup Kumar", "anup@fintech.com", "MEDIUM");
    }
}