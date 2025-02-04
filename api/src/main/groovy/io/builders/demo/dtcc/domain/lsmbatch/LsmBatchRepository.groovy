package io.builders.demo.dtcc.domain.lsmbatch

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LsmBatchRepository extends JpaRepository<LsmBatch, Integer> {

    @Query('SELECT b FROM LsmBatch b JOIN FETCH b.settlements')
    List<LsmBatch> findAllWithSettlements()

}
