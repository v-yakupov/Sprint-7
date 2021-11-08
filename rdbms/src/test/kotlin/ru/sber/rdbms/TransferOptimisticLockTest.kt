package ru.sber.rdbms

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TransferOptimisticLockTest {

    @Test
    fun transfer() {
        TransferOptimisticLock().transfer(1, 2, 0)
    }
}