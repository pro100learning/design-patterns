package org.codeus.design_patterns.mediator.refactored.service;

import org.codeus.design_patterns.mediator.common.Transaction;
import org.codeus.design_patterns.mediator.common.TransactionStatus;
import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.refactored.service.interfaces.ITransactionService;

import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {
    
    @Override
    public Transaction createTransaction(PaymentRequest request, TransactionStatus status) {
        String transactionId = java.util.UUID.randomUUID().toString();
        return new Transaction(transactionId, request.getUserId(), 
                             request.getMerchantId(), request.getAmount(), 
                             request.getCurrency(), status);
    }
}
