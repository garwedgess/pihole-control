package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.requests.PiHoleGroupRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleGroupsResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyGroupResponseData
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.domain.model.ModifyGroupResponseEntity
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun PiHoleGroupsResponseData.GroupData.toGroupEntity() = GroupEntity(
    id = this.id,
    name = this.name,
    enabled = this.enabled,
    comment = this.comment,
    dateAdded = Instant.ofEpochSecond(this.dateAdded)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .replace("T", " "),
    dateModified = Instant.ofEpochSecond(this.dateModified)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .replace("T", " "),
)

fun GroupEntity.toPiHoleGroupRequestData() = PiHoleGroupRequestData(
    name = this.name,
    enabled = this.enabled,
    comment = this.comment,
)

fun PiHoleModifyGroupResponseData.toEntity() = ModifyGroupResponseEntity(
    success = this.processed?.run { success.isNotEmpty() && errors.isEmpty() } ?: false,
    message = this.processed?.run {
        success.joinToString(separator = ",") { it.item }.takeIf { success.isNotEmpty() }
            ?: errors.joinToString(separator = ",") { it.error }.takeIf { success.isNotEmpty() }
    }
)
