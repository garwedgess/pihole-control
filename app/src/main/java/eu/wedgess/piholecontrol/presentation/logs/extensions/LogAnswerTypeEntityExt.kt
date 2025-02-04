package eu.wedgess.piholecontrol.presentation.logs.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
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
fun PiHoleLogsEntity.LogsAnswerTypeEntity.toStringValue() = when (this) {
    PiHoleLogsEntity.LogsAnswerTypeEntity.UPSTREAM -> stringResource(id = R.string.logs_type_label_allow_upstream)
    PiHoleLogsEntity.LogsAnswerTypeEntity.ALREADY_FORWARDED -> stringResource(
        id = R.string.logs_type_label_allow_already_forwarded
    )

    PiHoleLogsEntity.LogsAnswerTypeEntity.LOCAL_CACHE -> stringResource(id = R.string.logs_type_label_allow_cache)
    PiHoleLogsEntity.LogsAnswerTypeEntity.RETRIED -> stringResource(id = R.string.logs_type_label_allow_retried)
    PiHoleLogsEntity.LogsAnswerTypeEntity.RETRIED_IGNORED -> stringResource(
        id = R.string.logs_type_label_allow_retried_ignored
    )

    PiHoleLogsEntity.LogsAnswerTypeEntity.GRAVITY_BLOCK -> stringResource(id = R.string.logs_type_label_block_gravity)
    PiHoleLogsEntity.LogsAnswerTypeEntity.REGEX_BLOCK -> stringResource(id = R.string.logs_type_label_block_gravity)
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXACT_BLOCK -> stringResource(id = R.string.logs_type_label_block_exact)
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_IP_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_external_ip
    )

    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_NULL_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_external_null
    )

    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_NXRA_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_external_nrxa
    )

    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_GRAVITY_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_gravity_cname
    )

    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_REGEX_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_regex_cname
    )

    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_EXACT_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_exact_cname
    )

    PiHoleLogsEntity.LogsAnswerTypeEntity.UNKNOWN -> stringResource(id = R.string.logs_type_label_unknown)
}

@Composable
fun PiHoleLogsEntity.LogsAnswerTypeEntity.toIcon() = when (this) {
    PiHoleLogsEntity.LogsAnswerTypeEntity.UPSTREAM,
    PiHoleLogsEntity.LogsAnswerTypeEntity.ALREADY_FORWARDED,
    PiHoleLogsEntity.LogsAnswerTypeEntity.RETRIED,
    PiHoleLogsEntity.LogsAnswerTypeEntity.RETRIED_IGNORED -> Icons.Default.GppGood

    PiHoleLogsEntity.LogsAnswerTypeEntity.GRAVITY_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.REGEX_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXACT_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_IP_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_NULL_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_NXRA_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_GRAVITY_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_REGEX_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_EXACT_BLOCK -> Icons.Default.GppBad

    PiHoleLogsEntity.LogsAnswerTypeEntity.UNKNOWN -> Icons.AutoMirrored.Filled.Help
    PiHoleLogsEntity.LogsAnswerTypeEntity.LOCAL_CACHE -> Icons.Default.Cached
}

@Composable
fun PiHoleLogsEntity.LogsAnswerTypeEntity.toColor() = when (this) {
    PiHoleLogsEntity.LogsAnswerTypeEntity.UPSTREAM,
    PiHoleLogsEntity.LogsAnswerTypeEntity.ALREADY_FORWARDED,
    PiHoleLogsEntity.LogsAnswerTypeEntity.RETRIED,
    PiHoleLogsEntity.LogsAnswerTypeEntity.RETRIED_IGNORED -> MaterialTheme.colorScheme.totalQueriesBackground

    PiHoleLogsEntity.LogsAnswerTypeEntity.GRAVITY_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.REGEX_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXACT_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_IP_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_NULL_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.EXTERNAL_NXRA_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_GRAVITY_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_REGEX_BLOCK,
    PiHoleLogsEntity.LogsAnswerTypeEntity.CNAME_EXACT_BLOCK -> MaterialTheme.colorScheme.domainsOnAdListBackground

    PiHoleLogsEntity.LogsAnswerTypeEntity.UNKNOWN -> MaterialTheme.colorScheme.percentageBlockedBackground
    PiHoleLogsEntity.LogsAnswerTypeEntity.LOCAL_CACHE -> MaterialTheme.colorScheme.queriesBlockedBackground
}
