package xyz.yorek.xpermission.ui

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import xyz.yorek.xpermission.R

/**
 * Created by yorek.liu on 2021/8/4
 *
 * @author yorek.liu
 */
internal object PermissionUI {
    @LayoutRes var tipsLayoutRes: Int = 0
    lateinit var permissionTips: Map<String, Pair<String, String>>

    fun showPermissionTips(context: Context?, overlayTips: Map<String, Pair<String, String>>, permission: String) {
        val activity = context as? Activity ?: return

        val isPortrait = activity.resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val layoutResId = if (isPortrait) tipsLayoutRes else tipsLayoutRes

        if (layoutResId == 0) return
        val permissionTextPair = overlayTips[permission] ?: permissionTips[permission] ?: return

        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        var permissionTipsView: View? = contentView.findViewById(R.id.xpermission_permission_tips)
        if (permissionTipsView == null) {
            permissionTipsView = inflatePermissionTipsView(activity, layoutResId)
            permissionTipsView.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {}

                override fun onViewDetachedFromWindow(v: View?) {
                    v?.animation?.cancel()
                }
            })
            contentView.addView(permissionTipsView, getLayoutParams(isPortrait, activity))
            getSlideInAnimation(isPortrait).let {
                permissionTipsView.startAnimation(it)
            }
        }
        permissionTipsView.findViewById<TextView>(R.id.xpermission_title)?.text = permissionTextPair.first
        permissionTipsView.findViewById<TextView>(R.id.xpermission_info)?.text = permissionTextPair.second
    }

    fun hidePermissionTips(context: Context?) {
        val activity = context as? Activity ?: return

        val isPortrait = activity.resources.configuration.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        val permissionTipsView: View = contentView.findViewById(R.id.xpermission_permission_tips) ?: return

        val animation = getSlideOutAnimation(isPortrait)
        animation.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (activity != null && !activity.isDestroyed && !activity.isFinishing) {
                    contentView.removeView(permissionTipsView)
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        permissionTipsView.startAnimation(animation)
    }

    private fun inflatePermissionTipsView(activity: Activity, @LayoutRes layoutId: Int): View {
        val permissionTipsView = LayoutInflater.from(activity).inflate(layoutId, null, false)
        permissionTipsView.id = R.id.xpermission_permission_tips
        return permissionTipsView
    }

    private fun getLayoutParams(isPortrait: Boolean, activity: Activity): FrameLayout.LayoutParams {
        val layoutParam: FrameLayout.LayoutParams
        if (isPortrait) {
            layoutParam = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        } else {
            val decorViewWidth = activity.window.decorView.width
            // FIXME system permission dialog width?
            val systemPermissionDialogWidth = decorViewWidth * 0.65F
            val permissionViewWidth = (decorViewWidth - systemPermissionDialogWidth) / 2
            layoutParam = FrameLayout.LayoutParams(
                permissionViewWidth.toInt(),
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParam.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        }
        return layoutParam
    }

    private fun getSlideInAnimation(isPortrait: Boolean): Animation {
        val animation = if (isPortrait) {
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0F, Animation.RELATIVE_TO_SELF, 0F,
                Animation.RELATIVE_TO_SELF, -1F, Animation.RELATIVE_TO_SELF, 0F
            )
        } else {
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1F, Animation.RELATIVE_TO_SELF, 0F,
                Animation.RELATIVE_TO_SELF, 0F, Animation.RELATIVE_TO_SELF, 0F
            )
        }
        animation.duration = 600
        animation.interpolator = LinearInterpolator()
        return animation
    }

    private fun getSlideOutAnimation(isPortrait: Boolean): Animation {
        val animation = if (isPortrait) {
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0F, Animation.RELATIVE_TO_SELF, 0F,
                Animation.RELATIVE_TO_SELF, 0F, Animation.RELATIVE_TO_SELF, -1F
            )
        } else {
            TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0F, Animation.RELATIVE_TO_SELF, 1F,
                Animation.RELATIVE_TO_SELF, 0F, Animation.RELATIVE_TO_SELF, 0F
            )
        }
        animation.duration = 600
        animation.interpolator = LinearInterpolator()
        return animation
    }
}