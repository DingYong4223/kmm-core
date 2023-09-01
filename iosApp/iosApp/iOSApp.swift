import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
//        KBusinessEntry.companion
//                .doInitKmm(context: self)
//                .setConfig(config: KConfig(
//                        debug: true,
//                        platform: KAppConfig.companion.TYPE_PLAT_IOS,
//                        deviceID: "ios_debug_device_id")
//                )
        KLogerPlatformKt.doInitLoger(context: self, debug: true)
        KmmkvEntryKt.doInitKMMKV(context: self)

    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
