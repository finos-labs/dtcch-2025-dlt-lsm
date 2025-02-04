package io.builders.demo.dtcc.domain.user

import io.builders.demo.core.domain.BaseEntity
import jakarta.persistence.*

import groovy.transform.EqualsAndHashCode

@Entity
@Table(name = 'user')
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
