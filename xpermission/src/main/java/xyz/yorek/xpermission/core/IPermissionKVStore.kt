package xyz.yorek.xpermission.core

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by yorek.liu on 2021/8/6
 *
 * @author yorek.liu
 */
interface IPermissionKVStore {
    fun put(key: String, value: Long)

    fun get(key: String, default: Long): Long
}

class XPermissionSharedPreferenceKVStore(
    val context: Context
): IPermissionKVStore {
    private val mSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("xpermission", Context.MODE_PRIVATE)
    }

    override fun put(key: String, value: Long) {
        mSharedPreferences.edit().putLong(key, value).apply()
    }

    override fun get(key: String, default: Long): Long {
        return mSharedPreferences.getLong(key, default)
    }

}