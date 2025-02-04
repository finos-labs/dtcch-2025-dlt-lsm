package io.builders.demo.surikata.domain.transaction

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByHash(String hash)

}
