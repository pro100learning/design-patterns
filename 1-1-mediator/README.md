# Mediator Design Pattern

This repository contain exercise on Mediator Design Pattern.


### TEST CASES SUMMARY

1. **Successful Payment Scenarios:**
    - testSuccessfulPayment_NormalTransaction: Normal user, moderate amount, standard payment method
    - testSuccessfulPayment_EdgeCaseLimits: Transaction exactly at daily limit boundary
    - testSuccessfulPayment_InternationalLowRisk: International payment with low overall risk
    - testSuccessfulPayment_DifferentCurrencies: Payment in non-USD currency
    - testMinimumValidPayment: Smallest possible valid payment amount

2. **Basic Validation Failures:**
    - testPaymentDeclined_InvalidAmount_Zero: Zero amount payment
    - testPaymentDeclined_InvalidAmount_Negative: Negative amount payment

3. **Daily Limit Exceeded:**
    - testPaymentDeclined_DailyLimitExceeded: Transaction exceeds daily spending limit
    - testSuccessfulPayment_HighAmountLowRisk: High amount that exceeds limits despite low fraud risk

4. **Fraud Detection Scenarios:**
    - testPaymentDeclined_FraudDetected_HighAmount: Very high amount triggers fraud detection
    - testPaymentDeclined_FraudDetected_NewUserHighRisk: New user with multiple risk factors
    - testPaymentDeclined_FraudDetected_MultipleRiskFactors: Combination of all fraud indicators

5. **Gateway Processing Failures:**
    - testPaymentFailed_BlockedUser: User blocked at gateway level

6. **Complex Scenarios:**
    - testPaymentDeclined_CombinedValidationFailures: Transaction failing multiple validation types
    - testRiskScoreCalculation_ExactThreshold: Testing risk score calculation edge cases

7. **Data Integrity Tests:**
    - testTransactionProperties_ValidFields: Verification of all transaction field mappings

**Key Test Data Patterns:**
- "normal_user" = Regular user with no special risk factors
- "new_user" = New account (+40 risk score)
- "blocked_user" = User blocked at gateway level
- Amounts > 10000 = Triggers high amount fraud check
- Amounts > 5000 = Adds +30 to risk score
- "INTERNATIONAL" payment method = Adds +25 to risk score
- Daily limit = 5000, current usage = 2000
- Risk score > 80 = Fraud detection triggers
- Amount <= 0 = Basic validation failure

These tests ensure complete coverage of all business logic paths without depending on internal service implementations, making them perfect for testing before and after refactoring.
