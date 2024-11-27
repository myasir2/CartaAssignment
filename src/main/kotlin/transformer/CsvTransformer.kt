package transformer

import model.RawVestEvent
import model.VestEvent
import model.VestType
import util.enumValueOrDefault
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class CsvTransformer: Transformer {

    override fun transform(rawEvents: List<RawVestEvent>, quantityPrecision: Int): List<VestEvent> {
        require(quantityPrecision in MIN_QUANTITY_PRECISION..MAX_QUANTITY_PRECISION) {
            "quantityPrecision must be between 0 and 6"
        }

        return rawEvents.map {
            val type = enumValueOrDefault(it.type, VestType.UNKNOWN)
            val date = LocalDate.parse(it.awardDate)
            val quantity = BigDecimal(it.quantity).setScale(quantityPrecision, RoundingMode.DOWN).toDouble()

            VestEvent(
                type = type,
                awardDate = date,
                employeeId = it.employeeId,
                employeeName = it.employeeName,
                awardId = it.awardId,
                quantity = if(type == VestType.CANCEL) (quantity * -1) else quantity,
            )
        }
    }

    private companion object {
        const val MIN_QUANTITY_PRECISION = 0
        const val MAX_QUANTITY_PRECISION = 6
    }
}
