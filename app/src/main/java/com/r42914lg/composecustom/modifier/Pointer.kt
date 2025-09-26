package com.r42914lg.composecustom.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize

class OnPointerEventNode(var callback: (PointerEvent) -> Unit) :
    PointerInputModifierNode, Modifier.Node() {
    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize,
    ) {
        if (pass == PointerEventPass.Initial) {
            callback(pointerEvent)
        }
    }

    override fun onCancelPointerInput() {}
}

data class PointerInputElement(val callback: (PointerEvent) -> Unit) :
    ModifierNodeElement<OnPointerEventNode>() {
    override fun create() = OnPointerEventNode(callback)

    override fun update(node: OnPointerEventNode) {
        node.callback = callback
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "onPointerEvent"
        properties["callback"] = callback
    }
}

fun Modifier.onPointerEvent(callback: (PointerEvent) -> Unit) =
    this then PointerInputElement(callback)

@Preview
@Composable
fun OnPointerEventPreview() {
    Box(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxSize(0.5f)
            .onPointerEvent { println("DEBUG >>> ${it.type}") }
    )
}