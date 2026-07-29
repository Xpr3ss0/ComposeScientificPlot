package dev.xpr3ss0.scientificplot.transforms

import androidx.annotation.Size
import androidx.compose.ui.geometry.Offset
import dev.xpr3ss0.scientificplot.model.BoundingBox
import dev.xpr3ss0.scientificplot.model.PlotRange

class LinearTransform(
    val plotRange: PlotRange,
    val boundingBox: BoundingBox
) : CoordinateTransform {

    /*
    Linear map, mapping the 2d region specified by plotRange onto the 2d region specified by boundingBox
     */

    override fun dataToScreen(point: Offset): Offset {
        val xRangeSpan = plotRange.xMax - plotRange.xMin
        val yRangeSpan = plotRange.yMax - plotRange.yMin

        // screen coordinates relative to top left of bounding box
        val xRel = (point.x - plotRange.xMin) / xRangeSpan * boundingBox.size.width
        val yRel = (1.0F - (point.y - plotRange.yMin) / yRangeSpan) * boundingBox.size.height

        // point relative to top left of canvas
        return Offset(xRel, yRel) + boundingBox.topLeft
    }

    override fun screenToData(point: Offset): Offset {
        val xRangeSpan = plotRange.xMax - plotRange.xMin
        val yRangeSpan = plotRange.yMax - plotRange.yMin

        // point relative to the top left bounding box in data coordinates
        val pointRel = point - boundingBox.topLeft

        // corresponding data coordinates
        val x = pointRel.x / boundingBox.size.width * xRangeSpan + plotRange.xMin
        val y = plotRange.yMax - pointRel.y / boundingBox.size.height * yRangeSpan
        return Offset(x, y)
    }
}

class LinearScale() : DataScale {
    override fun getTransform(plotRange: PlotRange, boundingBox: BoundingBox): CoordinateTransform {
        return LinearTransform(plotRange, boundingBox)
    }
}