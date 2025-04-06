import androidx.compose.ui.window.ComposeUIViewController
import com.alextos.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
