package com.mozhimen.composek.ui.node

import android.util.Log
import androidx.compose.runtime.AbstractApplier
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.kotlin.utilk.java.lang.UtilKField
import com.mozhimen.kotlin.utilk.java.lang.UtilKMethod
import com.mozhimen.kotlin.utilk.kotlin.strClazz2clazz
import java.lang.reflect.Field

/**
 * @ClassName UiApplier
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/10/29
 * @Version 1.0
 */
internal class UiApplier(
    root: Any,
) : AbstractApplier<Any>(root),IUtilK {

    override fun insertTopDown(index: Int, instance: Any) {
        // Ignored. Insert is performed in [insertBottomUp] to build the tree bottom-up to avoid
        // duplicate notification when the child nodes enter the tree.
    }

    override fun insertBottomUp(index: Int, instance: Any) {
//        current.insertAt(index, instance)
        Log.d(TAG, "insertBottomUp: instance ${instance.javaClass}")
        try {
            UtilKMethod.getDeclared_invoke_throw(current, "insertAt", arrayOf(Int::class.java, "androidx.compose.ui.node.LayoutNode".strClazz2clazz()), current, index, instance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun remove(index: Int, count: Int) {
//        current.removeAt(index, count)
        try {
            UtilKMethod.getDeclared_invoke_throw(current, "removeAt", arrayOf(Int::class.java, Int::class.java), current, index, count)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun move(from: Int, to: Int, count: Int) {
//        current.move(from, to, count)
        try {
            UtilKMethod.getDeclared_invoke_throw(current, "move", arrayOf(Int::class.java, Int::class.java, Int::class.java), current, from, to, count)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClear() {
//        root.removeAll()
        try {
            UtilKMethod.getDeclared_invoke_throw(current, "removeAll", arrayOf(), current)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onEndChanges() {
//        root.owner?.onEndApplyChanges()
        super.onEndChanges()
        try {
            val fieldOwner: Field = UtilKField.get(root, "owner")
            fieldOwner.isAccessible = true
            val owner = fieldOwner.get(root)
            if (owner!=null){
                UtilKMethod.getDeclared_invoke_throw(owner, "onEndApplyChanges", arrayOf(), owner)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
