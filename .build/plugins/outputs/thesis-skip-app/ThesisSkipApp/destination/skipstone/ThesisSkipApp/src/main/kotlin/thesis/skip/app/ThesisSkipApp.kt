package thesis.skip.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import skip.lib.*

import skip.foundation.*
import skip.ui.*
import skip.model.*

/// A logger for the ThesisSkipApp module.
internal val logger: SkipLogger = SkipLogger(subsystem = "be.hogent.ThesisSkipApp", category = "ThesisSkipApp")

/// The shared top-level view for the app, loaded from the platform-specific App delegates below.
///
/// The default implementation merely loads the `ContentView` for the app and logs a message.
class ThesisSkipAppRootView: View {
    constructor() {
    }

    override fun body(): View {
        return ComposeBuilder { composectx: ComposeContext ->
            Navigation()
                .task { -> MainActor.run { logger.info("Skip app logs are viewable in the Xcode console for iOS; Android logs can be viewed in Studio or using adb logcat") } }.Compose(composectx)
        }
    }

    companion object {
    }
}

/// Global application delegate functions.
///
/// These functions can update a shared observable object to communicate app state changes to interested views.
class ThesisSkipAppDelegate {

    private constructor() {
    }

    fun onInit(): Unit = logger.debug("onInit")

    fun onLaunch(): Unit = logger.debug("onLaunch")

    fun onResume(): Unit = logger.debug("onResume")

    fun onPause(): Unit = logger.debug("onPause")

    fun onStop(): Unit = logger.debug("onStop")

    fun onDestroy(): Unit = logger.debug("onDestroy")

    fun onLowMemory(): Unit = logger.debug("onLowMemory")

    companion object {
        val shared = ThesisSkipAppDelegate()
    }
}
