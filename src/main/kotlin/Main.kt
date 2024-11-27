import bo.VestBo
import java.io.File
import java.time.LocalDate
import kotlin.system.exitProcess

data class Arguments(
    val fileName: String,
    val targetDate: LocalDate,
    val quantityPrecision: Int,
)

fun main(args: Array<String>) {
    val arguments = parseArgs(args)
    val vestBo = VestBo()
    val enrichedEvents = vestBo.getEnrichedVestedEventsFromCsv(
        File(arguments.fileName),
        arguments.targetDate,
        arguments.quantityPrecision
    )

    enrichedEvents.forEach { event ->
        with(event) {
            println("$employeeId, $employeeName, $awardId, $awardQuantity")
        }
    }
}

fun parseArgs(args: Array<String>): Arguments {
    if (args.size < 2) {
        println("Please specify a file name and target date")

        exitProcess(1)
    }

    val fileName = args[0]
    val targetDate = LocalDate.parse(args[1])
    val quantityPrecision = if (args.size > 2) args[2].toInt() else 0

    return Arguments(fileName, targetDate, quantityPrecision)
}
