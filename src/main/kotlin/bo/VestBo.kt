package bo

import model.EnrichedVestEvent
import model.VestEvent
import org.apache.commons.io.FileUtils
import parser.CsvParser
import transformer.CsvTransformer
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDate

/**
 * This class will house all the logic for performing business operations on vesting events
 */
class VestBo(
    private val parser: CsvParser = CsvParser(),
    private val transformer: CsvTransformer = CsvTransformer(),
) {

    /**
     * Given a CSV file, extract the contents, and generate the enriched vested events
     */
    fun getEnrichedVestedEventsFromCsv(
        csvFile: File,
        targetDate: LocalDate,
        quantityPrecision: Int = 0
    ): List<EnrichedVestEvent> {
        val lines = FileUtils.readLines(csvFile, Charset.defaultCharset())
        val rawEvents = parser.parse(lines)

        val events = transformer.transform(rawEvents, quantityPrecision)

        return enrichVestedEvents(events, targetDate, quantityPrecision)
    }

    /**
     * This method will process the VestEvents, sum the quantities up grouped by employeeId, employeeName, and awardId,
     * and return a list of EnrichedVestEvents
     */
    private fun enrichVestedEvents(
        events: List<VestEvent>,
        targetDate: LocalDate,
        quantityPrecision: Int
    ): List<EnrichedVestEvent> {
        return events.filter { it.awardDate.isEqual(targetDate) || it.awardDate.isBefore(targetDate) }
            .groupBy { Triple(it.employeeId, it.employeeName, it.awardId) }
            .map { (key, groupedEvents) ->
                val quantitySum = groupedEvents.sumOf { it.quantity }

                EnrichedVestEvent(
                    employeeId = key.first,
                    employeeName = key.second,
                    awardId = key.third,
                    awardQuantity = String.format("%.${quantityPrecision}f", quantitySum)
                )
            }
    }
}
