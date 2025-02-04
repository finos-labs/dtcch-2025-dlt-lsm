package io.builders.demo.blockchain

import io.builders.demo.blockchain.configuration.BlockchainConfigurationProperties
import io.builders.demo.blockchain.domain.custody.CustodyKey
import io.builders.demo.blockchain.domain.custody.CustodyKeyRepository
import io.builders.demo.blockchain.domain.custody.CustodyProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys

@Component
class CustodyKeyGenerator {

    private static final int RADIX_HEX = 16

    @Autowired
    private CustodyKeyRepository repository

    @Autowired
    private BlockchainConfigurationProperties blockchainProps

    String generate(CustodyProvider provider = blockchainProps.defaultProvider) {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair()

        String privateKey = ecKeyPair.privateKey.toString(RADIX_HEX)
        String publicKey = ecKeyPair.publicKey.toString(RADIX_HEX)

        Credentials credentials = Credentials.create(ecKeyPair)
        String address = credentials.address

        repository.save(
                new CustodyKey(
                        privateKey: privateKey,
                        publicKey: publicKey,
                        address: address,
                        custodyProvider: provider
                )
        )

        return address
    }

}
