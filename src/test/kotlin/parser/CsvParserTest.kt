package parser

import TestDefaults.createCsvLine
import TestDefaults.createSampleRawVestEvent
import io.mockk.clearAllMocks
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CsvParserTest {

    private val parser = CsvParser()
    private val lines = listOf(createCsvLine(), createCsvLine())

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `it should successfully parse given CSV file and return list of raw vest events`() {
        val expected = listOf(createSampleRawVestEvent(), createSampleRawVestEvent())

        val actual = parser.parse(lines)

        assertEquals(expected, actual)
    }
}
