package io.builders.demo.blockchain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetTransactionCount

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

@Component
class DefaultDistributedNonceProvider implements DistributedNonceProvider {

    @Autowired
    private Web3j web3j

    private final Map<String, BigInteger> nonceCache = new ConcurrentHashMap<>()

    private final Map<String, Lock> lockCache = new ConcurrentHashMap<>()

    @Override
    BigInteger getNonce(String address) {
        return nonceCache.getOrDefault(address, getRemoteNonce(address))
    }

    @Override
    void setNonce(String address, BigInteger nonce) {
        nonceCache.put(address, nonce)
    }

    @Override
    void lockNonce(String address) {
        getLock(address).lock()
    }

    @Override
    void unlockNonce(String address) {
        getLock(address).unlock()
    }

    private Lock getLock(String address) {
        return lockCache.computeIfAbsent(address, { new ReentrantLock() })
    }

    private BigInteger getRemoteNonce(String address) {
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send()
        return ethGetTransactionCount.transactionCount
    }

}
