package dev.xpr3ss0.scientificplot.state

import dev.xpr3ss0.scientificplot.model.BoundingBox
import dev.xpr3ss0.scientificplot.model.CoordinateGrid
import dev.xpr3ss0.scientificplot.model.CoordinateLabels
import dev.xpr3ss0.scientificplot.model.DataSeries
import dev.xpr3ss0.scientificplot.model.PlotRange
import dev.xpr3ss0.scientificplot.model.SeriesPlot
import dev.xpr3ss0.scientificplot.transforms.CoordinateTransform
import dev.xpr3ss0.scientificplot.transforms.DataScale
import dev.xpr3ss0.scientificplot.transforms.LinearScale

data class PlotState(
    /*
    The externally available state of the plot.
     */
    val plotEntities: List<SeriesPlot>,
    val dataScale: DataScale,
    val plotRange: PlotRange,
    val boundingBoxRatio: Float
) {
    companion object {
        fun defaultFromEmpty() : PlotState {
            val plotEntities = emptyList<SeriesPlot>()
            val dataScale = LinearScale()
            val plotRange = PlotRange(-1F, 1F, -1F, 1F)
            val boundingBoxRatio = 0.8F
            return PlotState(plotEntities, dataScale, plotRange, boundingBoxRatio)
        }
        fun defaultFromData(dataSeries: DataSeries, name: String = "line plot"): PlotState {
            val plotRange = PlotRange(
                dataSeries.xValues.min().toFloat(),
                dataSeries.xValues.max().toFloat(),
                dataSeries.yValues.min().toFloat(),
                dataSeries.yValues.max().toFloat()
            )
            val seriesPlot = SeriesPlot.linePlot(dataSeries, name)
            return defaultFromEmpty().copy(plotEntities = listOf<SeriesPlot>(seriesPlot), plotRange = plotRange)
        }
    }
}

data class InternalPlotState(
    /*
    The internally used plot state.
    During composition of the plot, it is inferred from the externally available plot state,
    composition-time attributes of the composable, as well as the remembered plot state.
     */
    val coordinateGrid: CoordinateGrid,
    val boundingBox: BoundingBox,
    val transform: CoordinateTransform,
    val plotRange: PlotRange,
    val coordinateLabels: CoordinateLabels,
    val plotEntities: List<SeriesPlot>
)