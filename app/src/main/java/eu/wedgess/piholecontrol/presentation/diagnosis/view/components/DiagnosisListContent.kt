package eu.wedgess.piholecontrol.presentation.diagnosis.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
import eu.wedgess.piholecontrol.presentation.common.model.isSelected
import eu.wedgess.piholecontrol.presentation.diagnosis.DiagnosisContract
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisMessageInfo

@Composable
fun DiagnosisListContent(
    messages: List<DiagnosisMessageInfo>,
    selectionMode: SelectionMode<Int>,
    onEvent: (DiagnosisContract.Event) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState()
    ) {
        items(messages, key = { it.id }) { message ->
            DiagnosisMessageItem(
                modifier = Modifier.animateItem(),
                message = message,
                isSelected = selectionMode.isSelected(message.id),
                showDismissAction = selectionMode !is SelectionMode.Active,
                onItemClick = {
                    onEvent(DiagnosisContract.Event.OnDiagnosisMessageClick(message))
                },
                onItemLongClick = {
                    onEvent(DiagnosisContract.Event.OnDiagnosisMessageLongClick(message))
                },
                onDismissClick = {
                    onEvent(DiagnosisContract.Event.OnDismissDiagnosisMessageClick(message))
                }
            )
        }
    }
}
