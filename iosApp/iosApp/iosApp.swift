import UIKit
import ComposeApp

@main
final class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        let window = UIWindow(frame: UIScreen.main.bounds)
		self.window = window
		window.rootViewController = MainKt.MainViewController(
            delegate: ConverterApplicationDelegate(window: window, userDefaults: UserDefaults(suiteName: Constants.appGroup) ?? .standard, spotlightService: SpotlightService())
		)
		window.makeKeyAndVisible()
        return true
    }
}
