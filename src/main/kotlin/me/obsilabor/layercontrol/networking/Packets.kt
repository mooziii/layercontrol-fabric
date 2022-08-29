package me.obsilabor.layercontrol.networking

import net.minecraft.resources.ResourceLocation

object Packets {
    const val I_WANT_STRING = "layercontrol:i_want" // sent by the client to the server with the uuid of the player the client wants to know layers of
    const val PLS_GIVE_STRING = "layercontrol:pls_give" // sent by the server to the requested client
    const val MY_LAYERS_STRING = "layercontrol:my_layers" // the requested client sends his config to the server
    const val LAYERS_OF_STRING = "layercontrol:layers_of" // server returns config to the original requester
    const val INVALIDATE_STRING = "layercontrol:invalidate" // server sends this to all players because someone changed their config
    const val I_CHANGED_STRING = "layercontrol:i_changed" // sent to the server when a client changes its config
    val I_WANT = ResourceLocation(I_WANT_STRING)
    val PLS_GIVE = ResourceLocation(PLS_GIVE_STRING)
    val MY_LAYERS = ResourceLocation(MY_LAYERS_STRING)
    val LAYERS_OF = ResourceLocation(LAYERS_OF_STRING)
    val INVALIDATE = ResourceLocation(INVALIDATE_STRING)
    val I_CHANGED = ResourceLocation(I_CHANGED_STRING)
}