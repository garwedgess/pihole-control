package eu.wedgess.piholecontrol.presentation.localdns.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.localdns.LocalDnsContract
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordInfo

@Composable
fun LocalDnsListContent(
    records: List<LocalDnsRecordInfo>,
    onEvent: (LocalDnsContract.Event) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState()
    ) {
        items(records, key = { it.rawValue }) { record ->
            LocalDnsRecordItem(
                modifier = Modifier.animateItem(),
                record = record,
                onItemClick = {
                    onEvent(LocalDnsContract.Event.OnLocalDnsRecordClick(record))
                }
            )
        }
    }
}
