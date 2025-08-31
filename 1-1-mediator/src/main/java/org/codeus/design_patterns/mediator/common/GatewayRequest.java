package org.codeus.design_patterns.mediator.common;

import java.math.BigDecimal;

public class GatewayRequest {
    private String userId;
    private String merchantId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;

    // Constructors
    public GatewayRequest() {}

    public GatewayRequest(String userId, String merchantId, BigDecimal amount,
                          String currency, String paymentMethod) {
        this.userId = userId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
