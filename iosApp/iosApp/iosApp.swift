import UIKit
import ComposeApp

@main
final class AppDelegate: UIResponder, UIApplicationDelegate {}

final class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?

    func scene(
        _ scene: UIScene,
        willConnectTo session: UISceneSession,
        options connectionOptions: UIScene.ConnectionOptions
    ) {
        guard let windowScene = scene as? UIWindowScene else { return }

        let window = UIWindow(windowScene: windowScene)
        self.window = window
        window.rootViewController = MainKt.MainViewController(
            delegate: ConverterApplicationDelegate(window: window, userDefaults: UserDefaults(suiteName: Constants.appGroup) ?? .standard, spotlightService: SpotlightService())
        )
        window.makeKeyAndVisible()
    }
}
