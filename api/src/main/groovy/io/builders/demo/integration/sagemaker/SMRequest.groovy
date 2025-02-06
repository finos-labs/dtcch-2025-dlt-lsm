package io.builders.demo.integration.sagemaker

import groovy.transform.ToString

@ToString
class SMRequest {

    List<SMSettlement> settlements
    List<SMBalance> balances

}
