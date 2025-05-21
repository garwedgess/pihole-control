package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.GroupApiService
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toGroupEntity
import eu.wedgess.piholecontrol.data.mappers.toPiHoleGroupRequestData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.domain.model.ModifyGroupResponseEntity
import eu.wedgess.piholecontrol.domain.repository.GroupRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    val api: GroupApiService,
    val dispatcherProvider: DispatcherProvider
) : GroupRepository {

    override suspend fun fetchAllGroups(connection: ConnectionEntity): Result<List<GroupEntity>> =
        withContext(dispatcherProvider.io) {
            api.fetchAllGroups(connection)
                .mapCatching { response -> response.groups.map { it.toGroupEntity() } }
        }

    override suspend fun updateGroup(
        connection: ConnectionEntity,
        name: String,
        group: GroupEntity
    ): Result<ModifyGroupResponseEntity> = withContext(dispatcherProvider.io) {
        api.updateGroup(connection, name, group.toPiHoleGroupRequestData())
            .mapCatching { it.toEntity() }
    }

    override suspend fun deleteGroup(connection: ConnectionEntity, name: String): Result<Unit> =
        withContext(dispatcherProvider.io) {
            api.deleteGroup(connection, name)
        }
}
