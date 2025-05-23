package io.builders.demo.dtcc.domain.settlement

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SettlementRepository extends JpaRepository<Settlement, Integer> {

    List<Settlement> findAllByStatusIs(SettlementStatus status)
    List<Settlement> findAllByLsmBatchId(Integer batchId)

}
