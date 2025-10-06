package com.r42914lg.composecustom.filters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import kotlinx.coroutines.launch

data class Filter(
    val id: Int = 0,
    val label: String = "",
    val isSelected: Boolean = false,
)

@Composable
fun FilterItem(
    item: Filter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Text(
        modifier = modifier
            .clickable { onClick() }
            .border(width = 1.dp, color =  if (item.isSelected) Color.Cyan else Color.Gray),
        text = item.label,
    )
}

@Composable
fun FiltersBlock(items: List<Filter>) {
    val localItems = remember {
        val list = mutableStateListOf<Filter>()
        items.forEach { list.add(it) }
        list
    }
    val sortedItems by remember {
        derivedStateOf { localItems.sortedWith(compareByDescending { it.isSelected }) }
    }
    LazyRow {
        items(sortedItems, key = {it.id}) { item ->
            FilterItem(
                item = item,
                modifier = Modifier
                    .padding(8.dp)
                    .animateItem(
                        fadeInSpec = tween(durationMillis = 200),
                        fadeOutSpec = tween(durationMillis = 200),
                        placementSpec = tween(durationMillis = 300)
                    )
            ) {
                val index = localItems.indexOfFirst { it.id == item.id }
                if (index >= 0) {
                    localItems[index] = localItems[index].copy(isSelected = !localItems[index].isSelected)
                }
            }
        }
    }
}

@Composable
fun FiltersBlockFlowRow(items: List<Filter>) {
    val localItems = remember {
        val list = mutableStateListOf<Filter>()
        items.forEach { list.add(it) }
        list
    }
    val sortedItems by remember {
        derivedStateOf { localItems.sortedWith(compareByDescending { it.isSelected }) }
    }
    FlowRow(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sortedItems.forEachIndexed { _, filter ->
            key(filter.label + filter.isSelected) {
                AnimatedVisibility(
                    modifier = Modifier.animatePlacement(),
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    FilterItem(
                        modifier = Modifier.padding(8.dp),
                        item = filter,
                    ) {
                        val index = localItems.indexOfFirst { it.id == filter.id }
                        if (index >= 0) {
                            localItems[index] =
                                localItems[index].copy(isSelected = !localItems[index].isSelected)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FiltersBlockPreview() {
    val items = remember {
        val list = mutableStateListOf<Filter>()
        for (i in 0..5) {
            list.add(Filter(i, "Filter $i", isSelected = false))
        }
        list
    }
    Column {
        FiltersBlock(items)
        FiltersBlockFlowRow(items)
    }
}

fun Modifier.animatePlacement(): Modifier = composed {
    val scope = rememberCoroutineScope()
    var targetOffset by remember { mutableStateOf(IntOffset.Zero) }
    var animatable by remember {
        mutableStateOf<Animatable<IntOffset, AnimationVector2D>?>(null)
    }
    this.onPlaced {
        // Calculate the position in the parent layout
        targetOffset = it.positionInParent().round()
    }
        .offset {
            // Animate to the new target offset when alignment changes.
            val anim = animatable
                ?: Animatable(targetOffset, IntOffset.VectorConverter).also { animatable = it }
            if (anim.targetValue != targetOffset) {
                scope.launch {
                    anim.animateTo(targetOffset, spring(stiffness = StiffnessMediumLow))
                }
            }
            // Offset the child in the opposite direction to the targetOffset, and slowly catch
            // up to zero offset via an animation to achieve an overall animated movement.
            animatable?.let { it.value - targetOffset } ?: IntOffset.Zero
        }
}