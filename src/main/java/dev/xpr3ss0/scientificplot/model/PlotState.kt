package dev.xpr3ss0.scientificplot.model

import dev.xpr3ss0.scientificplot.transforms.CoordinateTransform

data class PlotState(
    val transform: CoordinateTransform,
    val plotRange: PlotRange,
    val boundingBox: BoundingBox
)