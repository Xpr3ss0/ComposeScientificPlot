package dev.xpr3ss0.scientificplot

import android.R
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.platform.testTag
import dev.xpr3ss0.scientificplot.model.BoundingBox
import dev.xpr3ss0.scientificplot.model.DataSeries
import dev.xpr3ss0.scientificplot.model.PlotRange
import dev.xpr3ss0.scientificplot.model.PlotState
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

            val boundingBoxRatio = 0.8F
            val boundingBox = BoundingBox.fromCenterSize(
                center = Offset(size.width / 2, size.height / 2),
                size = Size(
                    size.width * boundingBoxRatio,
                    size.height * boundingBoxRatio
                )
            )

            val coordinateTransform : CoordinateTransform = LinearTransform(
                plotRange = plotRange,
                boundingBox = boundingBox
            )

            val plotState = PlotState(
                transform = coordinateTransform,
                plotRange = plotRange,
                boundingBox = boundingBox
            )

            drawSeries(this, dataSeries, plotState)

            drawFrame(this, plotState)
        }
    }
}

fun drawSeries(drawScope: DrawScope,
               dataSeries: DataSeries,
               plotState: PlotState) {


    val coordinateTransform = plotState.transform
    val boundingBox = plotState.boundingBox

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
            return
        }
    } while (!boundingBox.contains(startPoint))

    path.moveTo(startPoint.x, startPoint.y)

    for (i in startIndex until dataSeries.xValues.size) {

        val nextPoint = coordinateTransform.dataToScreen(Offset(
            dataSeries.xValues[i].toFloat(),
            dataSeries.yValues[i].toFloat()
        ))

        if (boundingBox.contains(nextPoint)) {
            path.lineTo(nextPoint.x, nextPoint.y)
        }
    }

    drawScope.drawPath(
        path,
        color = Color.Blue,
        style = Stroke(width = 4f)
    )
}

fun drawFrame(drawScope: DrawScope, plotState: PlotState) {
    val path = Path()

    val box = plotState.boundingBox
    path.moveTo(box.topLeft.x, box.topLeft.y)
    path.lineTo(box.topRight.x, box.topRight.y)
    path.lineTo(box.bottomRight.x, box.bottomRight.y)
    path.lineTo(box.bottomLeft.x, box.bottomLeft.y)
    path.lineTo(box.topLeft.x, box.topLeft.y)

    drawScope.drawPath(
        path = path,
        color = Color.Black,
        style = Stroke(width = 5f)
    )
}