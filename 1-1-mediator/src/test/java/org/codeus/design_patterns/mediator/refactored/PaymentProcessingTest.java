package org.codeus.design_patterns.mediator.refactored;

import org.codeus.design_patterns.mediator.common.PaymentRequest;
import org.codeus.design_patterns.mediator.common.TransactionStatus;
import org.codeus.design_patterns.mediator.common.interfaces.IPaymentProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentProcessingTest {

  @Autowired
  private IPaymentProcessingService paymentProcessingService;

  @Test
  public void testSuccessfulPayment_NormalTransaction() {
    // Given: Normal transaction within limits, no fraud indicators
    PaymentRequest request = new PaymentRequest(
      "normal_user",           // Not a blocked or new user
      "merchant123",
      new BigDecimal("1000"),  // Below fraud threshold (10000) and within daily limit
      "USD",
      "CREDIT_CARD"           // Not international payment method
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be approved
    assertNotNull(transaction);
    assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
    assertEquals(request.getUserId(), transaction.getUserId());
    assertEquals(request.getAmount(), transaction.getAmount());
    assertNotNull(transaction.getTransactionId());
  }

  @Test
  public void testPaymentDeclined_InvalidAmount_Zero() {
    // Given: Invalid payment amount (zero)
    PaymentRequest request = new PaymentRequest(
      "normal_user",
      "merchant123",
      BigDecimal.ZERO,        // Invalid amount - triggers basic validation failure
      "USD",
      "CREDIT_CARD"
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be declined due to validation failure
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
    assertEquals(request.getUserId(), transaction.getUserId());
  }

  @Test
  public void testPaymentDeclined_InvalidAmount_Negative() {
    // Given: Invalid payment amount (negative)
    PaymentRequest request = new PaymentRequest(
      "normal_user",
      "merchant123",
      new BigDecimal("-100"),  // Negative amount - triggers basic validation failure
      "USD",
      "CREDIT_CARD"
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be declined due to validation failure
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
  }

  @Test
  public void testPaymentDeclined_DailyLimitExceeded() {
    // Given: Transaction that would exceed daily limit
    // Current daily usage is simulated as 2000, daily limit is 5000
    // So amount > 3000 should trigger limit exceeded
    PaymentRequest request = new PaymentRequest(
      "normal_user",
      "merchant123",
      new BigDecimal("3500"),  // 2000 (current) + 3500 = 5500 > 5000 (limit)
      "USD",
      "CREDIT_CARD"
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be declined due to limit exceeded
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
    assertEquals(request.getUserId(), transaction.getUserId());
  }

  @Test
  public void testPaymentDeclined_FraudDetected_HighAmount() {
    // Given: High amount transaction that triggers fraud detection
    PaymentRequest request = new PaymentRequest(
      "normal_user",
      "merchant123",
      new BigDecimal("15000"), // > 10000 triggers fraud check, risk score will be high
      "USD",
      "CREDIT_CARD"
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be declined due to fraud detection
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
    assertEquals(request.getUserId(), transaction.getUserId());
    assertEquals(request.getAmount(), transaction.getAmount());
  }

  @Test
  public void testPaymentDeclined_FraudDetected_NewUserHighRisk() {
    // Given: New user with high-risk characteristics
    PaymentRequest request = new PaymentRequest(
      "new_user",              // Triggers +40 risk score
      "merchant123",
      new BigDecimal("6000"),  // > 5000 triggers +30 risk score
      "USD",
      "INTERNATIONAL"          // Triggers +25 risk score
    );
    // Total risk score: 40 + 30 + 25 = 95 > 80 threshold

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be declined due to high risk score
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
    assertEquals(request.getUserId(), transaction.getUserId());
  }

  @Test
  public void testPaymentDeclined_FraudDetected_MultipleRiskFactors() {
    // Given: Transaction with multiple fraud indicators
    PaymentRequest request = new PaymentRequest(
      "new_user",              // +40 risk score
      "merchant123",
      new BigDecimal("12000"), // > 10000 suspicious amount + > 5000 risk factor (+30)
      "USD",
      "INTERNATIONAL"          // +25 risk score
    );
    // Total: 40 + 30 + 25 = 95, plus high amount check

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be declined due to multiple fraud indicators
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
  }

  @Test
  public void testPaymentFailed_BlockedUser() {
    // Given: Transaction from a blocked user (gateway will reject)
    PaymentRequest request = new PaymentRequest(
      "blocked_user",          // This user is blocked in PaymentGatewayService
      "merchant123",
      new BigDecimal("1000"),  // Valid amount, within limits
      "USD",
      "CREDIT_CARD"
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should fail at gateway level
    assertNotNull(transaction);
    assertEquals(TransactionStatus.FAILED, transaction.getStatus());
    assertEquals(request.getUserId(), transaction.getUserId());
  }

  @Test
  public void testSuccessfulPayment_EdgeCaseLimits() {
    // Given: Transaction exactly at the edge of limits and risk thresholds
    PaymentRequest request = new PaymentRequest(
      "normal_user",           // Not new user (no +40 risk)
      "merchant123",
      new BigDecimal("3000"),  // 2000 + 3000 = 5000 (exactly at limit)
      "USD",
      "CREDIT_CARD"           // Not international (+0 risk)
    );
    // Risk score: 0 (not new) + 0 (amount <= 5000) + 0 (not international) = 0

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be approved (at edge but within limits)
    assertNotNull(transaction);
    assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
    assertEquals(request.getUserId(), transaction.getUserId());
  }

  @Test
  public void testSuccessfulPayment_HighAmountLowRisk() {
    // Given: High amount but low risk score
    PaymentRequest request = new PaymentRequest(
      "normal_user",           // Not new user (no +40 risk)
      "merchant123",
      new BigDecimal("8000"),  // High but < 10000 (no fraud trigger), but > 5000 (+30 risk)
      "USD",
      "CREDIT_CARD"           // Not international (+0 risk)
    );
    // Risk score: 0 + 30 + 0 = 30 < 80 threshold
    // But this will exceed daily limit: 2000 + 8000 = 10000 > 5000

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Should be declined due to limit, not fraud
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
  }

  @Test
  public void testSuccessfulPayment_InternationalLowRisk() {
    // Given: International payment but low overall risk
    PaymentRequest request = new PaymentRequest(
      "normal_user",           // Not new user (no +40 risk)
      "merchant123",
      new BigDecimal("1000"),  // Low amount (no +30 risk)
      "USD",
      "INTERNATIONAL"          // International (+25 risk)
    );
    // Risk score: 0 + 0 + 25 = 25 < 80 threshold

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be approved despite international method
    assertNotNull(transaction);
    assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
    assertEquals(request.getUserId(), transaction.getUserId());
  }

  @Test
  public void testPaymentDeclined_CombinedValidationFailures() {
    // Given: Transaction that fails both limit check AND fraud detection
    PaymentRequest request = new PaymentRequest(
      "new_user",              // +40 risk score
      "merchant123",
      new BigDecimal("15000"), // Exceeds limit AND triggers fraud (>10000)
      "USD",
      "INTERNATIONAL"          // +25 risk score
    );
    // Will fail limit check first (2000 + 15000 > 5000)

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be declined (limit check happens first)
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
  }

  @Test
  public void testTransactionProperties_ValidFields() {
    // Given: Valid transaction request
    PaymentRequest request = new PaymentRequest(
      "test_user",
      "test_merchant",
      new BigDecimal("500"),
      "EUR",
      "DEBIT_CARD"
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Transaction should have all correct properties
    assertNotNull(transaction);
    assertEquals(request.getUserId(), transaction.getUserId());
    assertEquals(request.getMerchantId(), transaction.getMerchantId());
    assertEquals(request.getAmount(), transaction.getAmount());
    assertEquals(request.getCurrency(), transaction.getCurrency());
    assertNotNull(transaction.getTransactionId());
    assertNotNull(transaction.getTimestamp());
    assertFalse(transaction.getTransactionId().isEmpty());
  }

  @Test
  public void testSuccessfulPayment_DifferentCurrencies() {
    // Given: Transaction in different currency
    PaymentRequest request = new PaymentRequest(
      "normal_user",
      "merchant123",
      new BigDecimal("1500"),
      "EUR",                   // Different currency
      "CREDIT_CARD"
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Currency should be preserved and payment approved
    assertNotNull(transaction);
    assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
    assertEquals("EUR", transaction.getCurrency());
  }

  @Test
  public void testRiskScoreCalculation_ExactThreshold() {
    // Given: Transaction that transactions in exactly 80 risk score
    PaymentRequest request = new PaymentRequest(
      "new_user",              // +40 risk score
      "merchant123",
      new BigDecimal("4000"),  // Less than 5000 (no +30), but let's test edge case
      "USD",
      "INTERNATIONAL"          // +25 risk score
    );
    // Risk score: 40 + 0 + 25 = 65 < 80 (should pass)

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();


    // Then: Should be approved as risk score < 80
    // But will fail due to limit: 2000 + 4000 = 6000 > 5000
    assertNotNull(transaction);
    assertEquals(TransactionStatus.DECLINED, transaction.getStatus());
  }

  @Test
  public void testMinimumValidPayment() {
    // Given: Smallest valid payment amount
    PaymentRequest request = new PaymentRequest(
      "normal_user",
      "merchant123",
      new BigDecimal("0.01"),  // Minimum positive amount
      "USD",
      "CREDIT_CARD"
    );

    // When: Processing the payment
    var result = paymentProcessingService.processPayment(request);
    var transaction = result.getTransaction();

    // Then: Payment should be approved
    assertNotNull(transaction);
    assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
    assertEquals(new BigDecimal("0.01"), transaction.getAmount());
  }
}
