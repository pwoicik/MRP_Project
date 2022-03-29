package presentation.navigation

interface NavController {

    fun navigateUp()

    fun navigate(config: ScreenConfig)
}
