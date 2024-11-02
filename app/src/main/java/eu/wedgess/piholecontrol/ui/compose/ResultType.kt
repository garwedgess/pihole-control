package eu.wedgess.piholecontrol.ui.compose

import eu.wedgess.piholecontrol.utils.UiText

sealed interface ResultType {

    sealed interface Error : ResultType {
        data class WithTitle(val title: UiText) : Error
        data class WithTitleAndSubTitle(
            val title: UiText,
            val subTitle: UiText
        ) : Error
    }

    sealed interface Empty : ResultType {
        data class WithTitle(val title: UiText) : Empty
        data class WithTitleAndSubTitle(
            val title: UiText,
            val subTitle: UiText
        ) : Empty
    }

    sealed interface Loading : ResultType {
        data class WithTitle(val title: UiText = UiText.DynamicString("Loading")) : Loading
        data class WithTitleAndSubTitle(val title: UiText, val subtitle: UiText) : Loading
    }
}