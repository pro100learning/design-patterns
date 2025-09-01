package org.codeus.design_patterns.mediator.refactored.service;

import org.codeus.design_patterns.mediator.refactored.service.interfaces.IAccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

  @Override
  public boolean isNewAccount(String userId) {
    return "new_user".equals(userId);
  }
}
