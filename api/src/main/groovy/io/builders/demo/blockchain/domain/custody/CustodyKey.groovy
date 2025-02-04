package io.builders.demo.blockchain.domain.custody

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
class CustodyKey extends BaseEntity {

    @Id
    @GeneratedValue
    UUID id

    @Column(nullable = false, length = 256)
    String publicKey

    @Column(nullable = false, unique = true, length = 128)
    String privateKey

    @Column(nullable = false, unique = true, length = 42)
    String address

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CustodyProvider custodyProvider

}
