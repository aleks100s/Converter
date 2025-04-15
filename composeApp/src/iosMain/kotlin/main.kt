import androidx.compose.ui.window.ComposeUIViewController
import com.alextos.App
import com.alextos.di.initKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
