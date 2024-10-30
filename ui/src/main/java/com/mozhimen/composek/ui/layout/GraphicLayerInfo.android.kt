package com.mozhimen.composek.ui.layout

/**
 * @ClassName GraphicLayerInfo
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/10/30 2:04
 * @Version 1.0
 */
/**
 * The info about the graphics layers used by tooling.
 */
//@JvmDefaultWithCompatibility
interface GraphicLayerInfo {
    /**
     * The ID of the layer. This is used by tooling to match a layer to the associated
     * LayoutNode.
     */
    val layerId: Long

    /**
     * The uniqueDrawingId of the owner view of this graphics layer. This is used by
     * tooling to match a layer to the associated owner AndroidComposeView.
     */
    val ownerViewId: Long
        get() = 0
}
