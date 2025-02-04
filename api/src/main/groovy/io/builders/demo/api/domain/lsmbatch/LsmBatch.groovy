package io.builders.demo.api.domain.lsmbatch

import groovy.transform.EqualsAndHashCode
import io.builders.demo.api.domain.settlement.Settlement
import io.builders.demo.core.domain.BaseEntity
import jakarta.persistence.*

import java.time.OffsetDateTime

@Entity
@Table
@EqualsAndHashCode
class LsmBatch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @Column
    OffsetDateTime executionDate

    @OneToMany(mappedBy = "lsmBatch", cascade = CascadeType.ALL)
    List<Settlement> settlements

    @Column
    String aiResult

}
