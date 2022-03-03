package xyz.yorek.xpermission.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * Created by yorek.liu 2020/4/10
 *
 * @author yorek.liu
 */
class StatusBarPlaceholder(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
            getStatusBarHeight())

    }

    private fun getStatusBarHeight(): Int {
        if (sStatusBarHeight == -1) {
            sStatusBarHeight = getStatusBarHeight(context)
        }

        return sStatusBarHeight
    }

    companion object {
        private var sStatusBarHeight = -1

        fun getStatusBarHeight(context: Context): Int {
            if (sStatusBarHeight == -1) {
                sStatusBarHeight = getStatusBarHeightInner(context)
            }

            return sStatusBarHeight
        }

        private fun getStatusBarHeightInner(context: Context): Int {
            var result = 0
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }
    }
}