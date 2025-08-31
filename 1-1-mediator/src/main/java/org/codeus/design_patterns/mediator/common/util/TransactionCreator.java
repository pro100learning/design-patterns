package org.codeus.design_patterns.mediator.common.util;

import org.codeus.design_patterns.mediator.common.GatewayRequest;
import org.codeus.design_patterns.mediator.common.Transaction;
import org.codeus.design_patterns.mediator.common.TransactionStatus;
import org.codeus.design_patterns.mediator.common.PaymentRequest;

import java.util.UUID;

public class TransactionCreator {

  public static Transaction createTransaction(GatewayRequest request, TransactionStatus status) {
    String transactionId = UUID.randomUUID().toString();
    return new Transaction(transactionId, request.getUserId(),
      request.getMerchantId(), request.getAmount(),
      request.getCurrency(), status);
  }

  public static Transaction createDeclinedTransaction(PaymentRequest request, TransactionStatus status) {
    return new Transaction(null, request.getUserId(),
      request.getMerchantId(), request.getAmount(),
      request.getCurrency(), status);
  }
}

