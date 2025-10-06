package com.r42914lg.composecustom.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize

@Composable
fun MaxChildWidthColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        var layoutPass = 0
        var placeables: List<Placeable> = subcompose(layoutPass++, content).map {
            it.measure(constraints)
        }
        val columnSize =
            placeables.fold(IntSize.Zero) { currentMax: IntSize, placeable: Placeable ->
                IntSize(
                    width = maxOf(currentMax.width, placeable.width),
                    height = currentMax.height + placeable.height
                )
            }
        if (placeables.isNotEmpty() && placeables.size > 1) {
            placeables = subcompose(layoutPass, content).map { measurable: Measurable ->
                measurable.measure(Constraints(columnSize.width, constraints.maxWidth))
            }
        }
        layout(columnSize.width, columnSize.height) {
            var yPos = 0
            placeables.forEach { placeable: Placeable ->
                placeable.placeRelative(0, yPos)
                yPos += placeable.height
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOne() {
    Column {
        MaxChildWidthColumn {
            Text(modifier = Modifier.background(Color.Cyan), text = "sdfdfs")
            Text(modifier = Modifier.background(Color.Green), text = "sdfdfssdfsdfsdfsdfdsfsdf")
        }
    }
}