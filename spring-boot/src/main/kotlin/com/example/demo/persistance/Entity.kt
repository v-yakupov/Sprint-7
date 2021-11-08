package com.example.demo.persistance

import javax.persistence.*
import javax.persistence.Entity

@Entity
data class Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column
    var name: String?
)