package com.r42914lg.composecustom.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset

class TopLeftPadding(var padding: Dp) : LayoutModifierNode, Modifier.Node() {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val paddingPx = padding.roundToPx()
        val placeable = measurable.measure(constraints.offset(vertical = -paddingPx, horizontal = -paddingPx))
        return layout(placeable.width + paddingPx, placeable.height + paddingPx) {
            placeable.placeRelative(paddingPx, paddingPx)
        }
    }
}
data class VerticalPaddingElement(val padding: Dp) : ModifierNodeElement<TopLeftPadding>() {
    override fun create() = TopLeftPadding(padding)

    override fun update(node: TopLeftPadding) {
        node.padding = padding
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "topLeftPadding"
        properties["padding"] = padding
    }
}
fun Modifier.topLeftPadding(padding: Dp) = this then VerticalPaddingElement(padding)

@Preview
@Composable
fun PreviewTopLeft() {
    Box(Modifier.background(Color.Gray).topLeftPadding(50.dp)) {
        Text("SAMPLE", modifier = Modifier.background(Color.White))
    }
}