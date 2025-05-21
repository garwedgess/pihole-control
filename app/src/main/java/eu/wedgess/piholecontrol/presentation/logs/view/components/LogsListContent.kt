package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo
import eu.wedgess.piholecontrol.presentation.logs.model.LogsDialogType
import eu.wedgess.piholecontrol.presentation.logs.view.components.dialogs.LogsDialogs
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LogsListContent(
    logsList: List<LogEntryInfo>,
    liveLoggingEnabled: Boolean,
    dialogType: LogsDialogType,
    onEvent: (LogsContract.Event) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState()
    ) {
        stickyHeader {
            LogsListStickyHeader(
                listSize = logsList.size,
                liveLoggingEnabled = liveLoggingEnabled,
                onLiveLoggingChange = { onEvent(LogsContract.Event.OnLiveLoggingChanged(it)) }
            )
        }
        items(logsList, key = { it.id }) { log ->
            LogListItem(log, onItemClick = { onEvent(LogsContract.Event.OnLogSelected(log)) })
        }
    }
    LogsDialogs(dialogType, onEvent)
}

@ThemePreview
@Composable
private fun LogsListContentPreview() {
    PiHoleControlTheme {
        Surface {
            LogsListContent(
                logsList = PiHoleLogsEntity.LogEntryStatusEntity.entries.map {
                    LogEntryInfo(
                        timestamp = System.currentTimeMillis().div(1000L),
                        client = "My Android",
                        domain = "www.google.com.ccckjkjakjkldasjklasjkjlj",
                        time = "10:12:01",
                        replyTime = 1.2,
                        queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.AAAA,
                        id = 1,
                        status = it,
                        dnssec = PiHoleLogsEntity.LogEntryDnssecEntity.UNKNOWN,
                        replyType = PiHoleLogsEntity.LogEntryReplyTypeEntity.DOMAIN,
                        listId = null,
                        edeCode = -1,
                        edeText = null,
                        cname = null
                    )
                },
                liveLoggingEnabled = false,
                dialogType = LogsDialogType.None,
                onEvent = {}
            )
        }
    }
}
