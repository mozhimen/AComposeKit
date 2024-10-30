package com.mozhimen.composek.ui.platform

import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.setFrom

/**
 * @ClassName LayerMatrixCache
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 2:07
 * @Version 1.0
 */
/**
 * Helper class to cache a [Matrix] and inverse [Matrix], allowing the instance to be reused until
 * the Layer's properties have changed, causing it to call [invalidate].
 *
 * This allows us to avoid repeated calls to [AndroidMatrix.getValues], which calls
 * an expensive native method (nGetValues). If we know the matrix hasn't changed, we can just
 * re-use it without needing to read and update values.
 */
internal class LayerMatrixCache<T>(
    private val getMatrix: (target: T, matrix: android.graphics.Matrix) -> Unit
) {
    private var androidMatrixCache: android.graphics.Matrix? = null
    private var previousAndroidMatrix: android.graphics.Matrix? = null
    private var matrixCache: Matrix? = null
    private var inverseMatrixCache: Matrix? = null

    private var isDirty = true
    private var isInverseDirty = true
    private var isInverseValid = true

    /**
     * Ensures that the internal matrix will be updated next time [calculateMatrix] or
     * [calculateInverseMatrix] is called - this should be called when something that will
     * change the matrix calculation has happened.
     */
    fun invalidate() {
        isDirty = true
        isInverseDirty = true
    }

    /**
     * Returns the cached [Matrix], updating it if required (if [invalidate] was previously called).
     */
    fun calculateMatrix(target: T): Matrix {
        val matrix = matrixCache ?: Matrix().also {
            matrixCache = it
        }
        if (!isDirty) {
            return matrix
        }

        val cachedMatrix = androidMatrixCache ?: android.graphics.Matrix().also {
            androidMatrixCache = it
        }

        getMatrix(target, cachedMatrix)

        val prevMatrix = previousAndroidMatrix
        if (prevMatrix == null || cachedMatrix != prevMatrix) {
            matrix.setFrom(cachedMatrix)
            androidMatrixCache = prevMatrix
            previousAndroidMatrix = cachedMatrix
        }

        isDirty = false
        return matrix
    }

    /**
     * Returns the cached inverse [Matrix], updating it if required (if [invalidate] was previously
     * called). This returns `null` if the inverse matrix isn't valid. This can happen, for example,
     * when scaling is 0.
     */
    fun calculateInverseMatrix(target: T): Matrix? {
        val matrix = inverseMatrixCache ?: Matrix().also {
            inverseMatrixCache = it
        }
        if (isInverseDirty) {
            val normalMatrix = calculateMatrix(target)
            isInverseValid = normalMatrix.invertTo(matrix)
            isInverseDirty = false
        }
        return if (isInverseValid) matrix else null
    }
}
