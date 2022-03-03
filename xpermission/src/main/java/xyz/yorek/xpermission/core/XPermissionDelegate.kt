package xyz.yorek.xpermission.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings
import xyz.yorek.xpermission.OnPermissionRequestCallback
import xyz.yorek.xpermission.XPermission
import xyz.yorek.xpermission.ui.PermissionUI

/**
 * Created by yorek.liu on 11/16/20
 *
 * @author yorek.liu
 */
class XPermissionDelegate(
    private val realRequestPermission: (Array<out String>, Int) -> Unit,
    private val realStartActivityForResult: (Intent, Int) -> Unit
) {
    private var mContext: Context? = null
    private var mPendingCheck = false

    private var mPendingPermission: Array<out String>? = null
    private var mPermissionRequestCallback: OnPermissionRequestCallback? = null

    private var mProceedingChain: XPermissionChain? = null
    private var mOverlayTips: MutableMap<String, Pair<String, String>> = mutableMapOf()

    private var mIsAutoDeny = false
    private var mTag: String? = null
    private var mDuration: Long = 0

    private val mWhat = 0x3812
    private val mMainHandler = Handler(Looper.getMainLooper())

    fun permissions(vararg permissions: String) {
        mPendingPermission = permissions
    }

    fun overlayPermissionTips(overlayTips: Map<String, Pair<String, String>>) {
        mOverlayTips.clear()
        mOverlayTips.putAll(overlayTips)
    }

    fun autoDeny(tag: String, duration: Long) {
        mIsAutoDeny = true
        mTag = tag
        mDuration = duration
    }

    fun request(callback: OnPermissionRequestCallback) {
        mPermissionRequestCallback = callback
        requestInternal()
    }

    private fun hasPermissions(vararg permissions: String): Boolean {
        return XPermission.hasPermissions(mContext!!, *permissions)
    }

    private fun canDrawOverlays(): Boolean {
        return XPermission.canDrawOverlays(mContext!!)
    }

    private fun requestInternal() {
        if (mPendingPermission == null || mPermissionRequestCallback == null) {
            return
        }

        if (mContext == null) {
            mPendingCheck = true
            return
        }

        // 悬浮窗权限
        if (mPendingPermission!!.contentEquals(arrayOf(XPermission.PERMISSION_OVERLAY))) {
            requestDrawOverlays(mContext!!)
        } else {
            requestNormalPermission()
        }
    }

    @SuppressLint("InlinedApi")
    private fun requestDrawOverlays(context: Context) {
        val permissionGranted = canDrawOverlays()

        if (permissionGranted) {
            onPermissionHadGranted()
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
            realStartActivityForResult(intent, XPermission.ACTIVITY_REQUEST_CODE)
        }
    }

    private fun requestNormalPermission() {
        val pendingPermission = mPendingPermission!!
        val permissionGranted = hasPermissions(*pendingPermission)

        if (permissionGranted) {
            onPermissionHadGranted()
        } else {
            if (mIsAutoDeny) {
                val permissionKVStore: IPermissionKVStore = XPermissionSharedPreferenceKVStore(mContext!!)
                val lastRequestTimestamp = permissionKVStore.get(mTag!!, 0)
                val currentTimeMillis = System.currentTimeMillis()
                if (currentTimeMillis - lastRequestTimestamp >= mDuration) {
                    permissionKVStore.put(mTag!!, currentTimeMillis)

                    val chain = XPermissionChain(mContext!!, pendingPermission)
                    mProceedingChain = chain
                    proceedPermission(chain)
                } else {
                    onPermissionHadDenied()
                }
            } else {
                val chain = XPermissionChain(mContext!!, pendingPermission)
                mProceedingChain = chain
                proceedPermission(chain)
            }
        }
    }

    fun onPermissionHadGranted() {
        mPermissionRequestCallback?.onRequest(true, emptyList(), emptyList())
        reset()
    }

    private fun onPermissionHadDenied() {
        mPermissionRequestCallback?.onRequest(false, emptyList(), emptyList())
        reset()
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == XPermission.PERMISSION_REQUEST_CODE) {
            mProceedingChain?.let {
                it.onGranted(permissions, grantResults)
                proceedPermission(it)
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == XPermission.ACTIVITY_REQUEST_CODE) {
            if (canDrawOverlays()) {
                mPermissionRequestCallback?.onRequest(true, emptyList(), emptyList())
            } else {
                mPermissionRequestCallback?.onRequest(false, emptyList(), emptyList())
            }
        }

        reset()
    }
    
    private fun proceedPermission(chain: XPermissionChain) {
        if (chain.hasNext()) {
            val permissionPack = chain.next()

            // delay一段时间之后再弹出权限提示，防止权限被用户不再提示之后，没有权限请求框但是出现了权限提示
            mMainHandler.removeMessages(mWhat)
            val message: Message = Message.obtain(mMainHandler) {
                PermissionUI.showPermissionTips(mContext, mOverlayTips, permissionPack.first())
            }
            message.what = mWhat
            mMainHandler.sendMessageDelayed(message, 200)

            realRequestPermission(permissionPack, XPermission.PERMISSION_REQUEST_CODE)
        } else {
            // 权限请求全部完毕，准备回调外部接口
            callOuterCallback(chain)
        }
    }

    private fun callOuterCallback(chain: XPermissionChain) {
        mMainHandler.removeMessages(mWhat)
        PermissionUI.hidePermissionTips(mContext)
        chain.getPermissionResult { isConfirm, grantedList, deniedList ->
            mPermissionRequestCallback?.onRequest(isConfirm, grantedList, deniedList)
        }
        reset()
    }

    private fun reset() {
        mOverlayTips.clear()
        mIsAutoDeny = false
        mPendingPermission = null
        mPermissionRequestCallback = null
    }

    fun onAttach(context: Context?) {
        mContext = context
    }

    fun onAttach(activity: Activity?) {
        mContext = activity
    }

    fun onActivityCreated(savedInstanceState: Bundle?) {
        if (mPendingCheck) {
            mPendingCheck = false
            requestInternal()
        }
    }
}