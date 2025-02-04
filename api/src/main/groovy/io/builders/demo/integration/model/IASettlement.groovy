package io.builders.demo.integration.model

import groovy.transform.ToString

@ToString
@SuppressWarnings(['FactoryMethodName'])
class IASettlement {

    private static String currentCode = 'AA'

    BigDecimal tokenAmount
    BigDecimal cashAmount
    String buyer
    String seller

    String id

    static IASettlement create(BigDecimal tokenAmount, BigDecimal cashAmount, String buyer, String seller) {
        new IASettlement(
            tokenAmount: tokenAmount,
            cashAmount: cashAmount,
            buyer: buyer,
            seller: seller,
            id: nextCode()
        )
    }

    BigDecimal cashAmountByClient(String client) {
        if (client == buyer) {
            return -cashAmount
        }
        if (client == seller) {
            return cashAmount
        }
        BigDecimal.ZERO
    }

    BigDecimal tokenAmountByClient(String client) {
        if (client == buyer) {
            return tokenAmount
        }
        if (client == seller) {
            return -tokenAmount
        }
        BigDecimal.ZERO
    }

    private static String nextCode() {
        String next
        synchronized (IASettlement) {
            next = currentCode
            currentCode = nextcode(currentCode)
        }
        return next
    }

    private static String nextcode(String id) {
        char[] chars = id.toCharArray()
        for (int i = chars.length - 1; i >= 0; i--) {
            if (chars[i] < 'Z') {
                chars[i]++
                break
            } else {
                chars[i] = 'A'
            }
        }
        return new String(chars)
    }

}
