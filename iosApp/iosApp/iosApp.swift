import UIKit
import ComposeApp
import YandexMobileAds
import AppTrackingTransparency

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
			delegate: ConverterApplicationDelegate(window: window),
			nativeViewFactory: NativeViewFactoryImpl()
		)
		window.makeKeyAndVisible()
        trackApplicationLaunch()
		MobileAds.initializeSDK()
        return true
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        guard UserDefaults.standard.integer(forKey: "launch") > 1, ATTrackingManager.trackingAuthorizationStatus != .authorized else { return }

        DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
            ATTrackingManager.requestTrackingAuthorization { status in
                if status == .notDetermined {
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                        ATTrackingManager.requestTrackingAuthorization { _ in }
                    }
                }
            }
        }
    }
    
    private func trackApplicationLaunch() {
        var count = UserDefaults.standard.integer(forKey: "launch")
        count += 1
        UserDefaults.standard.set(count, forKey: "launch")
    }
}
