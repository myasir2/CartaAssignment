import model.EnrichedVestEvent
import model.RawVestEvent
import model.VestEvent
import model.VestType
import java.time.LocalDate

object TestDefaults {

    const val TEST_RAW_VEST_TYPE = "VEST"
    const val TEST_RAW_CANCEL_VEST_TYPE = "CANCEL"
    const val TEST_EMP_ID = "ISO1234"
    const val TEST_EMP_NAME = "John Smith"
    const val TEST_AWARD_ID = "AWARD_ID"
    const val TEST_RAW_AWARD_DATE = "2024-01-01"
    const val TEST_RAW_QUANTITY = "100"
    const val TEST_QUANTITY = 100.0
    val TEST_TARGET_DATE = LocalDate.of(2024, 1, 1)
    val TEST_VEST_TYPE = VestType.VEST
    val TEST_AWARD_DATE = LocalDate.parse(TEST_RAW_AWARD_DATE)

    fun createCsvLine(
        employeeId: String = TEST_EMP_ID,
        vestType: String = TEST_RAW_VEST_TYPE,
        awardId: String = TEST_AWARD_ID,
        awardDate: String = TEST_RAW_AWARD_DATE,
        quantity: String = TEST_RAW_QUANTITY
    ): String {
        return "$vestType,$employeeId,$TEST_EMP_NAME,$awardId,$awardDate,$quantity"
    }

    fun createSampleRawVestEvent(
        vestType: String = TEST_RAW_VEST_TYPE,
        employeeId: String = TEST_EMP_ID,
        employeeName: String = TEST_EMP_NAME,
        awardId: String = TEST_AWARD_ID,
        awardDate: String = TEST_RAW_AWARD_DATE,
        quantity: String = TEST_RAW_QUANTITY
    ): RawVestEvent {
        return RawVestEvent(
            type = vestType,
            employeeId = employeeId,
            employeeName = employeeName,
            awardId = awardId,
            awardDate = awardDate,
            awardQuantity = quantity
        )
    }

    fun createSampleVestEvent(
        vestType: VestType = TEST_VEST_TYPE,
        employeeId: String = TEST_EMP_ID,
        employeeName: String = TEST_EMP_NAME,
        awardId: String = TEST_AWARD_ID,
        awardDate: LocalDate = TEST_AWARD_DATE,
        quantity: Double = TEST_QUANTITY
    ): VestEvent {
        return VestEvent(
            type = vestType,
            employeeId = employeeId,
            employeeName = employeeName,
            awardId = awardId,
            awardDate = awardDate,
            awardQuantity = quantity
        )
    }

    fun createSampleEnrichedVestEvent(
        employeeId: String = TEST_EMP_ID,
        employeeName: String = TEST_EMP_NAME,
        awardId: String = TEST_AWARD_ID,
        quantity: String = TEST_RAW_QUANTITY
    ): EnrichedVestEvent {
        return EnrichedVestEvent(
            employeeId = employeeId,
            employeeName = employeeName,
            awardId = awardId,
            awardQuantity = quantity
        )
    }
}
