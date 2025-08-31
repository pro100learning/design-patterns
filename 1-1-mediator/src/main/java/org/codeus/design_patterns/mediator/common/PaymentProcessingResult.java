package org.codeus.design_patterns.mediator.common;

public class PaymentProcessingResult {
  protected final boolean successful;
  protected final Transaction transaction;

  public PaymentProcessingResult(Transaction transaction) {
    this.transaction = transaction;
    this.successful = transaction.isSuccessful();
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public boolean isSuccessful() {
    return successful;
  }
}