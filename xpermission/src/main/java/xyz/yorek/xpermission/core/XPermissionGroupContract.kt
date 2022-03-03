package xyz.yorek.xpermission.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo
import android.content.pm.PermissionInfo
import android.os.Build

/**
 * Created by yorek.liu on 2021/8/5
 *
 * @author yorek.liu
 */
class XPermissionGroupContract(
    val context: Context
) {
    private val mGroup2ItemListMap = mutableMapOf<String, MutableList<String>>()
    private val mItem2GroupMap = mutableMapOf<String, String>()

    init {
        buildContractHardcode()
    }

    /**
     * 代码获取到的权限组与认知不一致，采取下面的硬编码方式
     */
    private fun buildContract() {
        val packageManager = context.packageManager
        val groupList = packageManager.getAllPermissionGroups(PackageManager.GET_META_DATA)
        if (groupList.isNullOrEmpty()) return

        for (groupInfo : PermissionGroupInfo in groupList) {
            val groupName = groupInfo.name
            val permissionInfoList = packageManager.queryPermissionsByGroup(groupName, PackageManager.GET_META_DATA)

            var itemList = mGroup2ItemListMap[groupName]
            if (itemList == null) {
                itemList = mutableListOf()
                mGroup2ItemListMap[groupName] = itemList
            }

            for (permissionInfo : PermissionInfo in permissionInfoList) {
                val itemName = permissionInfo.name
                itemList.add(itemName)
                mItem2GroupMap[itemName] = groupName
            }
        }
    }

    private fun buildContractHardcode() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        mGroup2ItemListMap[Manifest.permission_group.STORAGE] = mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        mItem2GroupMap[Manifest.permission.READ_EXTERNAL_STORAGE] = Manifest.permission_group.STORAGE
        mItem2GroupMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = Manifest.permission_group.STORAGE
    }

    fun sortPermissionByGroup(permissions: Array<out String>): Array<out String> {
        // FIXME sort by group?
        return permissions
    }

    fun getPermissionGroup(itemName: String): String? {
        return mItem2GroupMap[itemName]
    }

    fun getPermissionItemList(groupName: String): List<String> {
        return mGroup2ItemListMap[groupName] ?: emptyList()
    }
}