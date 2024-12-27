package eu.wedgess.piholecontrol

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.internalToRoute
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class SavedStateHandleRule : TestWatcher() {

    val savedStateHandleMock: SavedStateHandle = mockk()
    private var currentRoute: Any? = null

    fun setRoute(route: Any) {
        currentRoute = route
    }

    override fun starting(description: Description?) {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandleMock.internalToRoute<Any>(any(), any()) } answers {
            currentRoute ?: error("Route not set. Call setRoute() in your test.")
        }
        super.starting(description)
    }

    override fun finished(description: Description?) {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
        currentRoute = null
        super.finished(description)
    }
}
