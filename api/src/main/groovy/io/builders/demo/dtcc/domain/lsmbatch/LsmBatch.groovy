package io.builders.demo.dtcc.domain.lsmbatch

import io.builders.demo.core.domain.BaseEntity
import io.builders.demo.dtcc.domain.settlement.Settlement
import jakarta.persistence.*

import java.time.OffsetDateTime

import groovy.transform.EqualsAndHashCode

@Entity
@Table
@EqualsAndHashCode
class LsmBatch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @Column
    OffsetDateTime executionDate

    @OneToMany(mappedBy = 'lsmBatch', orphanRemoval = true, cascade = CascadeType.ALL)
    List<Settlement> settlements = []

    @Column
    String aiResult

    void addSettlement(Settlement settlement) {
        settlement.lsmBatch(this)
        settlements.add(settlement)
    }

}
