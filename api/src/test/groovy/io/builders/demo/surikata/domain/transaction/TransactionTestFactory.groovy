package io.builders.demo.surikata.domain.transaction

import io.builders.demo.core.RandomGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TransactionTestFactory {

    @Autowired
    private static TransactionRepository repository

    TransactionTestFactory(TransactionRepository repository) {
        this.repository = repository
    }

    static Transaction create(
        @DelegatesTo(Transaction) Closure configurator = { }
    ) {
        Transaction transaction = new Transaction().tap(configurator)

        transaction.hash = transaction.hash ?: RandomGenerator.transactionHash()
        transaction.eventName = transaction.eventName ?: RandomGenerator.eventName()
        transaction.contractAddress = transaction.contractAddress ?: RandomGenerator.address()

        return repository.save(transaction)
    }

}
