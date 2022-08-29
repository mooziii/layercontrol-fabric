package me.obsilabor.layercontrol.config

import kotlinx.serialization.Serializable

@Serializable
data class LayerControlConfig(
    val enabledLayers: MutableMap<CustomizableRenderLayer, Boolean>,
    var renderOnOthers: Boolean,
    var parrotVariantLeft: Int,
    var parrotVariantRight: Int,
    var parrotScale: Float? = 0.5f,
    var bigHeadScale: Float? = 2f,
    var shareWithOthers: Boolean? = true,
    var renderMyConfigOnOthers: Boolean? = false
) {
    companion object {
        val DEFAULT = LayerControlConfig(
            defaultLayers(),
            false,
            0,
            0,
            0.5f,
            2f,
            true,
            false
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