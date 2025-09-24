package com.r42914lg.composecustom.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class Semaphore(
    var color1: Color,
    var color2: Color,
    var color3: Color,
) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        translate(top = - size.minDimension / 3) {
            drawCircle(color1, size.minDimension / 6)
        }
        drawCircle(color2, size.minDimension / 6)
        translate(top = size.minDimension / 3) {
            drawCircle(color3, size.minDimension / 6)
        }
    }
}
data class SemaphoreElement(
    val color1: Color,
    val color2: Color,
    val color3: Color,
) : ModifierNodeElement<Semaphore>() {
    override fun create() = Semaphore(color1, color2, color3)

    override fun update(node: Semaphore) {
        node.color1 = color1
        node.color2 = color2
        node.color2 = color3
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "semaphore"
        properties["color1"] = color1
        properties["color2"] = color2
        properties["color3"] = color3
    }
}
fun Modifier.semaphore(color1: Color, color2: Color, color3: Color) =
    this then SemaphoreElement(color1, color2, color3)

@Preview
@Composable
fun PreviewSemaphore() {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(Color.White)
            .semaphore(Color.Red, Color.Yellow, Color.Green)
    )
}