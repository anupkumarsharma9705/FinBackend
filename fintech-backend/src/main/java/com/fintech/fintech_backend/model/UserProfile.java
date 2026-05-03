package com.fintech.fintech_backend.model;

public class UserProfile {
    private String userId;
    private String name;
    private String email;
    private String trustTier; // LOW, MEDIUM, HIGH — we'll use this later

    public UserProfile(String userId, String name, String email, String trustTier) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.trustTier = trustTier;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getTrustTier() { return trustTier; }
}