package io.builders.demo.dtcc.domain.settlement

import io.builders.demo.core.domain.BaseEntity
import io.builders.demo.dtcc.domain.lsmbatch.LsmBatch
import io.builders.demo.dtcc.domain.user.User
import jakarta.persistence.*

import java.time.OffsetDateTime

import groovy.transform.EqualsAndHashCode

@Entity
@Table
@EqualsAndHashCode
class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    User buyer

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller

    @Column(nullable = false)
    BigDecimal cashAmount

    @Column(nullable = false)
    BigDecimal securityAmount

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    SettlementStatus status = SettlementStatus.PENDING

    @Column
    OffsetDateTime executionDate

    @ManyToOne
    @JoinColumn(name = 'lsm_batch_id', nullable = false)
    LsmBatch lsmBatch

}
