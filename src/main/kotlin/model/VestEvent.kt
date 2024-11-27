package model

import java.time.LocalDate

data class VestEvent(
    val type: VestType,
    val employeeId: String,
    val employeeName: String,
    val awardId: String,
    val awardDate: LocalDate,
    val quantity: Double
)
