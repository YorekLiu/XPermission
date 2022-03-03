package xyz.yorek.xpermission.core

import android.content.Context
import android.content.pm.PackageManager
import xyz.yorek.xpermission.XPermission

/**
 * Created by yorek.liu on 2021/8/5
 *
 * @author yorek.liu
 */
class XPermissionChain(
    private val context: Context,
    private val allSinglePermission: Array<out String>
) {

    private var iterator: Int = 0
    private val mPermissionGroupContract: XPermissionGroupContract = XPermissionGroupContract(context)
    private lateinit var packedPermissionList: MutableList<MutableList<out String>>
    private val packedPermissionStatusArray: Array<Boolean>

    init {
        packSinglePermission()
        packedPermissionStatusArray = Array(packedPermissionList.size) { false }
        updatePermissionStatus(0)
    }

    fun hasNext(): Boolean {
        for (i in iterator until packedPermissionStatusArray.size) {
            if (!packedPermissionStatusArray[i]) return true
        }
        return false
    }

    fun next(): Array<String> {
        for (i in iterator until packedPermissionStatusArray.size) {
            if (!packedPermissionStatusArray[i]) {
                iterator = i + 1
                return packedPermissionList[i].toTypedArray()
            }
        }
        throw IllegalStateException("call hasNext() method before this")
    }

    fun onGranted(permission: Array<out String>, status: IntArray) {
        val granted = status.all { it == PackageManager.PERMISSION_GRANTED }
        packedPermissionStatusArray[iterator - 1] = granted
    }

    fun getPermissionResult(callback: (Boolean, List<String>, List<String>) -> Unit) {
        val isConfirm = packedPermissionStatusArray.all { it }
        if (isConfirm) {
            val grantedList = mutableListOf<String>()
            for (it in packedPermissionList) {
                grantedList.addAll(it)
            }
            callback(true, grantedList, emptyList())
        } else {
            val grantedList = mutableListOf<String>()
            val deniedList = mutableListOf<String>()
            packedPermissionList.forEachIndexed { index, packedPermission ->
                if (packedPermissionStatusArray[index]) {
                    grantedList.addAll(packedPermission)
                } else {
                    deniedList.addAll(packedPermission)
                }
            }
            callback(false, grantedList, deniedList)
        }
    }

    /**
     * 将权限按照权限组进行聚合
     */
    private fun packSinglePermission() {
        packedPermissionList = mutableListOf()
        val addedByGroupPack = Array(allSinglePermission.size) { false }

        allSinglePermission.forEachIndexed { index, permission ->
            if (addedByGroupPack[index]) return@forEachIndexed
            val group = mPermissionGroupContract.getPermissionGroup(permission)
            if (group == null) {
                packedPermissionList.add(mutableListOf(permission))
            } else {
                val permissionPacked = mutableListOf<String>()
                permissionPacked.add(permission)
                for (i in index + 1 until allSinglePermission.size) {
                    if (group == mPermissionGroupContract.getPermissionGroup(allSinglePermission[i])) {
                        permissionPacked.add(allSinglePermission[i])
                        addedByGroupPack[i] = true
                    }
                }
                packedPermissionList.add(permissionPacked)
            }
        }
    }

    private fun updatePermissionStatus(from: Int) {
        for (index in from until packedPermissionList.size) {
            val permissionGroup = packedPermissionList[index]
            packedPermissionStatusArray[index] = XPermission.hasPermissions(context, *permissionGroup.toTypedArray())
        }
    }
}