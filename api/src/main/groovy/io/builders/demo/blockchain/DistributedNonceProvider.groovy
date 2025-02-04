package io.builders.demo.blockchain

interface DistributedNonceProvider extends NonceProvider {

    /**
     * Lock nonce by address
     * @param address Address
     */
    void lockNonce(String address)

    /**
     * Unlock a locked nonce by address
     * @param address Address
     */
    void unlockNonce(String address)

}
