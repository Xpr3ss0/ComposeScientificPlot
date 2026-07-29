package dev.xpr3ss0.scientificplot.model

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

data class ScientificNumber(
    val exponent: Int,
    val mantissa: Float
) {
    override fun toString(): String {
        return "$mantissa e+$exponent"
    }
    fun toFloat(): Float {
        return mantissa * 10.0.pow(exponent).toFloat()
    }
}

fun Float.toScientificNumber(): ScientificNumber {
    val exponentExact = log10(this)
    val exponentFloor = floor(exponentExact).toInt()
    val mantissa = this / 10.0F.pow(exponentFloor)
    return ScientificNumber(exponentFloor, mantissa)
}

data class CoordinateGrid(
    val xCoordinates: List<Float>,
    val yCoordinates: List<Float>
) {
    companion object {
        fun linearFromPlotRange(plotRange: PlotRange, xNum: Int, yNum: Int) : CoordinateGrid {
            val xSpacing = niceIntervals(plotRange.xMin, plotRange.xMax, xNum)
            val ySpacing = niceIntervals(plotRange.yMin, plotRange.yMax, yNum)
            val xCoordsStart = plotRange.xMin.closestIntegerMultiple(
                xSpacing.toFloat(),
                RoundDirection.UP)
            val xCoordsEnd = plotRange.xMax.closestIntegerMultiple(
                xSpacing.toFloat(),
                RoundDirection.DOWN
            )
            val xNumActual = ((xCoordsEnd - xCoordsStart) / xSpacing.toFloat()).toInt() + 1
            val yCoordsStart = plotRange.yMin.closestIntegerMultiple(
                ySpacing.toFloat(),
                RoundDirection.UP)
            val yCoordsEnd = plotRange.yMax.closestIntegerMultiple(
                ySpacing.toFloat(),
                RoundDirection.DOWN
            )
            val yNumActual = ((yCoordsEnd - yCoordsStart) / ySpacing.toFloat()).toInt() + 1
            return CoordinateGrid(
                xCoordinates = List<Float>(xNumActual) {
                    i -> xCoordsStart + i * xSpacing.toFloat()
                },
                yCoordinates = List<Float>(yNumActual) {
                        i -> yCoordsStart + i * ySpacing.toFloat()
                }
            )

        }

        private fun niceIntervals(minVal: Float, maxVal: Float, intervalNumTarget: Int = 5, niceVals: List<Int> = listOf(1,2,5)): ScientificNumber {
            val spacingTarget = (maxVal - minVal) / intervalNumTarget
            val spacingTargetScientific = spacingTarget.toScientificNumber()
            val niceMantissaDistances = List<Float>(niceVals.size) { i ->
                abs(spacingTargetScientific.mantissa - niceVals[i])
            }
            val closestIndex = niceMantissaDistances.indexOf(niceMantissaDistances.min())
            return ScientificNumber(spacingTargetScientific.exponent, niceVals[closestIndex].toFloat())

        }

        private fun Float.closestIntegerMultiple(of: Float, direction: Int): Float {
            val ratio = this / of
            return when(direction) {
                RoundDirection.UP -> ceil(ratio) * of
                RoundDirection.DOWN -> floor(ratio) * of
                else -> error("Invalid round direction specified.")
            }
        }

        private object RoundDirection {
            const val UP = 1
            const val DOWN = 2
        }
    }
}