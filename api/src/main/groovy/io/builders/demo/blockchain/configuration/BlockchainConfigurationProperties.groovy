package io.builders.demo.blockchain.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.web3j.tx.Contract

@Configuration
@ConfigurationProperties('blockchain')
class BlockchainConfigurationProperties {

    String url
    Integer chainId
    BigInteger gasPrice
    BigInteger gasLimit
    BigDecimal gasMultiplier
    Long timeout = 300L
    Map<String, ContractData> contracts = [:]

    static class ContractData<C extends Contract> {

        Class<C> contractClass
        String address

    }

    <C extends Contract> ContractData<C> find(Class<C> contractClass) {
        contracts.find { entry -> entry.value.contractClass == contractClass }?.value
    }

}
