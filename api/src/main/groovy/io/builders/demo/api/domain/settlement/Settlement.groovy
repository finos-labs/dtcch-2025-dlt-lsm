package io.builders.demo.api.domain.settlement

import groovy.transform.EqualsAndHashCode
import io.builders.demo.api.domain.lsmbatch.LsmBatch
import io.builders.demo.core.domain.BaseEntity
import jakarta.persistence.*

import java.time.OffsetDateTime

@Entity
@Table
@EqualsAndHashCode
class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @Column(nullable = false)
    Integer buyerId

    @Column(nullable = false)
    Integer sellerId

    @Column(nullable = false)
    BigDecimal cashAmount

    @Column(nullable = false)
    BigDecimal securityAmount

    @Column(nullable = false)
    String status = SettlementStatus.PENDING

    @Column
    OffsetDateTime executionDate

    @ManyToOne
    @JoinColumn(name = "lsm_batch_id", nullable = false)
    LsmBatch lsmBatch

}
