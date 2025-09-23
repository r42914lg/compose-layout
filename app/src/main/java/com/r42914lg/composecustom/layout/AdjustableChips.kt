package com.r42914lg.composecustom.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

@Composable
fun AdjustableChips(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        fun calculateConstraints(w: Int, factor: Float): Constraints {
            return if (factor <= 1) {
                Constraints(minWidth = w)
            } else {
                Constraints(minWidth = (w * factor).toInt())
            }
        }

        val widths = measurables.map {
            it.minIntrinsicWidth(constraints.maxHeight)
        }

        val widthFactor = constraints.maxWidth.toFloat() / widths.sumOf { it }
        val placeables = measurables.mapIndexed { i, measurable ->
            val fixedWidthConstraints = calculateConstraints(widths[i], widthFactor)
            measurable.measure(fixedWidthConstraints)
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
fun CallingChips(modifier: Modifier = Modifier) {
    AdjustableChips(modifier.padding(8.dp)) {
        Text("1")
        Text("22")
        Text("333")
        Text("4444")
    }
}

@Preview
@Composable
fun CallingChipsPreview() {
    CallingChips()
}