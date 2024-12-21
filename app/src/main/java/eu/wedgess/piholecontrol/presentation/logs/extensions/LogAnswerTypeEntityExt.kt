package eu.wedgess.piholecontrol.presentation.logs.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.LogAnswerTypeEntity
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.percentageBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.queriesBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun LogAnswerTypeEntity.toStringValue() = when (this) {
    LogAnswerTypeEntity.UPSTREAM -> stringResource(id = R.string.logs_type_label_allow_upstream)
    LogAnswerTypeEntity.ALREADY_FORWARDED -> stringResource(
        id = R.string.logs_type_label_allow_already_forwarded
    )
    LogAnswerTypeEntity.LOCAL_CACHE -> stringResource(id = R.string.logs_type_label_allow_cache)
    LogAnswerTypeEntity.RETRIED -> stringResource(id = R.string.logs_type_label_allow_retried)
    LogAnswerTypeEntity.RETRIED_IGNORED -> stringResource(
        id = R.string.logs_type_label_allow_retried_ignored
    )
    LogAnswerTypeEntity.GRAVITY_BLOCK -> stringResource(id = R.string.logs_type_label_block_gravity)
    LogAnswerTypeEntity.REGEX_BLOCK -> stringResource(id = R.string.logs_type_label_block_gravity)
    LogAnswerTypeEntity.EXACT_BLOCK -> stringResource(id = R.string.logs_type_label_block_exact)
    LogAnswerTypeEntity.EXTERNAL_IP_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_external_ip
    )
    LogAnswerTypeEntity.EXTERNAL_NULL_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_external_null
    )
    LogAnswerTypeEntity.EXTERNAL_NXRA_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_external_nrxa
    )
    LogAnswerTypeEntity.CNAME_GRAVITY_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_gravity_cname
    )
    LogAnswerTypeEntity.CNAME_REGEX_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_regex_cname
    )
    LogAnswerTypeEntity.CNAME_EXACT_BLOCK -> stringResource(
        id = R.string.logs_type_label_block_exact_cname
    )
    LogAnswerTypeEntity.UNKNOWN -> stringResource(id = R.string.logs_type_label_unknown)
}

@Composable
fun LogAnswerTypeEntity.toIcon() = when (this) {
    LogAnswerTypeEntity.UPSTREAM,
    LogAnswerTypeEntity.ALREADY_FORWARDED,
    LogAnswerTypeEntity.RETRIED,
    LogAnswerTypeEntity.RETRIED_IGNORED -> Icons.Default.GppGood
    LogAnswerTypeEntity.GRAVITY_BLOCK,
    LogAnswerTypeEntity.REGEX_BLOCK,
    LogAnswerTypeEntity.EXACT_BLOCK,
    LogAnswerTypeEntity.EXTERNAL_IP_BLOCK,
    LogAnswerTypeEntity.EXTERNAL_NULL_BLOCK,
    LogAnswerTypeEntity.EXTERNAL_NXRA_BLOCK,
    LogAnswerTypeEntity.CNAME_GRAVITY_BLOCK,
    LogAnswerTypeEntity.CNAME_REGEX_BLOCK,
    LogAnswerTypeEntity.CNAME_EXACT_BLOCK -> Icons.Default.GppBad
    LogAnswerTypeEntity.UNKNOWN -> Icons.AutoMirrored.Filled.Help
    LogAnswerTypeEntity.LOCAL_CACHE -> Icons.Default.Cached
}

@Composable
fun LogAnswerTypeEntity.toColor() = when (this) {
    LogAnswerTypeEntity.UPSTREAM,
    LogAnswerTypeEntity.ALREADY_FORWARDED,
    LogAnswerTypeEntity.RETRIED,
    LogAnswerTypeEntity.RETRIED_IGNORED -> MaterialTheme.colorScheme.totalQueriesBackground
    LogAnswerTypeEntity.GRAVITY_BLOCK,
    LogAnswerTypeEntity.REGEX_BLOCK,
    LogAnswerTypeEntity.EXACT_BLOCK,
    LogAnswerTypeEntity.EXTERNAL_IP_BLOCK,
    LogAnswerTypeEntity.EXTERNAL_NULL_BLOCK,
    LogAnswerTypeEntity.EXTERNAL_NXRA_BLOCK,
    LogAnswerTypeEntity.CNAME_GRAVITY_BLOCK,
    LogAnswerTypeEntity.CNAME_REGEX_BLOCK,
    LogAnswerTypeEntity.CNAME_EXACT_BLOCK -> MaterialTheme.colorScheme.domainsOnAdListBackground
    LogAnswerTypeEntity.UNKNOWN -> MaterialTheme.colorScheme.percentageBlockedBackground
    LogAnswerTypeEntity.LOCAL_CACHE -> MaterialTheme.colorScheme.queriesBlockedBackground
}
