package eu.wedgess.mihole.ui.filters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.utils.extensions.handleError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val repository: PiHoleRepository
) : ViewModel(), FiltersContract {

    private val _uiState: MutableStateFlow<FiltersContract.UiState> =
        MutableStateFlow(FiltersContract.UiState.initial())
    override val uiState: StateFlow<FiltersContract.UiState> = _uiState.asStateFlow()

    private val _effect: Channel<FiltersContract.Effect> = Channel(Channel.UNLIMITED)
    override val effect: Flow<FiltersContract.Effect> = _effect.receiveAsFlow()

    override fun onEvent(event: FiltersContract.Event) {
        when (event) {
            FiltersContract.Event.OnAddRuleClick -> displayAddRuleDialog()
            FiltersContract.Event.OnDismissAddRuleDialog -> dismissAddRuleDialog()
            FiltersContract.Event.FetchRulesList -> fetchRulesList()
            FiltersContract.Event.OnRuleDeselected -> deselectRule()
            is FiltersContract.Event.AddRule -> addRule(event.rule, event.isRegex)
            is FiltersContract.Event.RemoveRule -> removeRule(event.rule, event.ruleType)
            is FiltersContract.Event.OnRuleSelected -> ruleSelected(event.rule)
            is FiltersContract.Event.OnTabIndexChanged -> setCurrentFilterType(event.tabIndex)
        }
    }

    private fun setCurrentFilterType(tabIndex: Int) =
        _uiState.update { it.filterRuleType(tabIndex) }

    private fun displayAddRuleDialog() = _uiState.update { it.showAddRuleDialog() }
    private fun dismissAddRuleDialog() = _uiState.update { it.dismissAddRuleDialog() }

    private fun deselectRule() = _uiState.update { it.ruleDeselected() }
    private fun ruleSelected(
        rule: PiHoleFilterRules.PiHoleFilterRule
    ) =
        _uiState.update { it.selectedRule(rule) }

    private val fetchRulesListErrorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.update { it.filterListError(throwable.message ?: "Unknown error") }
    }

    private val addRuleErrorHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _effect.send(FiltersContract.Effect.Toast.RuleAddFailed)
        }
    }

    private val removeRuleErrorHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _effect.send(FiltersContract.Effect.Toast.RuleRemovalFailed)
        }
    }

    private fun fetchRulesList() = viewModelScope.launch(fetchRulesListErrorHandler) {
        when (val response = repository.fetchFilterRules()) {
            is ResponseResult.Success -> sortListAndSendToUi(response.data)

            is ResponseResult.Error -> fetchRulesListErrorHandler.handleException(
                this@launch.coroutineContext,
                response.handleError()
            )
        }
    }

    private fun sortListAndSendToUi(data: List<PiHoleFilterRules.PiHoleFilterRule>) {
        val allowList =
            data.filter { it.type == FilterRuleType.WHITE || it.type == FilterRuleType.REGEX_WHITE }
        val blockList =
            data.filter { it.type == FilterRuleType.BLACK || it.type == FilterRuleType.REGEX_BLACK }
        _uiState.update { it.allowList(allowList).blockList(blockList) }
    }

    private fun addRule(
        rule: String,
        isRegex: Boolean
    ) = viewModelScope.launch(addRuleErrorHandler) {
        val ruleType = if (_uiState.value.currentFilterRuleType == FilterRuleType.WHITE) {
            if (isRegex) FilterRuleType.REGEX_WHITE else FilterRuleType.WHITE
        } else {
            if (isRegex) FilterRuleType.REGEX_BLACK else FilterRuleType.BLACK
        }
        when (val response = repository.addFilterRules(rule, ruleType)) {
            is ResponseResult.Success -> {
                _uiState.update { it.dismissAddRuleDialog() }
                _effect.send(FiltersContract.Effect.Toast.RuleAdded)
                fetchRulesList()
            }

            is ResponseResult.Error -> addRuleErrorHandler.handleException(
                this@launch.coroutineContext,
                response.handleError()
            )
        }
    }

    private fun removeRule(
        rule: String,
        filterRuleType: FilterRuleType
    ) = viewModelScope.launch(removeRuleErrorHandler) {
        when (val response = repository.removeFilterRules(rule, filterRuleType)) {
            is ResponseResult.Success -> {
                _uiState.update { it.ruleDeselected() }
                _effect.send(FiltersContract.Effect.Toast.RuleRemoved)
                fetchRulesList()
            }

            is ResponseResult.Error -> removeRuleErrorHandler.handleException(
                this@launch.coroutineContext,
                response.handleError()
            )
        }
    }
}