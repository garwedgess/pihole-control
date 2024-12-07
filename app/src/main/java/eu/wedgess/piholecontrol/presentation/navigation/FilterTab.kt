package eu.wedgess.piholecontrol.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.base.TabItem
import eu.wedgess.piholecontrol.utils.UiText

sealed class FilterTab(override val title: UiText, override val icon: ImageVector) :
    TabItem(title = title, icon = icon) {

    data object AllowList : FilterTab(
        title = UiText.StringResource(R.string.filters_tab_title_allow_list),
        icon = Icons.Default.CheckCircleOutline
    )

    data object BlockList : FilterTab(
        title = UiText.StringResource(R.string.filters_tab_title_block_list),
        icon = Icons.Default.Block
    )

    fun toFilterRuleType() = when(this) {
        AllowList -> FilterRuleTypeEntity.ALLOW
        BlockList -> FilterRuleTypeEntity.BLOCK
    }

    companion object {
        fun all() = listOf(AllowList, BlockList)
    }
}