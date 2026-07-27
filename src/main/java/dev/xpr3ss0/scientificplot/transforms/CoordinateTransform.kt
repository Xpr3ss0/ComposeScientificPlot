package dev.xpr3ss0.scientificplot.transforms

import androidx.compose.ui.geometry.Offset

interface CoordinateTransform {
    fun dataToScreen(point: Offset): Offset
    fun screenToData(point: Offset): Offset
}