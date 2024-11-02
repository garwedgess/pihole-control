package eu.wedgess.piholecontrol.ui.logs.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLog
import eu.wedgess.piholecontrol.ui.logs.LogsContract
import eu.wedgess.piholecontrol.ui.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.ui.logs.view.components.dialogs.LogsDialogs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LogsListContent(
    logsList: List<PiHoleLog>,
    dialogType: LogsDialogType,
    onEvent: (LogsContract.Event) -> Unit
) {

    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        stickyHeader {
            LogsListStickyHeader(listSize = logsList.size)
        }
        items(logsList) { log ->
            LogListItem(log, onItemClicked = { onEvent(LogsContract.Event.OnLogSelected(log)) })
        }
    }
    LogsDialogs(dialogType, onEvent)
}