package com.example.scientificplot

import org.junit.Test
import dev.xpr3ss0.scientificplot.model.CoordinateGrid
import dev.xpr3ss0.scientificplot.model.PlotRange

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoordinateGridTest {
    @Test
    fun setupTestGrid() {
        val plotRange = PlotRange(-20F, 20F, -0.3F, 21.8F)
        val grid = CoordinateGrid.linearFromPlotRange(
            plotRange,
            40,
            6
        )
        println("x-range: (${plotRange.xMin}, ${plotRange.xMax})")
        println("x grid: ${grid.xCoordinates}")
        println("")
        println("y-range: (${plotRange.yMin}, ${plotRange.yMax})")
        println("y grid: ${grid.yCoordinates}")
    }

}