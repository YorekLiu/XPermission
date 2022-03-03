package xyz.yorek.xpermission.core

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import xyz.yorek.xpermission.IXPermission
import xyz.yorek.xpermission.OnPermissionRequestCallback

/**
 * Created by yorek.liu on 2020/11/13
 *
 * XPermission 原生fragment实现
 *
 * @author yorek.liu
 */
internal class XPermissionLegacyImpl : IXPermission, Fragment() {

    private val mDelegate: XPermissionDelegate = XPermissionDelegate(
        realRequestPermission = { permissions, code ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, code)
            } else {
                onPermissionHadGranted()
            }
        },
        realStartActivityForResult = { intent, code ->
            startActivityForResult(intent, code)
        }
    )

    private fun onPermissionHadGranted() {
        mDelegate.onPermissionHadGranted()
    }

    override fun permissions(vararg permissions: String): XPermissionLegacyImpl {
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (host == null) return
        } else {
            if (activity == null) return
        }
        mDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (host == null) return
        } else {
            if (activity == null) return
        }
        mDelegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mDelegate.onAttach(context)
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        mDelegate.onAttach(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDelegate.onActivityCreated(savedInstanceState)
    }
}