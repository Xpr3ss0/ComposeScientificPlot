package dev.xpr3ss0.scientificplot.transforms

import androidx.compose.ui.geometry.Offset
import dev.xpr3ss0.scientificplot.model.BoundingBox
import dev.xpr3ss0.scientificplot.model.PlotRange

interface CoordinateTransform {
    fun dataToScreen(point: Offset): Offset
    fun screenToData(point: Offset): Offset
}

interface DataScale {
    fun getTransform(plotRange: PlotRange, boundingBox: BoundingBox) : CoordinateTransform
}