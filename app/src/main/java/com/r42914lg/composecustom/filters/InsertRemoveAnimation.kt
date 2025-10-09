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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FilterItems(val data: List<Filter> = emptyList())

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
                val changedFilter =
                    displayItems.firstOrNull { it.filter.id == newFilter.id && it.filter.isSelected != newFilter.isSelected }

                changedFilter?.let {
                    coroutineScope.launch {
                        changedFilter.animation.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                        )
                        displayItems.remove(changedFilter)
                        val anim = Animatable(0f)
                        val position = if (newFilter.isSelected) 0 else displayItems.count { it.filter.isSelected }
                        displayItems.add(position, DisplayItem(newFilter, anim))
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
                modifier = Modifier
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

class ScreenModel {
    private val _state = MutableStateFlow(
        FilterItems(
            listOf(
                Filter(1, "Filter 1", isSelected = false),
                Filter(2, "Filter 2", isSelected = false),
                Filter(3, "Filter 3", isSelected = false),
                Filter(4, "Filter 4", isSelected = false),
                Filter(5, "Filter 5", isSelected = false),
                Filter(6, "Filter 6", isSelected = false),
            )
        )
    )
    val state = _state.asStateFlow()

    fun onClick(id: Int) {
        val currentList = state.value.data
        val newList = mutableListOf<Filter>()
        currentList.forEach {
            if (it.id == id) {
                newList.add(Filter(it.id, it.label, !it.isSelected))
            } else {
                newList.add(Filter(it.id, it.label, it.isSelected))
            }
        }
        _state.update { FilterItems(newList) }
    }
}

@Preview
@Composable
fun SamplePreview() {
    val stateHolder = remember { ScreenModel() }
    val state by stateHolder.state.collectAsStateWithLifecycle()

    Sample(state) {
       stateHolder.onClick(it)
    }
}
