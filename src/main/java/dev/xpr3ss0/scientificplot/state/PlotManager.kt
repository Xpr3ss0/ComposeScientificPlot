package dev.xpr3ss0.scientificplot.state

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class PlotManager(initialState: PlotState) {
    var plotState by mutableStateOf<PlotState>(initialState)
    //    private set
}