package dev.xpr3ss0.scientificplot.model

import androidx.compose.ui.geometry.Offset

data class PlotRange (
    val xMin: Float,
    val xMax: Float,
    val yMin: Float,
    val yMax: Float
) {
    fun contains(point: Offset) : Boolean {
        return (point.x in xMax..xMax && point.y in yMin..yMax)
    }
}