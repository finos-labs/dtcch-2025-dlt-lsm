package io.builders.demo.integration.sagemaker

import groovy.transform.ToString

@ToString
class SMSettlement {

    BigDecimal tokenAmount
    BigDecimal cashAmount
    String buyer
    String seller
    Integer id

}
