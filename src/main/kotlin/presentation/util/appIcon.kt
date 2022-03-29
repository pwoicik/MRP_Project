package presentation.util

import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import java.io.FileNotFoundException
import kotlin.io.path.Path
import kotlin.io.path.inputStream

fun getAppIcon() = try {
    loadSvgPainter(
        Path(
            System.getProperty("user.dir"),
            "app",
            "resources",
            "icon.svg"
        ).inputStream(),
        density = Density(1f)
    )
} catch (_: FileNotFoundException) {
    null
}
