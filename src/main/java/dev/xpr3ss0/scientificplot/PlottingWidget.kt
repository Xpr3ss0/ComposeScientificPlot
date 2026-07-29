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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import dev.xpr3ss0.scientificplot.model.BoundingBox
import dev.xpr3ss0.scientificplot.model.CoordinateGrid
import dev.xpr3ss0.scientificplot.model.CoordinateLabels
import dev.xpr3ss0.scientificplot.state.InternalPlotState
import dev.xpr3ss0.scientificplot.state.PlotManager
import dev.xpr3ss0.scientificplot.transforms.CoordinateTransform

@Composable
fun ScientificPlot(
    plotManager: PlotManager,
    modifier: Modifier = Modifier
) {

    val textMeasurer = rememberTextMeasurer()
    Box (modifier.testTag("ScientificPlotTest")){
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(10.dp)
        ) {
            val plotState = plotManager.plotState

            val boundingBox = BoundingBox.fromCenterSize(
                center = Offset(size.width / 2, size.height / 2),
                size = Size(
                    size.width * plotState.boundingBoxRatio,
                    size.height * plotState.boundingBoxRatio
                )
            )

            val coordinateTransform : CoordinateTransform = plotState.dataScale.getTransform(
                plotRange = plotState.plotRange,
                boundingBox = boundingBox
            )

            val grid = CoordinateGrid.linearFromPlotRange(plotState.plotRange, xNum = 5, yNum = 4)

            val labels = CoordinateLabels.linearFromCoordinateGrid(grid)

            val internalState = InternalPlotState(
                transform = coordinateTransform,
                plotRange = plotState.plotRange,
                boundingBox = boundingBox,
                coordinateGrid = grid,
                coordinateLabels = labels,
                dataSeries = plotState.dataSeries
            )


            drawFrame(this, internalState)

            drawGrid(this, internalState)

            drawLabels(this, internalState, textMeasurer)

            if (plotState.dataSeries.isFull() && plotState.dataSeries.isSymmetric()) {
                drawSeries(this, internalState)
            }
        }


    }
}

fun drawSeries(drawScope: DrawScope, state: InternalPlotState) {


    val coordinateTransform = state.transform
    val boundingBox = state.boundingBox

    val path = Path()

    // iterate over array, using first in-range value as line start
    var startIndex = 0
    var startPoint: Offset
    do {
        startPoint = coordinateTransform.dataToScreen(Offset(
            state.dataSeries.xValues[startIndex].toFloat(),
            state.dataSeries.yValues[startIndex].toFloat()
        ))
        startIndex++
        if (startIndex >= state.dataSeries.xValues.size) {
            // reached end of array
            return
        }
    } while (!boundingBox.contains(startPoint))

    path.moveTo(startPoint.x, startPoint.y)

    for (i in startIndex until state.dataSeries.xValues.size) {

        val nextPoint = coordinateTransform.dataToScreen(Offset(
            state.dataSeries.xValues[i].toFloat(),
            state.dataSeries.yValues[i].toFloat()
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

fun drawFrame(drawScope: DrawScope, state: InternalPlotState) {
    val path = Path()

    val box = state.boundingBox
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

fun drawGrid(drawScope: DrawScope, state: InternalPlotState) {
    val path = Path()
    val box = state.boundingBox
    val grid = state.coordinateGrid
    val transform = state.transform

    for (x in grid.xCoordinates) {
        val xScreen = transform.dataToScreen(Offset(x, 0F)).x
        path.moveTo(xScreen, box.bottom)
        path.lineTo(xScreen, box.top)
    }

    for (y in grid.yCoordinates) {
        val yScreen = transform.dataToScreen(Offset(0F, y)).y
        path.moveTo(box.left, yScreen)
        path.lineTo(box.right, yScreen)
    }

    drawScope.drawPath(
        path,
        color = Color.Gray,
        style = Stroke(width = 4f)
    )
}

fun drawLabels(drawScope: DrawScope, state: InternalPlotState, textMeasurer: TextMeasurer) {
    val labelStyle = TextStyle(
        color = Color.Black,
        fontSize = 12.sp
    )

    for (i in state.coordinateGrid.xCoordinates.indices) {
        val labelText = state.coordinateLabels.xLabels.labels[i]
        val x = state.transform.dataToScreen(
            Offset(state.coordinateGrid.xCoordinates[i],0F)
        ).x
        val y =  state.boundingBox.bottom

        val textLayoutResult = textMeasurer.measure(labelText, style = labelStyle)

        drawScope.drawText(
            textMeasurer = textMeasurer,
            text = labelText,
            style = labelStyle,
            topLeft = Offset(
                x = x - textLayoutResult.size.width / 2F * 1.2F,
                y = y
            )
        )

    }

    for (i in state.coordinateGrid.yCoordinates.indices) {
        val labelText = state.coordinateLabels.yLabels.labels[i]
        val y = state.transform.dataToScreen(
            Offset(x = 0F, y = state.coordinateGrid.yCoordinates[i])
        ).y
        val x =  state.boundingBox.left

        val textLayoutResult = textMeasurer.measure(labelText, style = labelStyle)

        drawScope.drawText(
            textMeasurer = textMeasurer,
            text = labelText,
            style = labelStyle,
            topLeft = Offset(
                x = x - textLayoutResult.size.width * 1.2F,
                y = y
            )
        )

    }

}