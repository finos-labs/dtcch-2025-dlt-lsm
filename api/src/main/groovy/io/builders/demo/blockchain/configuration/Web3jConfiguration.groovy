package io.builders.demo.blockchain.configuration

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.tx.gas.StaticGasProvider
import org.web3j.tx.response.NoOpProcessor
import org.web3j.tx.response.TransactionReceiptProcessor

import java.util.concurrent.TimeUnit

@Configuration
class Web3jConfiguration {

    @Bean
    Web3j web3j(BlockchainConfigurationProperties blockchainProps) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
        Long tos = blockchainProps.timeout
        builder.connectTimeout(tos, TimeUnit.SECONDS)
        builder.readTimeout(tos, TimeUnit.SECONDS)
        builder.writeTimeout(tos, TimeUnit.SECONDS)
        Web3j.build(new HttpService(blockchainProps.url, builder.build()))
    }

    @Bean
    ContractGasProvider gasProvider(BlockchainConfigurationProperties blockchainProps) {
        new StaticGasProvider(blockchainProps.gasPrice, blockchainProps.gasLimit)
    }

    @Bean
    TransactionReceiptProcessor txReceiptProcessor(Web3j web3j) {
        return new NoOpProcessor(web3j)
    }

}
