package com.r42914lg.composecustom.filters

import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
    FiltersBlock(items)
}