package dev.xpr3ss0.scientificplot.model

import kotlin.math.abs
import kotlin.math.pow

data class Labels(
    val labels: List<String>,
    val commonExponent: Int? = null,
    val commonAddition: Float? = null
)

data class CoordinateLabels(
    val xLabels: Labels,
    val yLabels: Labels
) {
    companion object {
        fun linearFromCoordinateGrid(grid: CoordinateGrid, exponentLimit: Int = 3, lengthLimit: Int = 5) =
            CoordinateLabels(
                xLabels = linearLabels(grid.xCoordinates, exponentLimit, lengthLimit),
                yLabels = linearLabels(grid.yCoordinates, exponentLimit, lengthLimit)
            )

        private fun linearLabels(values: List<Float>, exponentLimit: Int, lengthLimit: Int): Labels {
            var finalValues = values
            var commonExponent: Int? = null
            var commonAddition: Float? = null

            if (values.maxOf { x -> abs(x.toScientificNumber().exponent) } > exponentLimit) {
                commonExponent = values.maxOf { x -> abs(x.toScientificNumber().exponent) }
                finalValues = values.map { x -> x / 10F.pow(commonExponent) }
            }

            if (finalValues.maxOf { x -> x.toString().length } > lengthLimit) {
                commonAddition = finalValues.min()
                finalValues = finalValues.map { x -> x - commonAddition }
            }

            val labels = finalValues.map { x -> x.toString() }
            return Labels(labels, commonExponent, commonAddition)
        }
    }
}