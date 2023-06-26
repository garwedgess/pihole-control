package eu.wedgess.mihole.utils.vico

import com.patrykandpatrick.vico.core.component.Component
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.context.MeasureContext

/**
 * Defines the appearance of an item of a [Legend].
 *
 * @param icon the [Component] used as the item’s icon.
 * @param label the [TextComponent] used for the label.
 * @param labelText the text content of the label.
 */
public open class LegendItem(
    public open val icon: Component,
    public open val label: TextComponent,
    public open val labelText: CharSequence,
) {
    /**
     * Measures the height of an item of a [Legend].
     *
     * @param context the [MeasureContext] used to measure the height.
     * @param availableWidth the available width for the item.
     * @param iconPaddingDp the padding between the icon and the label.
     * @param iconSizeDp the size of the icon.
     */
    public fun getHeight(
        context: MeasureContext,
        availableWidth: Float,
        iconPaddingDp: Float,
        iconSizeDp: Float,
    ): Float = with(context) {
        label.getHeight(
            context = context,
            text = labelText,
            width = (availableWidth - iconSizeDp.pixels - iconPaddingDp.pixels).toInt(),
        )
    }

    /**
     * Measures the width of the Label of a [LegendItem].
     *
     * @param context the [MeasureContext] used to measure the height.
     * @param availableWidth the available width for the item.
     * @param iconPaddingDp the padding between the icon and the label.
     * @param iconSizeDp the size of the icon.
     */
    public fun getOriginalLabelWidth(
        context: MeasureContext,
        availableWidth: Float,
        iconPaddingDp: Float,
        iconSizeDp: Float,
    ): Float = with(context) {
        label.getWidth(
            context = context,
            text = labelText,
            width = (availableWidth - iconSizeDp.pixels - iconPaddingDp.pixels).toInt(),
        )
    }

    /**
     * Measures the width of a [LegendItem], including Icon, Label and Padding between them.
     *
     * @param context the [MeasureContext] used to measure the height.
     * @param availableWidth the available width for the item.
     * @param iconPaddingDp the padding between the icon and the label.
     * @param iconSizeDp the size of the icon.
     */
    public fun getOriginalWidth(
        context: MeasureContext,
        availableWidth: Float,
        iconPaddingDp: Float,
        iconSizeDp: Float,
    ): Float = with(context) {
        getOriginalLabelWidth(
            context,
            availableWidth,
            iconPaddingDp,
            iconSizeDp,
        ) + (iconSizeDp + iconPaddingDp).pixels
    }
}