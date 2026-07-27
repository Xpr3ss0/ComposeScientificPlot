package dev.xpr3ss0.scientificplot.model

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

data class CoordinateGrid(
    val xCoordinates: List<Float>,
    val yCoordinates: List<Float>,
    val xSpacing: Float,
    val ySpacing: Float
) {
    companion object {
        fun fromPlotRange(plotRange: PlotRange, xNum: Int, yNum: Int) {

        }

        private fun niceIntervals(minVal: Float, maxVal: Float, intervalNumTarget: Int = 5, niceVals: List<Int> = listOf(1,2,5)) {
            val spacingTarget = (maxVal - minVal) / intervalNumTarget
            val exponentTarget = log10(spacingTarget)
            val exponentTargetFloor = floor(exponentTarget).toInt()
            val spacingTargetMantissa = spacingTarget / 10.0F.pow(exponentTargetFloor)
            val candidateDistance = List<Float>(niceVals.size) { i ->
                abs(spacingTargetMantissa - niceVals[i])
            }

        }
    }
}