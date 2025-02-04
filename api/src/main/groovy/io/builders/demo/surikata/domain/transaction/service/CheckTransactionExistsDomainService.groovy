package io.builders.demo.surikata.domain.transaction.service

import io.builders.demo.surikata.domain.transaction.Transaction
import io.builders.demo.surikata.domain.transaction.TransactionRepository
import io.builders.demo.surikata.domain.transaction.exception.TransactionNotExistsDomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CheckTransactionExistsDomainService {

    @Autowired
    TransactionRepository repository

    Transaction execute(String transactionHash) {
        repository.findByHash(transactionHash).orElseThrow {
            new TransactionNotExistsDomainException(String.format(
                TransactionNotExistsDomainException.NOT_EXIST_BY_HASH_MESSAGE,
                transactionHash
            ))
        }
    }

    Transaction execute(UUID id) {
        repository.findById(id).orElseThrow {
            new TransactionNotExistsDomainException(String.format(
                TransactionNotExistsDomainException.NOT_EXIST_BY_ID_MESSAGE,
                id
            ))
        }
    }

}
