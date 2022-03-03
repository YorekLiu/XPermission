package xyz.yorek.xpermisison.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannedString
import android.text.style.BulletSpan
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import xyz.yorek.xpermisison.R
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvSingle: TextView = findViewById(R.id.tvSingle)
        findViewById<View>(R.id.btnRequestSinglePermission).setOnClickListener {
            requestSinglePermission(tvSingle)
        }

        val tvAll: TextView = findViewById(R.id.tvAll)
        findViewById<View>(R.id.btnRequestAllPermission).setOnClickListener {
            requestAllPermission(tvAll)
        }
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        findViewById<TextView>(R.id.tvTitle).text = title
    }

    protected fun buildPermissionCallbackInfo(isGranted: Boolean, grantedList: List<String>, deniedList: List<String>): SpannedString {
        return buildSpannedString {
            appendLine("${Date(System.currentTimeMillis()).toLocaleString()}: isGranted=$isGranted")
            inSpans(BulletSpan(16, 0xFFFF0000.toInt())) {
                appendLine("grantedList=${grantedList}")
            }
            inSpans(BulletSpan(16, 0xFFFF0000.toInt())) {
                append("deniedList=${deniedList}")
            }
        }
    }

    abstract fun requestSinglePermission(textView: TextView)

    abstract fun requestAllPermission(textView: TextView)
}