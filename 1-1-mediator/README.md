# Mediator Design Pattern

This repository contain exercise on Mediator Design Pattern.

## Prerequisites

The project represents a small Payment System. It can validate payment data using Basic validation, Limits and Fraud
Detection. Once validation is finished, it uses payment gateway to .
On top of this, system tries to audit important events and notify developers.

## Main Flow Diagram

```
┌─────────────────────────────┐
│   PaymentProcessingService  │ ◄─── Entry Point
└─────────────┬───────────────┘
              │
              ▼
              ├──────────────────────────────────┐────────────────────────────────────────────┐
              │                                  │                                            │
              ▼                                  ▼                                            ▼
┌─────────────────────────┐            ┌─────────────────────────┐                  ┌─────────────────────────┐
│   LimitCheckService     │            │   FraudDetectionService │                  │  PaymentGatewayService  │
└─────────────┬───────────┘            └─────────────┬───────────┘                  └─────────────────────────┘
              │                                      │
              ├──────────────┐                       ├──────────────────┐
              │              │                       │                  │
              ▼              ▼                       ▼                  ▼
    ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
    │  AuditService   │ │NotificationServ.│ │RiskAssessmentSv.│ │NotificationServ.│
    └─────────────────┘ └─────────────────┘ └─────────────┬───┘ └─────────────────┘
                                                          │
                                                          ├──────────────┐
                                                          │              │
                                                          ▼              ▼
                                                ┌─────────────────┐ ┌─────────────────┐
                                                │ AccountService  │ │  AuditService   │
                                                └─────────────────┘ └─────────────────┘
```

**NOTE**: Almost all components are dependent on `AuditService` and `NotificationServ` (this is not displayed for
simplicity)

There two modules:

- [dirty](src%2Fmain%2Fjava%2Forg%2Fcodeus%2Fdesign_patterns%2Fmediator%2Fdirty) - contains raw, dirty Payment system
  solution which has problems
- [refactored](src%2Fmain%2Fjava%2Forg%2Fcodeus%2Fdesign_patterns%2Fmediator%2Frefactored) - will contain pretty
  refactored Payment system solution, somewhere in the future, but at the start it is the same

**!NOTE:** work in the [refactored](src%2Fmain%2Fjava%2Forg%2Fcodeus%2Fdesign_patterns%2Fmediator%2Frefactored) module
and keep [dirty](src%2Fmain%2Fjava%2Forg%2Fcodeus%2Fdesign_patterns%2Fmediator%2Fdirty) untouched for reference.

There two test suites:

- [/dirty/PaymentProcessingTest.java](src%2Ftest%2Fjava%2Forg%2Fcodeus%2Fdesign_patterns%2Fmediator%2Fdirty%2FPaymentProcessingTest.java) -
  tests for dirty solution
- [/refactored/PaymentProcessingTest.java](src%2Ftest%2Fjava%2Forg%2Fcodeus%2Fdesign_patterns%2Fmediator%2Frefactored%2FPaymentProcessingTest.java) -
  tests for refactored solution, run to verify if your refactoring keeps the same business logic

---

## Exercise Goal

Analyze the code and try to refactor it using different techniques, including applying the Mediator pattern somewhere 😁

Keep an eye on following requirements:

- components should conform Single Responsibility Principle as close as possible;
- components should have as a little dependencies as possible
- Mediator class should have supportive Responsibility: orchestrating components and handling cross-cutting concerns

---

## Hints:

<details> 
<summary>Hint 1: from where to start?</summary>
org.codeus.design_patterns.mediator.dirty.payment.PaymentProcessingService is a good base for mediator, but you should remove all business logic from it. See requirements above.
</details>
<details> 
<summary>Hint 2 [Light]: handling payment validation logic part: </summary>
Are all business logic encapsulated in a separate service?
Should validation-related services be somehow grouped?
</details>
<details> 
<summary>Hint 3 [Detailed]: handling payment validation logic part: </summary>
You could extract amount validation logic to a separate service using the previous hint.
So, now you can have another Mediator/Facade orchestrating all validation steps and handling their cross-cutting concerns.
Once done, PaymentProcessingService (aka main Mediator) will have only one convenient dependency on validation and each validation step can be reused in another implementation of the validation-related Mediator/Facade.
</details>
<details> 
<summary>Hint 4 [Light]: handling payment logic part</summary>
Are all business logic encapsulated in a separate service?
How would you arrange payment and gateway functionalities?
</details>
<details> 
<summary>Hint 5 [Detailed]: handling payment logic part</summary>
You could extract GatewayRequest preparation logic to a separate PaymentService using the previous hint.
So, now you that new service may interact with the GatewayService.
Once done, PaymentProcessingService (aka main Mediator) will have only one convenient dependency on the PaymentService and each validation step can be reused in another implementation of the validation-related Mediator/Facade.
</details>

---

## Test cases summary:

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

These tests ensure complete coverage of all business logic paths without depending on internal service implementations,
making them perfect for testing before and after refactoring.

---

## 🔥 Extra

Once refactored with the Mediator pattern, try to refactor further and implement Event-Driven Mediator (or another
variation which you've not implemented in the main part)
