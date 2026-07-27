package dev.xpr3ss0.scientificplot.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class BoundingBox(
    val bottomLeft: Offset,
    val bottomRight: Offset,
    val topLeft: Offset,
    val topRight: Offset,
    val size: Size
) {
    companion object {
        // Factory for TopLeft + BottomRight
        fun fromLTRB(topLeft: Offset, bottomRight: Offset): BoundingBox = BoundingBox(
            topLeft = topLeft,
            topRight = Offset(bottomRight.x, topLeft.y),
            bottomLeft = Offset(topLeft.x, bottomRight.y),
            bottomRight = bottomRight,
            size = Size(
                bottomRight.x - topLeft.x,
                bottomRight.y - topLeft.y
            )
        )

        // Factory for Center + Size
        fun fromCenterSize(center: Offset, size: Size): BoundingBox = BoundingBox(
            size = size,
            topLeft = center + Offset(-size.width / 2, -size.height / 2),
            topRight = center + Offset(size.width / 2, -size.height / 2),
            bottomLeft = center + Offset(-size.width / 2, size.height / 2),
            bottomRight = center + Offset(size.width / 2, size.height / 2)
        )
    }
    fun contains(point: Offset): Boolean {
        val inRangeX = (point.x in topLeft.x..topRight.x)
        val inRangeY = (point.y in topLeft.y..bottomLeft.y)
        return (inRangeX && inRangeY)
    }
}
