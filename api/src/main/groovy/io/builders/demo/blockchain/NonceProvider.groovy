package io.builders.demo.blockchain

interface NonceProvider {

    /**
     * Obtains nonce by address
     * @param address Address
     * @return Returns a BigInteger (nonce)
     */
    BigInteger getNonce(String address)

    /**
     * Set nonce to address
     * @param address Address
     * @param nonce Nonce to set
     */
    void setNonce(String address, BigInteger nonce)

}
