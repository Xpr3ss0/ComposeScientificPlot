package dev.xpr3ss0.scientificplot.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke

data class PlotStyle(
    val color: Color,
    val lineStyle: Stroke
)

data class SeriesPlot(
    val plotStyle: PlotStyle,
    val dataSeries: DataSeries,
    val name: String
) {
    companion object {

        fun linePlot(dataSeries: DataSeries, name: String, color: Color = Color.Black, lineWidth: Float = 4F) : SeriesPlot {
            val stroke = Stroke(width = lineWidth)
            return SeriesPlot(
                plotStyle = PlotStyle(color, stroke),
                dataSeries = dataSeries,
                name = name
            )
        }

        fun dashedPlot(dataSeries: DataSeries, name: String, color: Color = Color.Black, lineWidth: Float = 4.0F, dashIntervals: List<Float>? = null) : SeriesPlot {
            val dashIntervals = (dashIntervals ?: listOf(10F, 20F)).toFloatArray()
            val dashEffect = PathEffect.dashPathEffect(dashIntervals)
            val stroke = Stroke(width = lineWidth, pathEffect = dashEffect)
            return SeriesPlot(
                plotStyle = PlotStyle(color, stroke),
                dataSeries = dataSeries,
                name = name
            )
        }
    }
}