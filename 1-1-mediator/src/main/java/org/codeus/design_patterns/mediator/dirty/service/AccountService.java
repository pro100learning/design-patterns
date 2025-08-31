package org.codeus.design_patterns.mediator.dirty.service;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
    
    public boolean isNewAccount(String userId) {
        // Simulate new account check
        return "new_user".equals(userId);
    }
}

