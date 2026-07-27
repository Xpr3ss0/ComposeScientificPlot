package dev.xpr3ss0.scientificplot

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import dev.xpr3ss0.scientificplot.model.DataSeries
import dev.xpr3ss0.scientificplot.model.PlotRange
import dev.xpr3ss0.scientificplot.transforms.CoordinateTransform
import dev.xpr3ss0.scientificplot.transforms.LinearTransform

@Composable
fun ScientificPlot(
    dataSeries: DataSeries,
    modifier: Modifier = Modifier
) {
    Box (modifier.testTag("ScientificPlotTest")){
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(10.dp)
        ) {

            if (!dataSeries.isFull() || !dataSeries.isSymmetric()) return@Canvas

            val plotRange = PlotRange(
                xMin = dataSeries.xValues.min().toFloat(),
                xMax = dataSeries.xValues.max().toFloat(),
                yMin = dataSeries.yValues.min().toFloat(),
                yMax = dataSeries.yValues.max().toFloat()
                // yMax = 0.1F
            )

            val coordinateTransform : CoordinateTransform = LinearTransform(
                plotRange = plotRange,
                size = size
            )

            val path = Path()

            // iterate over array, using first in-range value as line start
            var startIndex = 0
            var startPoint: Offset
            do {
                startPoint = coordinateTransform.dataToScreen(Offset(
                    dataSeries.xValues[startIndex].toFloat(),
                    dataSeries.yValues[startIndex].toFloat()
                ))
                startIndex++
                if (startIndex >= dataSeries.xValues.size) {
                    // reached end of array
                    return@Canvas
                }
            } while (!(startPoint.x in 0F..size.width && startPoint.y in 0F..size.height))

            path.moveTo(startPoint.x, startPoint.y)

            if (startIndex >= dataSeries.xValues.size - 1) {
                // no more points to plot
                return@Canvas
            }

            for (i in startIndex until dataSeries.xValues.size) {

                val nextPoint = coordinateTransform.dataToScreen(Offset(
                    dataSeries.xValues[i].toFloat(),
                    dataSeries.yValues[i].toFloat()
                ))

                if (nextPoint.x in 0F..size.width && nextPoint.y in 0F..size.height) {
                    path.lineTo(nextPoint.x, nextPoint.y)
                }
            }

            drawPath(
                path,
                color = Color.Blue,
                style = Stroke(width = 4f)
            )
        }
    }
}