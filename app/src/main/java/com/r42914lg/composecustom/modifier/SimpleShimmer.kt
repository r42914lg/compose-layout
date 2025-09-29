package com.r42914lg.composecustom.modifier

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize

fun Modifier.shimmer() = composed {
    val size = remember { mutableStateOf(IntSize(0, 0)) }
    val transition = rememberInfiniteTransition()
    val startOffsetX = transition.animateFloat(
        initialValue = -2 * size.value.width.toFloat(),
        targetValue = 2 * size.value.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX.value, 0f),
            end = Offset(startOffsetX.value + size.value.width.toFloat(), size.value.height.toFloat())
        )
    ).onGloballyPositioned {
        size.value = it.size
    }
}

@Preview
@Composable
fun ShimmerPreview() {
    Text(
        modifier = Modifier
            .background(Color.White)
            .shimmer(),
        text = "test-test-test"
    )
}