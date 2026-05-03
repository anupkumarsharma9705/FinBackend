package com.fintech.fintech_backend.controller;

import com.fintech.fintech_backend.model.AccountBalance;
import com.fintech.fintech_backend.model.UserProfile;
import com.fintech.fintech_backend.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/balance")
    public ResponseEntity<AccountBalance> getBalance(@RequestParam String accountId) {
        AccountBalance balance = accountService.getBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile(@RequestParam String userId) {
        UserProfile profile = accountService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }
}