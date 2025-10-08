package com.r42914lg.composecustom.filters

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class FilterItems(val data: List<Filter>)

data class DisplayItem(
    val filter: Filter,
    val animation: Animatable<Float, AnimationVector1D>
)

@Composable
fun Sample(
    filters: FilterItems,
    onClick: (Int) -> Unit
) {
    val displayItems = remember { mutableStateListOf<DisplayItem>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(filters.data) {
        if (displayItems.isEmpty()) {
            filters.data.forEach {
                displayItems.add(DisplayItem(it, Animatable(1f)))
            }
        } else {
            filters.data.forEach { newFilter ->
                val oldFilter =
                    displayItems.firstOrNull { it.filter.id == newFilter.id && it.filter.isSelected != newFilter.isSelected }

                oldFilter?.let {
                    coroutineScope.launch {
                        oldFilter.animation.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                        )
                        displayItems.remove(oldFilter)
                        val anim = Animatable(0f)
                        displayItems.add(DisplayItem(newFilter, anim))
                        coroutineScope.launch {
                            anim.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                            )
                        }
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier.animateContentSize()
            ) {
                displayItems.forEach { item ->
                    key(item.filter) {
                        val scale by item.animation.asState()
                        if (scale > 0f) {
                        Box(
                            modifier = Modifier.graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                alpha = scale
                            }
                        ) {
                            RenderFilter(item.filter) {
                                onClick(it)
                            }
                        }
                    }
                    }
                }
            }
        }
    )
}

@Composable
fun Card(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(modifier = modifier.padding(16.dp)) { content() }
}

@Composable
fun RenderFilter(filter: Filter, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(filter.id) }
    ) {
        Text(
            modifier = Modifier.border(1.dp, if (filter.isSelected) Color.Blue else Color.Gray),
            text = filter.label
        )
    }
}

@Preview
@Composable
fun SamplePreview() {
    val items = remember {
        val list = mutableStateListOf<Filter>()
        for (i in 0..5) {
            list.add(Filter(i, "Filter $i", isSelected = false))
        }
        list
    }
    val sortedItems by remember {
        derivedStateOf {
            items.sortedWith(compareByDescending { it.isSelected })
        }
    }
    Sample(FilterItems(sortedItems)) { id ->
        val oldValue = items.find { it.id == id }
//        items.remove(oldValue)
        oldValue?.let {
            items.add(it.copy(isSelected = oldValue.isSelected.not()))
        }
    }
}
