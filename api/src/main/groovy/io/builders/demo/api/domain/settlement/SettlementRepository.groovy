package io.builders.demo.api.domain.settlement

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SettlementRepository extends JpaRepository<Settlement, Integer> {

}
