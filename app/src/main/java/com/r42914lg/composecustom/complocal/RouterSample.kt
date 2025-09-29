package com.r42914lg.composecustom.complocal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

interface Screen {
    @Composable
    fun Content()
}

interface Router {
    val currentScreen: StateFlow<Screen>
    fun push(screen: Screen)
    fun pop()
}

class SomeRouterImpl(default: Screen) : Router {
    private val currentStack = ArrayDeque<Screen>()
    override val currentScreen = MutableStateFlow(default).also {
        currentStack.add(default)
    }

    override fun push(screen: Screen) {
        currentStack.add(screen)
        currentScreen.update { screen }
    }
    override fun pop() {
        if (currentStack.isEmpty()) {
            return
        }
        currentStack.removeLast()
        currentScreen.update { currentStack.last() }
    }
}

val localRouter = staticCompositionLocalOf<Router?> { null }

@Composable
fun AppContent(
    defaultScreen: Screen
) {
    CompositionLocalProvider(localRouter provides SomeRouterImpl(defaultScreen)) {
        MainContainer()
    }
}

@Preview
@Composable
fun AppContentPreview() {
    AppContent(ScreenOne)
}

/**
 * Container
 */

@Composable
fun MainContainer() {
    val router = localRouter.current
    router?.let {
        val currentContent by it.currentScreen.collectAsStateWithLifecycle()
        Column {
            Text("top bar", Modifier.fillMaxWidth().background(Color.Red))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .background(Color.Cyan)
            ) {
                currentContent.Content()
            }
            Text("bottom bar", Modifier.fillMaxWidth().background(Color.Green))
        }
    }
}

/**
 * Screens
 */

data object ScreenOne : Screen {
    @Composable
    override fun Content() = ComponentOne()
}

@Composable
fun ComponentOne() {
    val router = localRouter.current
    Text(
        text = "navigate to two",
        modifier = Modifier.clickable {
            router?.push(ScreenTwo)
        },
    )
}

data object ScreenTwo : Screen {
    @Composable
    override fun Content() = ComponentTwo()
}

@Composable
fun ComponentTwo() {
    val router = localRouter.current
    Text(
        text = "back to one",
        modifier = Modifier.clickable {
            router?.pop()
        },
    )
}