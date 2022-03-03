package xyz.yorek.xpermission;

import java.util.Map;

/**
 * Created by yorek.liu on 2020/3/18
 *
 * @author yorek.liu
 */
public interface IXPermission {

    /**
     * 要请求的权限
     */
    IXPermission permissions(String... permissions);

    /**
     * 针对权限的说明文案
     * 一般一个权限只需要一个固定的说明文案，通过XPermission#init方法注入
     *
     * 特殊情况下，业务方可以调用此方法overlay init方法注入的权限说明
     */
    IXPermission overlayPermissionTips(Map<String, kotlin.Pair<String, String>> overlayTips);

    /**
     * 在用户拒绝后一段时间内再次请求权限时，框架直接返回未授权，而不向用户发起请求，避免请求频次过高
     * @param tag 此处请求的tag
     * @param duration 多少ms之内自动拒绝
     */
    IXPermission autoDeny(String tag, long duration);

    /**
     * 在Java中使用时，由于SAM特性，可lambda
     * XPermission.get(mBaseActivity)
     *     .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
     *     .request((isGranted, grantedList, deniedList) -> {
     *         if (isGranted) {
     *             // permission granted
     *         } else {
     *             // permission denied
     *         }
     *     });
     * @param callback {@link OnPermissionRequestCallback}
     */
    void request(OnPermissionRequestCallback callback);
}
