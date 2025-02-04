package io.builders.demo.blockchain.domain.custody

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustodyKeyRepository extends JpaRepository<CustodyKey, UUID> {

    Optional<CustodyKey> findByAddress(String address)

}
