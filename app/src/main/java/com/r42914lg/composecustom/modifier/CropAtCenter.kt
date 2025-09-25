package com.r42914lg.composecustom.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

class CropAtCenter(private val factor: Float) : LayoutModifierNode, Modifier.Node() {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val adjustedWidth = (constraints.maxWidth * factor).toInt()
        val adjustedHeight = (constraints.maxHeight * factor).toInt()
        val placeable = measurable.measure(
            constraints.copy(
                maxWidth = adjustedWidth,
                maxHeight = adjustedHeight,
                minWidth = adjustedWidth,
                minHeight = adjustedHeight,
            )
        )
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(
                (constraints.maxWidth - adjustedWidth) / 2,
                (constraints.maxHeight - adjustedHeight) / 2
            )
        }
    }
}
data class CropAtCenterElement(val fraction: Float) : ModifierNodeElement<CropAtCenter>() {
    override fun create() = CropAtCenter(fraction)

    override fun update(node: CropAtCenter) {}

    override fun InspectorInfo.inspectableProperties() {
        name = "quarterSize"
        properties["fraction"] = fraction
    }
}

fun Modifier.cropAtCenter(factor: Float = 0.25f) = this then CropAtCenterElement(factor)

@Preview
@Composable
fun PreviewCropAtCenter() {
    Box(Modifier.background(Color.White).size(200.dp)) {
        Text(
            text = "SAMPLE",
            modifier = Modifier.cropAtCenter().background(Color.Blue)
        )
    }
}