package transformer

import TestDefaults.createSampleRawVestEvent
import TestDefaults.createSampleVestEvent
import model.VestType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CsvTransformerTest {

    private val transformer = CsvTransformer()

    @Test
    fun `it should transform raw events successfully for inputs with greater precision than specified precision`() {
        val rawEvents = listOf(
            createSampleRawVestEvent(quantity = "100.4567"),
            createSampleRawVestEvent(quantity = "100.5214")
        )
        val expected = listOf(
            createSampleVestEvent(quantity = 100.45),
            createSampleVestEvent(quantity = 100.52),
        )

        val actual = transformer.transform(rawEvents, 2)

        assertEquals(expected, actual)
    }

    @Test
    fun `it should transform raw events successfully for inputs with less precision than specified precision`() {
        val rawEvents = listOf(
            createSampleRawVestEvent(quantity = "100.4567"),
            createSampleRawVestEvent(quantity = "100.5214")
        )
        val expected = listOf(
            createSampleVestEvent(quantity = 100.4567),
            createSampleVestEvent(quantity = 100.5214),
        )

        val actual = transformer.transform(rawEvents, 6)

        assertEquals(expected, actual)
    }

    @Test
    fun `it should convert quantity to negative if vest type is CANCEL`() {
        val rawEvents = listOf(
            createSampleRawVestEvent(vestType = "CANCEL", quantity = "100"),
        )
        val expected = listOf(
            createSampleVestEvent(vestType = VestType.CANCEL, quantity = -100.0),
        )

        val actual = transformer.transform(rawEvents, 6)

        assertEquals(expected, actual)
    }

    @Test
    fun `it should throw IllegalArgumentException if quantity exception is less than 0 or greater than 6`() {
        val rawEvents = listOf(
            createSampleRawVestEvent(quantity = "100.4567"),
            createSampleRawVestEvent(quantity = "100.5214")
        )

        assertThrows<IllegalArgumentException> {
            transformer.transform(rawEvents, -1)
        }
        assertThrows<IllegalArgumentException> {
            transformer.transform(rawEvents, 7)
        }
    }
}
