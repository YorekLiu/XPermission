package xyz.yorek.xpermission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import xyz.yorek.xpermission.core.XPermissionLegacyImpl
import xyz.yorek.xpermission.core.XPermissionSupportImpl
import xyz.yorek.xpermission.ui.PermissionUI
import java.lang.IllegalStateException

/**
 * Created by yorek.liu 2020/3/18
 *
 * 基于Fragment的响应式API的动态权限请求框架
 *
 * @author yorek.liu
 */
class XPermission {

    companion object {
        private const val TAG = "XPermission"
        const val PERMISSION_OVERLAY = "PERMISSION_OVERLAY"
        const val PERMISSION_REQUEST_CODE = 0x0501
        const val ACTIVITY_REQUEST_CODE = 0x0502

        @JvmStatic
        fun init(@LayoutRes tipsLayoutRes: Int, permissionTips: Map<String, Pair<String, String>>) {
            PermissionUI.tipsLayoutRes = tipsLayoutRes
            PermissionUI.permissionTips = permissionTips
        }

        @JvmStatic
        fun get(context: Context): IXPermission {
            return when (context) {
                is FragmentActivity -> {
                    getInner(context)
                }
                is Activity -> {
                    getInner(context)
                }
                else -> {
                    throw IllegalStateException("context($context) isn't an activity!")
                }
            }
        }

        @JvmStatic
        fun hasPermissions(context: Context, vararg permissions: String): Boolean {
            return permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }

        @JvmStatic
        fun canDrawOverlays(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Settings.canDrawOverlays(context)
            } else {
                true
            }
        }

        private fun getInner(activity: FragmentActivity): IXPermission {
            var permissionRequestFragment =
                activity.supportFragmentManager.findFragmentByTag(TAG) as IXPermission?
            if (permissionRequestFragment == null) {
                permissionRequestFragment = XPermissionSupportImpl()
                activity.supportFragmentManager.beginTransaction()
                    .add(permissionRequestFragment, TAG)
                    .commitAllowingStateLoss()
            }

            return permissionRequestFragment
        }

        @Suppress("DEPRECATION")
        private fun getInner(activity: Activity): IXPermission {
            var permissionRequestFragment =
                activity.fragmentManager.findFragmentByTag(TAG) as IXPermission?
            if (permissionRequestFragment == null) {
                permissionRequestFragment = XPermissionLegacyImpl()
                activity.fragmentManager.beginTransaction()
                    .add(permissionRequestFragment, TAG)
                    .commitAllowingStateLoss()
            }

            return permissionRequestFragment
        }
    }
}