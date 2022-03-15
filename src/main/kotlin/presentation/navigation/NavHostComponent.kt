package presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.router
import presentation.screen.createComponent.CreateComponentComponent
import presentation.screen.product.ProductComponent
import presentation.screen.products.ProductsComponent

class NavHostComponent(
    componentContext: ComponentContext
) : Component(componentContext) {

    private val router = router<ScreenConfig, Component>(
        initialConfiguration = ScreenConfig.Products,
        childFactory = ::createScreenComponent
    )

    private lateinit var _navController: NavController
    private val navController: NavController
        get() {
            if (!::_navController.isInitialized) {
                _navController = object : NavController {
                    override fun navigateUp() {
                        router.pop()
                    }

                    override fun navigate(config: ScreenConfig) {
                        router.navigate { it + config }
                    }
                }
            }
            return _navController
        }

    private fun createScreenComponent(
        screenConfig: ScreenConfig,
        componentContext: ComponentContext
    ) = when (screenConfig) {
        ScreenConfig.Products -> ProductsComponent(
            componentContext = componentContext,
            navController = navController
        )
        is ScreenConfig.Product -> ProductComponent(
            componentComponent = componentContext,
            config = screenConfig,
            navController = navController
        )
        is ScreenConfig.CreateComponent -> CreateComponentComponent(
            componentContext = componentContext,
            config = screenConfig,
            navController = navController
        )
    }

    @OptIn(ExperimentalDecomposeApi::class)
    @Composable
    override fun render() {
        Surface(modifier = Modifier.fillMaxSize()) {
            Children(
                routerState = router.state,
                animation = crossfade()
            ) {
                it.instance.render()
            }
        }
    }
}
