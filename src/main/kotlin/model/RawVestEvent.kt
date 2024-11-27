package model

data class RawVestEvent(
    val type: String,
    val employeeId: String,
    val employeeName: String,
    val awardId: String,
    val awardDate: String,
    val awardQuantity: String
)
