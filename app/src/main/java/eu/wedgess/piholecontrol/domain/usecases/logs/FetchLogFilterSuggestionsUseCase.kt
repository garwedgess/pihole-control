package eu.wedgess.piholecontrol.domain.usecases.logs

import eu.wedgess.piholecontrol.domain.model.LogFilterSuggestionsEntity
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchLogFilterSuggestionsUseCase @Inject constructor(
    private val logsRepository: LogsRepository,
    private val observeActiveUser: ObserveActiveUserUseCase
) {
    operator fun invoke(): Flow<Result<LogFilterSuggestionsEntity>> {
        return observeActiveUser().map { connection ->
            connection.fold(
                onSuccess = { activeConnection ->
                    logsRepository.fetchLogFilterSuggestions(activeConnection)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        }
    }
}
