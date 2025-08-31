package org.codeus.design_patterns.mediator.common;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String userId;
    private String merchantId;
    private BigDecimal amount;
    private String currency;
    private TransactionStatus status;
    private LocalDateTime timestamp;
    
    // Constructors
    public Transaction() {}
    
    public Transaction(String transactionId, String userId, String merchantId, 
                      BigDecimal amount, String currency, TransactionStatus status) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccessful() {
      return status.equals(TransactionStatus.APPROVED);
    }
    
    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
