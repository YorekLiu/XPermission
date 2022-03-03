package xyz.yorek.xpermisison.sample

import android.Manifest
import android.os.Bundle
import android.widget.TextView
import xyz.yorek.xpermisison.R
import xyz.yorek.xpermission.XPermission
import java.util.concurrent.TimeUnit

/**
 * Created by yorek.liu on 2022/3/2
 *
 * @author yorek.liu
 */
class OverlayPermissionTipsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "高级特性"
        findViewById<TextView>(R.id.tvRequestSinglePermission).text = "自定义权限说明"
        findViewById<TextView>(R.id.btnRequestAllPermission).text = "拒绝后，1m内申请直接返回拒绝结果"
    }

    override fun requestSinglePermission(textView: TextView) {
        XPermission.get(this)
            .permissions(Manifest.permission.CAMERA)
            .overlayPermissionTips(
                mapOf(
                    Manifest.permission.CAMERA to ("相机权限自定义权限标题" to "这是相机权限自定义权限说明说明说明说明说明说明说明说明说明说明说明说明说明说明说明说明")
                )
            ).request { isGranted, grantedList, deniedList ->
                textView.text = buildPermissionCallbackInfo(isGranted, grantedList, deniedList)
            }
    }

    override fun requestAllPermission(textView: TextView) {
        XPermission.get(this)
            .autoDeny("test1", TimeUnit.MINUTES.toMillis(1))
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            ).request { isGranted, grantedList, deniedList ->
                textView.text = buildPermissionCallbackInfo(isGranted, grantedList, deniedList)
            }
    }
}