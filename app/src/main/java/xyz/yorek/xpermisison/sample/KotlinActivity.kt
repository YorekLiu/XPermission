package xyz.yorek.xpermisison.sample

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import xyz.yorek.xpermisison.R
import xyz.yorek.xpermission.XPermission
import java.util.*


class KotlinActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Kotlin页面，点击跳转到Java代码页面"
        findViewById<View>(R.id.tvTitle).setOnClickListener {
            startActivity(Intent(this, JavaActivity::class.java))
        }
    }

    override fun requestSinglePermission(textView: TextView) {
        XPermission.get(this)
            .permissions(Manifest.permission.CAMERA)
            .request { isGranted, grantedList, deniedList ->
                textView.text = buildPermissionCallbackInfo(isGranted, grantedList, deniedList)
            }
    }

    override fun requestAllPermission(textView: TextView) {
        XPermission.get(this)
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