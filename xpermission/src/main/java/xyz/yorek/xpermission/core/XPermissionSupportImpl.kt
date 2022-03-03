package xyz.yorek.xpermission.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import xyz.yorek.xpermission.IXPermission
import xyz.yorek.xpermission.OnPermissionRequestCallback

/**
 * Created by yorek.liu on 2020/11/13
 *
 * XPermission support实现
 *
 * @author yorek.liu
 */
internal class XPermissionSupportImpl : IXPermission, Fragment() {

    private val mDelegate: XPermissionDelegate = XPermissionDelegate(
        realRequestPermission = { permissions, code ->
            requestPermissions(permissions, code)
        },
        realStartActivityForResult = { intent, code ->
            startActivityForResult(intent, code)
        }
    )

    override fun permissions(vararg permissions: String): XPermissionSupportImpl {
        mDelegate.permissions(*permissions)
        return this
    }

    override fun overlayPermissionTips(overlayTips: Map<String, Pair<String, String>>): IXPermission {
        mDelegate.overlayPermissionTips(overlayTips)
        return this
    }

    override fun autoDeny(tag: String, duration: Long): IXPermission {
        mDelegate.autoDeny(tag, duration)
        return this
    }

    /**
     * @param callback
     * @see OnPermissionRequestCallback
     */
    override fun request(callback: OnPermissionRequestCallback) {
        mDelegate.request(callback)
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (host == null) return
        mDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (host == null) return
        mDelegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDelegate.onAttach(context)
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mDelegate.onAttach(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDelegate.onActivityCreated(savedInstanceState)
    }
}