package io.builders.demo.surikata.domain.transaction.service

import io.builders.demo.surikata.domain.transaction.TransactionRepository
import io.builders.demo.surikata.domain.transaction.exception.TransactionAlreadyExistsDomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CheckTransactionAlreadyExistsDomainService {

    @Autowired
    TransactionRepository repository

    void execute(String hash) {
        repository.findByHash(hash).ifPresent {
            throw new TransactionAlreadyExistsDomainException(String.format(
                TransactionAlreadyExistsDomainException.ALREADY_EXISTS_BY_HASH,
                hash
            ))
        }
    }

}
