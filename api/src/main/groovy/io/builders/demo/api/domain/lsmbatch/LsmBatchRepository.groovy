package io.builders.demo.api.domain.lsmbatch

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LsmBatchRepository extends JpaRepository<LsmBatch, Integer> {

}
