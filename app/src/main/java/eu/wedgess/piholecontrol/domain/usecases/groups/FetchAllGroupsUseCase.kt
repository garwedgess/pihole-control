package eu.wedgess.piholecontrol.domain.usecases.groups

import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.GroupRepository
import javax.inject.Inject

class FetchAllGroupsUseCase @Inject constructor(
    private val groupsRepository: GroupRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(): Result<List<GroupEntity>> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                groupsRepository.fetchAllGroups(activeConnection)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
