package org.codeus.design_patterns.mediator.refactored.service;

import org.codeus.design_patterns.mediator.refactored.dto.LimitCheckResult;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.ILimitCheckService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LimitCheckService implements ILimitCheckService {

  @Override
  public LimitCheckResult checkDailyLimit(String userId, BigDecimal amount) {
    System.out.println("LimitCheckService: Checking daily limits...");

    BigDecimal dailyLimit = new BigDecimal("5000");
    BigDecimal currentUsage = getCurrentDailyUsage(userId);

    boolean exceedLimits = currentUsage.add(amount).compareTo(dailyLimit) > 0;

    return new LimitCheckResult(exceedLimits, dailyLimit, currentUsage, userId, amount);
  }

  private BigDecimal getCurrentDailyUsage(String userId) {
    // Simulate fetching current daily usage
    return new BigDecimal("2000");
  }
}

