package io.builders.demo.api.domain.user

import io.builders.demo.core.domain.BaseEntity
import jakarta.persistence.*

import groovy.transform.EqualsAndHashCode

@Entity
@Table
@EqualsAndHashCode
class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @Column(nullable = false)
    String dltAddress

    @Column(nullable = false, unique = true)
    String alias

}
