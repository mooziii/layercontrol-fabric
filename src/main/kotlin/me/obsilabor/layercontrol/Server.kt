package me.obsilabor.layercontrol

import me.obsilabor.layercontrol.networking.NetworkingListener

fun initServer() {
    NetworkingListener.init(true)
}