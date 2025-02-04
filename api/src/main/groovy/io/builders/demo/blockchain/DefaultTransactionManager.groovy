package io.builders.demo.blockchain

import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.response.TransactionReceiptProcessor

import groovy.util.logging.Slf4j

@Slf4j
class DefaultTransactionManager extends RawTransactionManager {

    private final DistributedNonceProvider distributedNonceProvider

    DefaultTransactionManager(
        Web3j web3j,
        Credentials credentials,
        long chainId,
        TransactionReceiptProcessor transactionReceiptProcessor,
        DistributedNonceProvider distributedNonceProvider
    ) {
        super(web3j, credentials, chainId, transactionReceiptProcessor)
        this.distributedNonceProvider = distributedNonceProvider
    }

    @Override
    @SuppressWarnings('CatchException')
    EthSendTransaction sendTransaction(
        BigInteger gasPrice,
        BigInteger gasLimit,
        String to,
        String data,
        BigInteger value,
        boolean constructor
    ) throws IOException {
        long startTime = System.currentTimeMillis()
        try {
            this.distributedNonceProvider.lockNonce(this.fromAddress)
            BigInteger nonce = this.distributedNonceProvider.getNonce(this.fromAddress)
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data)
            log.info("""
                    Sending transaction with gasPrice: ${gasPrice}, gasLimit: ${gasLimit}, to: ${to},
                    data: ${data}, value: ${value}, nonce: ${nonce}
                """)
            EthSendTransaction transaction = this.signAndSend(rawTransaction)
            this.distributedNonceProvider.setNonce(this.fromAddress, nonce.add(BigInteger.ONE))
            long endTime = System.currentTimeMillis()
            log.info(
                "Transaction sent with hash ${transaction.transactionHash} with duration ${endTime - startTime} ms")
            return transaction
        } catch (Exception ex) {
            log.error("Exception when sending transaction with nonce ${nonce} and gas price ${gasPrice}", ex)
            throw ex
        } finally {
            this.distributedNonceProvider.unlockNonce(this.fromAddress)
        }
    }

}
