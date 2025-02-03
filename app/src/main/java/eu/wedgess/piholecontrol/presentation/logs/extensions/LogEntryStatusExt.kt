package eu.wedgess.piholecontrol.presentation.logs.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.percentageBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.queriesBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun PiHoleLogsEntity.LogEntryStatusEntity.toStringValue() = when (this) {
    PiHoleLogsEntity.LogEntryStatusEntity.UNKNOWN ->
        stringResource(id = R.string.logs_type_label_unknown)

    PiHoleLogsEntity.LogEntryStatusEntity.GRAVITY ->
        stringResource(id = R.string.logs_type_label_block_gravity)

    PiHoleLogsEntity.LogEntryStatusEntity.FORWARDED ->
        stringResource(id = R.string.logs_type_label_forwarded)

    PiHoleLogsEntity.LogEntryStatusEntity.CACHE ->
        stringResource(id = R.string.logs_type_label_allow_cache)

    PiHoleLogsEntity.LogEntryStatusEntity.REGEX ->
        stringResource(id = R.string.logs_type_label_block_regex)

    PiHoleLogsEntity.LogEntryStatusEntity.DENYLIST ->
        stringResource(id = R.string.logs_type_label_block_list)

    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_IP ->
        stringResource(id = R.string.logs_type_label_block_external_ip)

    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_NULL ->
        stringResource(id = R.string.logs_type_label_block_external_null)

    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_NXRA ->
        stringResource(id = R.string.logs_type_label_block_external_nrxa)

    PiHoleLogsEntity.LogEntryStatusEntity.GRAVITY_CNAME ->
        stringResource(id = R.string.logs_type_label_block_gravity_cname)

    PiHoleLogsEntity.LogEntryStatusEntity.REGEX_CNAME ->
        stringResource(id = R.string.logs_type_label_block_regex_cname)

    PiHoleLogsEntity.LogEntryStatusEntity.DENYLIST_CNAME ->
        stringResource(id = R.string.logs_type_label_block_list_cname)

    PiHoleLogsEntity.LogEntryStatusEntity.RETRIED ->
        stringResource(id = R.string.logs_type_label_allow_retried)

    PiHoleLogsEntity.LogEntryStatusEntity.RETRIED_DNSSEC ->
        stringResource(id = R.string.logs_type_label_dnssed)

    PiHoleLogsEntity.LogEntryStatusEntity.IN_PROGRESS ->
        stringResource(id = R.string.logs_type_label_inprogress)

    PiHoleLogsEntity.LogEntryStatusEntity.DBBUSY ->
        stringResource(id = R.string.logs_type_label_dbbusy)

    PiHoleLogsEntity.LogEntryStatusEntity.SPECIAL_DOMAIN ->
        stringResource(id = R.string.logs_type_label_special_domain)

    PiHoleLogsEntity.LogEntryStatusEntity.CACHE_STALE ->
        stringResource(id = R.string.logs_type_label_cache_stale)

    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_EDE15 ->
        stringResource(id = R.string.logs_type_label_external_blocked_ede15)
}

@Composable
fun PiHoleLogsEntity.LogEntryStatusEntity.toIcon() = when (this) {
    PiHoleLogsEntity.LogEntryStatusEntity.GRAVITY,
    PiHoleLogsEntity.LogEntryStatusEntity.FORWARDED,
    PiHoleLogsEntity.LogEntryStatusEntity.RETRIED,
    PiHoleLogsEntity.LogEntryStatusEntity.RETRIED_DNSSEC,
    PiHoleLogsEntity.LogEntryStatusEntity.IN_PROGRESS -> Icons.Default.GppGood

    PiHoleLogsEntity.LogEntryStatusEntity.REGEX,
    PiHoleLogsEntity.LogEntryStatusEntity.DENYLIST,
    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_IP,
    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_NULL,
    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_NXRA,
    PiHoleLogsEntity.LogEntryStatusEntity.GRAVITY_CNAME,
    PiHoleLogsEntity.LogEntryStatusEntity.REGEX_CNAME,
    PiHoleLogsEntity.LogEntryStatusEntity.DENYLIST_CNAME,
    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_EDE15 -> Icons.Default.GppBad

    PiHoleLogsEntity.LogEntryStatusEntity.UNKNOWN -> Icons.AutoMirrored.Filled.Help
    PiHoleLogsEntity.LogEntryStatusEntity.CACHE -> Icons.Default.Cached
    PiHoleLogsEntity.LogEntryStatusEntity.DBBUSY -> Icons.Default.HourglassEmpty
    PiHoleLogsEntity.LogEntryStatusEntity.SPECIAL_DOMAIN -> Icons.Default.Domain
    PiHoleLogsEntity.LogEntryStatusEntity.CACHE_STALE -> Icons.Default.Refresh
}

@Composable
fun PiHoleLogsEntity.LogEntryStatusEntity.toColor() = when (this) {
    PiHoleLogsEntity.LogEntryStatusEntity.FORWARDED,
    PiHoleLogsEntity.LogEntryStatusEntity.RETRIED,
    PiHoleLogsEntity.LogEntryStatusEntity.RETRIED_DNSSEC,
    PiHoleLogsEntity.LogEntryStatusEntity.IN_PROGRESS -> MaterialTheme.colorScheme.totalQueriesBackground

    PiHoleLogsEntity.LogEntryStatusEntity.GRAVITY,
    PiHoleLogsEntity.LogEntryStatusEntity.REGEX,
    PiHoleLogsEntity.LogEntryStatusEntity.DENYLIST,
    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_IP,
    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_NULL,
    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_NXRA,
    PiHoleLogsEntity.LogEntryStatusEntity.GRAVITY_CNAME,
    PiHoleLogsEntity.LogEntryStatusEntity.REGEX_CNAME,
    PiHoleLogsEntity.LogEntryStatusEntity.DENYLIST_CNAME,
    PiHoleLogsEntity.LogEntryStatusEntity.EXTERNAL_BLOCKED_EDE15 -> MaterialTheme.colorScheme.domainsOnAdListBackground

    PiHoleLogsEntity.LogEntryStatusEntity.UNKNOWN,
    PiHoleLogsEntity.LogEntryStatusEntity.DBBUSY,
    PiHoleLogsEntity.LogEntryStatusEntity.SPECIAL_DOMAIN -> MaterialTheme.colorScheme.percentageBlockedBackground

    PiHoleLogsEntity.LogEntryStatusEntity.CACHE,
    PiHoleLogsEntity.LogEntryStatusEntity.CACHE_STALE -> MaterialTheme.colorScheme.queriesBlockedBackground
}
