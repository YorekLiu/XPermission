package xyz.yorek.xpermission;

import java.util.List;

/**
 * Created by yorek.liu on 2020/3/18
 *
 * @author yorek.liu
 */
public interface OnPermissionRequestCallback {
    /**
     * 权限请求回调
     * @param isGranted true表示所有权限都授予了；false表示至少有一种权限没有授予，具体授予情况请查看grantedList及deniedList
     * @param grantedList 当所有权限都已经授予时，该参数为一个空List；
     * 当所有权限在此次授予时，会返回所有权限的List；
     * 当有部分权限没有授予时，该参数会存放实际已经授权的权限；
     * @param deniedList 当有部分权限没有授予时，该参数会存放用户拒绝权限；
     * 其他情况为空的List
     */
    void onRequest(boolean isGranted, List<String> grantedList, List<String> deniedList);
}
