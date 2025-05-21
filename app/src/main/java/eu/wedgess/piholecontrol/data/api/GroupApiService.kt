package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.requests.PiHoleGroupRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleAuthSessionStatusResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleGroupsResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyGroupResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface GroupApiService {
    suspend fun fetchAllGroups(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleGroupsResponseData>

    suspend fun updateGroup(
        connection: ConnectionEntity,
        name: String,
        groupRequestData: PiHoleGroupRequestData
    ): PiHoleApiResult<PiHoleModifyGroupResponseData>

    suspend fun deleteGroup(
        connection: ConnectionEntity,
        name: String
    ): PiHoleApiResult<Unit>
}
