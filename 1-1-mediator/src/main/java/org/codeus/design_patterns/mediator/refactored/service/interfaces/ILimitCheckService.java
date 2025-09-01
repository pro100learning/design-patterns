package org.codeus.design_patterns.mediator.refactored.service.interfaces;

import org.codeus.design_patterns.mediator.refactored.dto.LimitCheckResult;

import java.math.BigDecimal;

public interface ILimitCheckService {
    LimitCheckResult checkDailyLimit(String userId, BigDecimal amount);
}
