package io.builders.demo.integration.model

import groovy.transform.ToString

@ToString
class IASettlement {

    BigDecimal tokenAmount
    BigDecimal cashAmount
    String buyer
    String seller
    Integer id

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

}
