package dev.xpr3ss0.scientificplot.model

data class DataSeries (
    val xValues: List<Double>,
    val yValues: List<Double>
) {
    fun isEmpty(): Boolean {
        return (xValues.isEmpty() && yValues.isEmpty())
    }
    fun isFull(): Boolean {
        return (!xValues.isEmpty() && !yValues.isEmpty())
    }
    fun isSymmetric(): Boolean {
        return (xValues.size == yValues.size)
    }

    private fun getFloatRange(values: List<Double>) : ClosedFloatingPointRange<Float> {
        val minVal = values.min().toFloat()
        val maxVal = values.max().toFloat()
        return minVal.rangeTo(maxVal)
    }
    fun getFloatRangeX() = getFloatRange(xValues)
    fun getFloatRangeY() = getFloatRange(yValues)
}