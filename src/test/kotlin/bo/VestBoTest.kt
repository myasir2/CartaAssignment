package bo

import TestDefaults.TEST_RAW_CANCEL_VEST_TYPE
import TestDefaults.TEST_RAW_VEST_TYPE
import TestDefaults.createCsvLine
import TestDefaults.createSampleEnrichedVestEvent
import TestDefaults.createSampleRawVestEvent
import TestDefaults.createSampleVestEvent
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import model.VestType
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import parser.CsvParser
import transformer.CsvTransformer
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDate

internal class VestBoTest {

    private val quantityPrecision = 1
    private val parser = mockk<CsvParser>()
    private val transformer = mockk<CsvTransformer>()
    private val csvFile = mockk<File>()
    private val bo = VestBo(parser, transformer)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `it should read from the csv file, and transform the events to enriched events successfully`() {
        val targetDate = LocalDate.of(2024, 2, 1)
        val lines = listOf(
            createCsvLine(employeeId = "emp1", awardId = "award1", awardDate = "2024-01-01", quantity = "500"),
            createCsvLine(employeeId = "emp1", awardId = "award1", awardDate = "2024-02-01", quantity = "500"),
            createCsvLine(employeeId = "emp2", awardId = "award1", awardDate = "2024-02-01", quantity = "1000"),
        )
        val rawEvents = listOf(
            createSampleRawVestEvent(
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-01-01",
                quantity = "500"
            ),
            createSampleRawVestEvent(
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-02-01",
                quantity = "500"
            ),
            createSampleRawVestEvent(
                employeeId = "emp2",
                awardId = "award1",
                awardDate = "2024-02-01",
                quantity = "1000"
            ),
        )
        val events = listOf(
            createSampleVestEvent(
                employeeId = "emp1", awardId = "award1", awardDate = LocalDate.parse("2024-01-01"), quantity = 500.0
            ),
            createSampleVestEvent(
                employeeId = "emp1", awardId = "award1", awardDate = LocalDate.parse("2024-02-01"), quantity = 500.0
            ),
            createSampleVestEvent(
                employeeId = "emp2", awardId = "award1", awardDate = LocalDate.parse("2024-02-01"), quantity = 1000.0
            )
        )
        val expected = listOf(
            createSampleEnrichedVestEvent(employeeId = "emp1", awardId = "award1", quantity = "1000.0"),
            createSampleEnrichedVestEvent(employeeId = "emp2", awardId = "award1", quantity = "1000.0"),
        )

        mockkStatic(FileUtils::class)
        every { FileUtils.readLines(csvFile, Charset.defaultCharset()) } returns lines
        every { parser.parse(lines) } returns rawEvents
        every { transformer.transform(rawEvents, quantityPrecision) } returns events

        val actual = bo.getEnrichedVestedEventsFromCsv(csvFile, targetDate, quantityPrecision)

        assertEquals(expected, actual)
    }

    @Test
    fun `it should read from the csv file, and transform the events to enriched events successfully with cancel vests`() {
        val targetDate = LocalDate.of(2024, 2, 1)
        val lines = listOf(
            createCsvLine(
                vestType = TEST_RAW_VEST_TYPE,
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-01-01",
                quantity = "1000"
            ),
            createCsvLine(
                vestType = TEST_RAW_CANCEL_VEST_TYPE,
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-02-01",
                quantity = "500"
            ),
        )
        val rawEvents = listOf(
            createSampleRawVestEvent(
                vestType = TEST_RAW_VEST_TYPE,
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-01-01",
                quantity = "1000"
            ),
            createSampleRawVestEvent(
                vestType = TEST_RAW_CANCEL_VEST_TYPE,
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-02-01",
                quantity = "500"
            ),
        )
        val events = listOf(
            createSampleVestEvent(
                vestType = VestType.VEST,
                employeeId = "emp1",
                awardId = "award1",
                awardDate = LocalDate.parse("2024-01-01"),
                quantity = 1000.0
            ),
            createSampleVestEvent(
                vestType = VestType.CANCEL,
                employeeId = "emp1",
                awardId = "award1",
                awardDate = LocalDate.parse("2024-02-01"),
                quantity = -500.0
            ),
        )
        val expected = listOf(
            createSampleEnrichedVestEvent(employeeId = "emp1", awardId = "award1", quantity = "500.0"),
        )

        mockkStatic(FileUtils::class)
        every { FileUtils.readLines(csvFile, Charset.defaultCharset()) } returns lines
        every { parser.parse(lines) } returns rawEvents
        every { transformer.transform(rawEvents, quantityPrecision) } returns events

        val actual = bo.getEnrichedVestedEventsFromCsv(csvFile, targetDate, quantityPrecision)

        assertEquals(expected, actual)
    }

