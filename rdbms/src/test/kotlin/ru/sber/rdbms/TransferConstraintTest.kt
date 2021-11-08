package ru.sber.rdbms

import org.junit.jupiter.api.Test

internal class TransferConstraintTest {

    @Test
    fun transfer() {
        TransferConstraint().transfer(1, 2, 0)
    }
}