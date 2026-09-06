package com.velora.tracker.domain.model

enum class PaymentMethod {
    CASH, UPI, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, OTHER;
    
    fun displayName(): String = when(this) {
        CASH -> "Cash"
        UPI -> "UPI"
        CREDIT_CARD -> "Credit Card"
        DEBIT_CARD -> "Debit Card"
        BANK_TRANSFER -> "Bank Transfer"
        OTHER -> "Other"
    }
}
