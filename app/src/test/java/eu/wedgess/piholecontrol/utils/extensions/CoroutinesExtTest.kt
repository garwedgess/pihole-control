package eu.wedgess.piholecontrol.utils.extensions

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CoroutinesExtTest {

    @Test
    fun `resultOf - returns success when block succeeds`() {
        val result = "Hello".resultOf { this.length }
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(5)
    }

    @Test
    fun `resultOf - returns failure when block throws exception`() {
        val result = "Hello".resultOf { throw RuntimeException("Test Exception") }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Test Exception")
    }

    @Test
    fun `resultOf - rethrows cancellation exception`() {
        try {
            "Hello".resultOf { throw CancellationException("Cancelled") }
        } catch (e: CancellationException) {
            assertThat(e.message).isEqualTo("Cancelled")
            return
        }
        throw AssertionError("CancellationException was not thrown")
    }

    @Test
    fun `Flow resultOf - emits success when flow succeeds`() = runTest {
        val flow = flowOf(1, 2, 3)
        flow.resultOf().test {
            assertThat(awaitItem().getOrNull()).isEqualTo(1)
            assertThat(awaitItem().getOrNull()).isEqualTo(2)
            assertThat(awaitItem().getOrNull()).isEqualTo(3)
            awaitComplete()
        }
    }

    @Test
    fun `Flow resultOf - emits failure when flow throws exception`() = runTest {
        val flow = flow<Int> { throw RuntimeException("Test Exception") }
        flow.resultOf().test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
            assertThat(result.exceptionOrNull()?.message).isEqualTo("Test Exception")
            awaitComplete()
        }
    }

    @Test
    fun `Flow resultOf - emits failure when flow emits exception`() = runTest {
        val flow = flow {
            emit(1)
            throw RuntimeException("Test Exception")
        }
        flow.resultOf().test {
            assertThat(awaitItem().getOrNull()).isEqualTo(1)
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
            assertThat(result.exceptionOrNull()?.message).isEqualTo("Test Exception")
            awaitComplete()
        }
    }

    @Test
    fun `mapResult - maps successful result`() = runTest {
        val flow = flowOf(Result.success(5))
        flow.mapResult { it * 2 }.test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            assertThat(result.getOrNull()).isEqualTo(10)
            awaitComplete()
        }
    }

    @Test
    fun `mapResult - passes through failed result`() = runTest {
        val exception = RuntimeException("Test Exception")
        val flow = flowOf(Result.failure<Int>(exception))
        flow.mapResult { it * 2 }.test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }

    @Test
    fun `runWithErrorHandling - returns success when operation succeeds`() = runTest {
        val result = runWithErrorHandling { "Hello" }
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `runWithErrorHandling - returns failure when operation throws exception`() = runTest {
        val result = runWithErrorHandling { throw RuntimeException("Test Exception") }
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Test Exception")
    }

    @Test
    fun `runWithErrorHandling - rethrows cancellation exception`() = runTest {
        try {
            runWithErrorHandling { throw CancellationException("Cancelled") }
        } catch (e: CancellationException) {
            assertThat(e.message).isEqualTo("Cancelled")
            return@runTest
        }
        throw AssertionError("CancellationException was not thrown")
    }
}
