package ru.sber.rdbms

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TransferPessimisticLockTest {

    @Test
    fun transfer() {
        TransferPessimisticLock().transfer(1, 2, 0)
    }
}