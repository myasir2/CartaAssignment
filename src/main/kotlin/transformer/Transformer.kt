package transformer

import model.RawVestEvent
import model.VestEvent

/**
 * An interface to extend different type of transformers from raw vest event to model object
 */
interface Transformer {

    /**
     * Convert raw events to model objects for processing
     */
    fun transform(rawEvents: List<RawVestEvent>, quantityPrecision: Int): List<VestEvent>
}
