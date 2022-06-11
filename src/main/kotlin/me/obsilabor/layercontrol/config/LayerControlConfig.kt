package me.obsilabor.layercontrol.config

import kotlinx.serialization.Serializable

@Serializable
data class LayerControlConfig(
    val enabledLayers: MutableMap<CustomizableRenderLayer, Boolean>,
    var renderOnOthers: Boolean,
    var parrotVariantLeft: Int,
    var parrotVariantRight: Int,
    var parrotScale: Float? = 0.5f
) {

    companion object {
        val DEFAULT = LayerControlConfig(
            defaultLayers(),
            false,
            0,
            0,
            0.5f
        )

        private fun defaultLayers(): MutableMap<CustomizableRenderLayer, Boolean> {
            val map = mutableMapOf<CustomizableRenderLayer, Boolean>()
            CustomizableRenderLayer.values().forEach {
                map[it] = it.mojang
            }
            return map
        }
    }

}