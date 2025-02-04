package io.builders.demo.integration.model

class Balance {
    BigDecimal cashAmount
    BigDecimal tokenAmount

    static Balance create(BigDecimal cashAmount, BigDecimal tokenAmount) {
        new Balance(cashAmount: cashAmount, tokenAmount: tokenAmount)
    }

    static Balance empty() {
        new Balance(cashAmount: 0, tokenAmount: 0)
    }

    @Override
    String toString() {
        "\"cash\": $cashAmount, \"token\": $tokenAmount"
    }
}
