package org.codeus.design_patterns.mediator.dirty.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LimitCheckService {

  private final AuditService auditService;
  private final NotificationService notificationService;

  public LimitCheckService(AuditService auditService,
                           NotificationService notificationService) {
    this.auditService = auditService;
    this.notificationService = notificationService;
  }

  public boolean checkDailyLimit(String userId, BigDecimal amount) {
    System.out.println("LimitCheckService: Checking daily limits...");

    // Simulate daily limit check
    BigDecimal dailyLimit = new BigDecimal("5000");
    BigDecimal currentUsage = getCurrentDailyUsage(userId); // Simulate

    if (currentUsage.add(amount).compareTo(dailyLimit) > 0) {
      auditService.logLimitViolation(userId, amount, dailyLimit);
      notificationService.sendLimitWarning(userId, dailyLimit);
      return false;
    }

    return true;
  }

  private BigDecimal getCurrentDailyUsage(String userId) {
    // Simulate fetching current daily usage
    return new BigDecimal("2000");
  }
}
