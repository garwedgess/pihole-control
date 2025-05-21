package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.domain.model.ModifyGroupResponseEntity

interface GroupRepository {
    suspend fun fetchAllGroups(connection: ConnectionEntity): Result<List<GroupEntity>>
    suspend fun updateGroup(
        connection: ConnectionEntity,
        name: String,
        group: GroupEntity
    ): Result<ModifyGroupResponseEntity>

    suspend fun deleteGroup(connection: ConnectionEntity, name: String): Result<Unit>
}
