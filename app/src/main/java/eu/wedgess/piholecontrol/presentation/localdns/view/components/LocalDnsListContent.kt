package eu.wedgess.piholecontrol.presentation.localdns.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
import eu.wedgess.piholecontrol.presentation.common.model.isSelected
import eu.wedgess.piholecontrol.presentation.localdns.LocalDnsContract
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordInfo

@Composable
fun LocalDnsListContent(
    records: List<LocalDnsRecordInfo>,
    selectionMode: SelectionMode<String>,
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
                isSelected = selectionMode.isSelected(record.rawValue),
                onItemClick = {
                    onEvent(LocalDnsContract.Event.OnLocalDnsRecordClick(record))
                },
                onItemLongClick = {
                    onEvent(LocalDnsContract.Event.OnLocalDnsRecordLongClick(record))
                }
            )
        }
    }
}
