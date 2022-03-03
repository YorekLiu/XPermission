package xyz.yorek.xpermisison.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import xyz.yorek.xpermisison.R;
import xyz.yorek.xpermission.XPermission;

/**
 * Created by yorek.liu on 2022/3/2
 *
 * @author yorek.liu
 */
public class JavaActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Java页面，点击跳转到高级特性页面");
        findViewById(R.id.tvTitle).setOnClickListener(v -> {
            startActivity(new Intent(this, OverlayPermissionTipsActivity.class));
        });
    }

    @Override
    public void requestSinglePermission(@NonNull TextView textView) {
        XPermission.get(this)
                .permissions(Manifest.permission.CAMERA)
                .request((isGranted, grantedList, deniedList) -> {
                    textView.setText(buildPermissionCallbackInfo(isGranted, grantedList, deniedList));
                });
    }

    @Override
    public void requestAllPermission(@NonNull TextView textView) {
        XPermission.get(this)
                .permissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).request((isGranted, grantedList, deniedList) -> {
                    textView.setText(buildPermissionCallbackInfo(isGranted, grantedList, deniedList));
                });
    }
}