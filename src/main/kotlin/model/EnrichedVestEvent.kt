package model

/**
 * The data class to represent the output after transforming and processing the vest event
 */
data class EnrichedVestEvent(
    val employeeId: String,
    val employeeName: String,
    val awardId: String,
    val awardQuantity: String
)
