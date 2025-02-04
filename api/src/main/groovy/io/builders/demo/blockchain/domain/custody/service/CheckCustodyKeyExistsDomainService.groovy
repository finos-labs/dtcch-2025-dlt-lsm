package io.builders.demo.blockchain.domain.custody.service

import io.builders.demo.blockchain.domain.custody.CustodyKey
import io.builders.demo.blockchain.domain.custody.CustodyKeyRepository
import io.builders.demo.blockchain.domain.custody.exception.CustodyKeyNotExistsDomainException
import jakarta.validation.constraints.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CheckCustodyKeyExistsDomainService {

    @Autowired
    private CustodyKeyRepository repository

    CustodyKey executeByAddress(@NotNull String address) {
        repository.findByAddress(address).orElseThrow {
            new CustodyKeyNotExistsDomainException(address)
        }
    }

}
