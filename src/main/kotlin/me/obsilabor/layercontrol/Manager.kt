package me.obsilabor.layercontrol

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.obsilabor.layercontrol.config.ClothConfigManager
import net.minecraft.client.Minecraft

fun init() {
    ClothConfigManager
}

val minecraft: Minecraft
get() = Minecraft.getInstance()

@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    prettyPrint = true
    encodeDefaults = true
    prettyPrintIndent = "  "
}