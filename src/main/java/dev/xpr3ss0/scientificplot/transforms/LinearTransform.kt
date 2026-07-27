package dev.xpr3ss0.scientificplot.transforms

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import dev.xpr3ss0.scientificplot.model.PlotRange

class LinearTransform(
    val plotRange: PlotRange,
    val size: Size
) : CoordinateTransform {
    override fun dataToScreen(point: Offset): Offset {
        val xRangeSpan = plotRange.xMax - plotRange.xMin
        val yRangeSpan = plotRange.yMax - plotRange.yMin
        val x = (point.x - plotRange.xMin) / xRangeSpan * size.width
        val y = (1.0F - (point.y - plotRange.yMin) / yRangeSpan) * size.height
        return Offset(x, y)
    }

    override fun screenToData(point: Offset): Offset {
        val xRangeSpan = plotRange.xMax - plotRange.xMin
        val yRangeSpan = plotRange.yMax - plotRange.yMin
        val x = point.x / size.width * xRangeSpan + plotRange.xMin
        val y = plotRange.yMax - point.y / size.height * yRangeSpan
        return Offset(x, y)
    }
}