    @Test
    fun `it should filter out awards after the target date`() {
        val targetDate = LocalDate.of(2024, 2, 1)
        val lines = listOf(
            createCsvLine(employeeId = "emp1", awardId = "award1", awardDate = "2024-01-01", quantity = "500"),
            createCsvLine(employeeId = "emp1", awardId = "award1", awardDate = "2024-02-02", quantity = "500"),
        )
        val rawEvents = listOf(
            createSampleRawVestEvent(
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-01-01",
                quantity = "500"
            ),
            createSampleRawVestEvent(
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-02-02",
                quantity = "500"
            ),
        )
        val events = listOf(
            createSampleVestEvent(
                employeeId = "emp1", awardId = "award1", awardDate = LocalDate.parse("2024-01-01"), quantity = 500.0
            ),
            createSampleVestEvent(
                employeeId = "emp1", awardId = "award1", awardDate = LocalDate.parse("2024-02-02"), quantity = 500.0
            ),
        )
        val expected = listOf(
            createSampleEnrichedVestEvent(employeeId = "emp1", awardId = "award1", quantity = "500.0"),
        )

        mockkStatic(FileUtils::class)
        every { FileUtils.readLines(csvFile, Charset.defaultCharset()) } returns lines
        every { parser.parse(lines) } returns rawEvents
        every { transformer.transform(rawEvents, quantityPrecision) } returns events

        val actual = bo.getEnrichedVestedEventsFromCsv(csvFile, targetDate, quantityPrecision)

        assertEquals(expected, actual)
    }

    @Test
    fun `it should apply precision correctly`() {
        val precision = 4
        val targetDate = LocalDate.of(2024, 2, 1)
        val lines = listOf(
            createCsvLine(employeeId = "emp1", awardId = "award1", awardDate = "2024-01-01", quantity = "500.1234"),
            createCsvLine(employeeId = "emp2", awardId = "award1", awardDate = "2024-01-02", quantity = "500.1"),
        )
        val rawEvents = listOf(
            createSampleRawVestEvent(
                employeeId = "emp1",
                awardId = "award1",
                awardDate = "2024-01-01",
                quantity = "500.1234"
            ),
            createSampleRawVestEvent(
                employeeId = "emp2",
                awardId = "award1",
                awardDate = "2024-01-02",
                quantity = "500.1"
            ),
        )
        val events = listOf(
            createSampleVestEvent(
                employeeId = "emp1", awardId = "award1", awardDate = LocalDate.parse("2024-01-01"), quantity = 500.1234
            ),
            createSampleVestEvent(
                employeeId = "emp2", awardId = "award1", awardDate = LocalDate.parse("2024-02-01"), quantity = 500.1
            ),
        )
        val expected = listOf(
            createSampleEnrichedVestEvent(employeeId = "emp1", awardId = "award1", quantity = "500.1234"),
            createSampleEnrichedVestEvent(employeeId = "emp2", awardId = "award1", quantity = "500.1000"),
        )

        mockkStatic(FileUtils::class)
        every { FileUtils.readLines(csvFile, Charset.defaultCharset()) } returns lines
        every { parser.parse(lines) } returns rawEvents
        every { transformer.transform(rawEvents, precision) } returns events

        val actual = bo.getEnrichedVestedEventsFromCsv(csvFile, targetDate, precision)

        assertEquals(expected, actual)
    }
}
