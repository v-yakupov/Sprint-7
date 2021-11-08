package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferPessimisticLock {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "postgres"
        )

        connection.use { connection ->
            try {
                connection.autoCommit = false

                connection.prepareStatement("SELECT id, amount FROM account1 WHERE id IN(?, ?) ORDER BY id FOR UPDATE").use { statement ->
                    statement.setLong(1, accountId1)
                    statement.setLong(2, accountId2)
                    statement.executeQuery().use {
                        it.next()
                        val acc1id = it.getLong("id")
                        val acc1amount = it.getLong("amount")
                        it.next()
                        val acc2id = it.getLong("id")
                        val acc2amount = it.getLong("amount")
                        if (
                            (acc1id == accountId1) && ((acc1amount - amount) < 0) ||
                            (acc2id == accountId1) && ((acc2amount - amount) < 0)
                        ) throw SQLException("Negative balance possible")
                    }
                }

                connection.prepareStatement("UPDATE account1 SET amount = amount - ? WHERE id = ?;").use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.executeUpdate()
                }
                connection.prepareStatement("UPDATE account1 SET amount = amount + ? WHERE id = ?;").use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.executeUpdate()
                }

                connection.commit()
            } catch (e: SQLException) {
                connection.rollback()
                e.printStackTrace()
            } finally {
                connection.autoCommit = true
            }
        }
    }
}
