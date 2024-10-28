package eu.wedgess.mihole.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import eu.wedgess.mihole.ui.base.TabItem
import eu.wedgess.mihole.utils.UiText

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
        AllowList -> FilterRuleType.ALLOW
        BlockList -> FilterRuleType.BLOCK
    }

    companion object {
        fun all() = listOf(AllowList, BlockList)
    }
}