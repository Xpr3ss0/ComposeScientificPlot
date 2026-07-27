package dev.Xpr3ss0.scientificplot.ui.components

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
import androidx.compose.ui.platform.testTag

@Composable
fun ScientificPlot(
    xData: DoubleArray?,
    yData: DoubleArray?,
    modifier: Modifier = Modifier
) {
    Box (modifier.testTag("ScientificPlotTest")){
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(10.dp)
        ) {

            if (xData == null || xData.isEmpty()) return@Canvas
            if (yData == null || yData.isEmpty()) return@Canvas

            val xMin = xData.first()
            val xMax = xData.last()

            val yMin = yData.min()
            val yMax = yData.max()

            fun mapX(x: Double): Float =
                ((x - xMin) / (xMax - xMin) * size.width).toFloat()

            fun mapY(y: Double): Float =
                (size.height * (1.0 - (y - yMin) / (yMax - yMin))).toFloat()

            val path = Path()

            path.moveTo(
                mapX(xData[0]),
                mapY(yData[0])
            )

            for (i in 1 until xData.size) {
                path.lineTo(
                    mapX(xData[i]),
                    mapY(yData[i])
                )
            }

            drawPath(
                path,
                color = Color.Blue,
                style = Stroke(width = 4f)
            )
        }
    }
}