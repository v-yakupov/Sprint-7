package ru.sber.rdbms

import java.sql.DriverManager
import java.sql.SQLException

class TransferConstraint {
    fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "postgres"
        )

        connection.use {
            try {
                it.prepareStatement("UPDATE account1 SET amount = amount - ? WHERE id = ?;").use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId1)
                    statement.executeUpdate()
                }
                it.prepareStatement("UPDATE account1 SET amount = amount + ? WHERE id = ?;").use { statement ->
                    statement.setLong(1, amount)
                    statement.setLong(2, accountId2)
                    statement.executeUpdate()
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }
}
