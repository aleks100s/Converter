import UIKit
import ComposeApp
import YandexMobileAds

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        let window = UIWindow(frame: UIScreen.main.bounds)
		self.window = window
		window.rootViewController = MainKt.MainViewController(
			delegate: ConverterApplicationDelegate(window: window),
			nativeViewFactory: NativeViewFactoryImpl()
		)
		window.makeKeyAndVisible()
		MobileAds.initializeSDK()
        return true
    }
}
