import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider(
    override val main: CoroutineDispatcher = Dispatchers.Unconfined,
    override val io: CoroutineDispatcher = Dispatchers.Unconfined,
    override val default: CoroutineDispatcher = Dispatchers.Unconfined
) : DispatcherProvider