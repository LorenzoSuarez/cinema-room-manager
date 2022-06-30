package cinema

import kotlin.properties.Delegates

const val S = 'S'
const val B = 'B'
const val SPACE = " "
private val cinema = mutableListOf<MutableList<Char>>()
private val currentIncome = mutableListOf<Int>()
private var input by Delegates.notNull<Int>()
private val seating = mutableListOf<Pair<Int, Int>>()

fun main() {
    val rows = print("Enter the number of rows: ").run { readLine()!!.toInt() }
    val seatsInRow = print("Enter the number of seats in each row: ").run { readLine()!!.toInt() }

    repeat(rows) {
        cinema.add(List(seatsInRow) { S }.toMutableList())
    }

    do {
        input = print("1. Show the seats\n2. Buy a ticket\n3. Statistics\n0. Exit\n")
            .run { readLine()!!.toInt() }

        when (input) {
            1 -> {
                printHead(seatsInRow)
                printCinema()
            }

            2 -> {
                do {
                    val rowsNumber = print("Enter a row number: ").run { readLine()!!.toInt() }
                    val seatsInRowNumber =
                        print("Enter a seat number in that row: ").run { readLine()!!.toInt() }

                    val isAlreadyPurchased = seating.indexOf(rowsNumber to seatsInRowNumber) != -1
                    val isOutOfBounds =
                        rowsNumber !in 1..cinema.size || seatsInRowNumber !in 1..cinema.first().size

                    when {
                        isAlreadyPurchased -> println("That ticket has already been purchased!")
                        isOutOfBounds -> println("Wrong input!")
                        else -> {
                            seating.add(rowsNumber to seatsInRowNumber)
                            val price = if (rows * seatsInRow < 60) 10 else {
                                if (rowsNumber <= (rows / 2)) 10 else 8
                            }
                            currentIncome.add(price)
                            println()
                            println("Ticket price: $$price")
                        }
                    }

                } while (isAlreadyPurchased || isOutOfBounds)

            }

            3 -> {
                println("Number of purchased tickets: ${seating.size}")
                println("Percentage: ${calculatePercentage()}%")
                println("Current income: $${currentIncome.sum()}")
                println("Total income: $${calculateTotalIncome(rows, seatsInRow)}")

                println()
            }
        }

    } while (input != 0)

}

private fun calculateTotalIncome(rows: Int, seatsInRow: Int): Int =
    if (rows * seatsInRow < 60) rows * seatsInRow * 10
    else {
        ((rows / 2) * seatsInRow * 10) + ((((rows / 2) + rows % 2) * seatsInRow) * 8)
    }

private fun printHead(seatsInRow: Int) {
    println()
    println(
        "Cinema:\n  ${(1..seatsInRow).toList().joinToString(separator = SPACE)}"
    )
}

private fun calculatePercentage(): String =
    "%.2f".format(seating.size * 100 / (cinema.size * cinema.first().size).toDouble())

private fun printCinema() {
    cinema.forEachIndexed { index, s ->
        seating.filter { it.first == index.inc() }.map {
            s[it.second.dec()] = B
        }
        println(s.joinToString(separator = SPACE, prefix = "${index.inc()} "))
    }
    println()
}
