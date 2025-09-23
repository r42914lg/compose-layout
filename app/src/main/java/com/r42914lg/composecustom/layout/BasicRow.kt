package com.r42914lg.composecustom.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MyBasicRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            var x = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.width
            }
        }
    }
}

@Composable
fun CallingComposableRow(modifier: Modifier = Modifier) {
    MyBasicRow(modifier.padding(8.dp)) {
        Text("MyBasicColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}

@Preview
@Composable
fun CallingComposableRowPreview() {
    CallingComposableRow()
}