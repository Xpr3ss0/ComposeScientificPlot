package dev.xpr3ss0.scientificplot.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import dev.xpr3ss0.scientificplot.model.PlotRange
import dev.xpr3ss0.scientificplot.model.SeriesPlot

class PlotManager(initialState: PlotState) {
    var plotState by mutableStateOf(initialState)
        private set

    fun addPlot(plot : SeriesPlot) {
        plotState = plotState.copy(plotEntities = plotState.plotEntities + plot)
        val updatedEntities = plotState.plotEntities + plot
        updatePlots(updatedEntities)
    }

    fun setPlot(plot: SeriesPlot) {
        val updatedEntities = listOf(plot)
        updatePlots(updatedEntities)
    }

    // helper functions
    fun updatePlots(updatedEntities: List<SeriesPlot>, relativeRangePadding: Float = 0.1F) {

        if (updatedEntities.isEmpty() || updatedEntities.all { !it.dataSeries.isFull() }) {
            val globalMinX = -1F
            val globalMinY = -1F
            val globalMaxX = 1F
            val globalMaxY = 1F

            plotState = plotState.copy(
                plotRange = PlotRange(
                    xMin = globalMinX,
                    xMax = globalMaxX,
                    yMin = globalMinY,
                    yMax = globalMaxY
                ),
                plotEntities = updatedEntities
            )
            return
        }

        val fullEntities = updatedEntities.filter { plot -> plot.dataSeries.isFull() }

        val globalMinX = fullEntities.minOf { plot -> plot.dataSeries.xValues.min() }.toFloat()
        val globalMinY = fullEntities.minOf { plot -> plot.dataSeries.yValues.min() }.toFloat()
        val globalMaxX = fullEntities.maxOf { plot -> plot.dataSeries.xValues.max() }.toFloat()
        val globalMaxY = fullEntities.maxOf { plot -> plot.dataSeries.yValues.max() }.toFloat()

        val globalRangeX = globalMaxX - globalMinX
        val rangePaddingX = relativeRangePadding * globalRangeX
        val globalRangeY = globalMaxY  - globalMinY
        val rangePaddingY = relativeRangePadding * globalRangeY

        plotState = plotState.copy(
            plotRange = PlotRange(
                xMin = globalMinX - rangePaddingX,
                xMax = globalMaxX + rangePaddingX,
                yMin = globalMinY - rangePaddingY,
                yMax = globalMaxY + rangePaddingY
            ),
            plotEntities = updatedEntities
        )
    }

    fun setRange(xMin: Float? = null, xMax: Float? = null, yMin: Float? = null, yMax: Float? = null) {
        val newRange = PlotRange(
            xMin = xMin?: plotState.plotRange.xMin,
            xMax = xMax?: plotState.plotRange.xMax,
            yMin = yMin?: plotState.plotRange.yMin,
            yMax = yMax?: plotState.plotRange.yMax,
        )
        plotState = plotState.copy(plotRange = newRange)
    }
}