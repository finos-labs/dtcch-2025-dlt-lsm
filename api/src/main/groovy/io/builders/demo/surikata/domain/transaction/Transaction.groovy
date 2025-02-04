package io.builders.demo.surikata.domain.transaction

import io.builders.demo.core.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table
@Entity
class Transaction extends BaseEntity {

    @Id
    @GeneratedValue
    UUID id

    @Column(nullable = false, unique = true, length = 98)
    String hash

    @Column(nullable = false)
    String contractAddress

    @Column(nullable = false)
    String eventName

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    TransactionStatus status = TransactionStatus.PENDING

}
