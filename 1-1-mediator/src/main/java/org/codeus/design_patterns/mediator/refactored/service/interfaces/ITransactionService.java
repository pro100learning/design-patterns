package org.codeus.design_patterns.mediator.refactored.service.interfaces;

import org.codeus.design_patterns.mediator.common.Transaction;
import org.codeus.design_patterns.mediator.common.TransactionStatus;
import org.codeus.design_patterns.mediator.common.PaymentRequest;

public interface ITransactionService {
    Transaction createTransaction(PaymentRequest request, TransactionStatus status);
}
