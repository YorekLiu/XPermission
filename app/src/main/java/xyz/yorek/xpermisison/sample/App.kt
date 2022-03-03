package xyz.yorek.xpermisison.sample

import android.Manifest
import android.app.Application
import xyz.yorek.xpermisison.R
import xyz.yorek.xpermission.XPermission
import java.util.HashMap

/**
 * Created by yorek.liu on 2022/3/2
 *
 * @author yorek.liu
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initXPermission()
    }

    private fun initXPermission() {
        val permissionTips: MutableMap<String, Pair<String, String>> = HashMap()
        permissionTips[Manifest.permission.CAMERA] = "相机权限申请说明" to "为了您能正常使用上传头像、作品、照片和扫一扫等功能，需要您授权"
        permissionTips[Manifest.permission.RECORD_AUDIO] = "录音权限申请说明" to "为了您能正常录制、跟读内容或发表语音"
        permissionTips[Manifest.permission.READ_EXTERNAL_STORAGE] = "存储权限申请说明" to "下载音频、分享海报、剪裁图片、上传图片和分享海报时，需要您授权"
        permissionTips[Manifest.permission.WRITE_EXTERNAL_STORAGE] = "存储权限申请说明" to "下载音频、分享海报、剪裁图片、上传图片和分享海报时，需要您授权"
        permissionTips[Manifest.permission.ACCESS_COARSE_LOCATION] = "定位权限申请说明" to "为了确定所在地区的版权情况、为您的早教机等设备进行联网，需要您授权"
        permissionTips[Manifest.permission.READ_PHONE_STATE] = "通话权限申请说明" to "为保证您账号登录安全，以及通话时自动暂停播放，需要您授权通话权限"

        XPermission.init(xyz.yorek.xpermission.R.layout.xpermission_tip, permissionTips)
    }
}