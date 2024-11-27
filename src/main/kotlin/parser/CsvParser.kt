package parser

import model.RawVestEvent

/**
 * A class to parse CSV contents containing vesting events
 */
class CsvParser(

    private val delimiter: String = DEFAULT_DELIMITER,
) {

    /**
     * Parse the given CSV contents (lines) and return a list of RawVestEvents
     */
    fun parse(lines: List<String>): List<RawVestEvent> = lines.map(this::parseRawEvent)

    private fun parseRawEvent(line: String): RawVestEvent {
        val values = line.split(delimiter)

        return RawVestEvent(
            type = values[0].trim(),
            employeeId = values[1].trim(),
            employeeName = values[2].trim(),
            awardId = values[3].trim(),
            awardDate = values[4].trim(),
            awardQuantity = values[5].trim(),
        )
    }

    private companion object {
        const val DEFAULT_DELIMITER = ","
    }
}
