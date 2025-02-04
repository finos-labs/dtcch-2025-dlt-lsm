package io.builders.demo.blockchain

import io.builders.demo.blockchain.configuration.BlockchainConfigurationProperties
import io.builders.demo.blockchain.exception.ContractNotFoundException
import io.builders.demo.dtcc.infrastructure.configuration.DtccConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.Contract
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.tx.response.TransactionReceiptProcessor

import java.lang.reflect.Method

@Component
class ContractLoader {

    @Autowired
    private Web3j web3j

    @Autowired
    private ContractGasProvider gasProvider

    @Autowired
    private BlockchainConfigurationProperties blockchainProperties

    @Autowired
    DtccConfigurationProperties dtccConfigurationProperties

    @Autowired
    private TransactionReceiptProcessor transactionReceiptProcessor

    @Autowired
    private DefaultDistributedNonceProvider defaultNonceProvider

    <T extends Contract> T load(Class<T> contractClass) throws Exception {
        BlockchainConfigurationProperties.ContractData<T> contractData = blockchainProperties.find(contractClass)
        if (contractData == null) {
            throw new ContractNotFoundException(contractClass)
        }

        Method loadMethod = contractClass.getMethod('load', String, Web3j, TransactionManager, ContractGasProvider)
        Credentials credentials = Credentials.create(dtccConfigurationProperties.privateKey)
        return contractClass.cast(loadMethod.invoke(
            null,
            contractData.address,
            web3j,
            new DefaultTransactionManager(
                web3j,
                credentials,
                blockchainProperties.chainId,
                transactionReceiptProcessor,
                defaultNonceProvider
            ),
            gasProvider
        ))
    }

}
