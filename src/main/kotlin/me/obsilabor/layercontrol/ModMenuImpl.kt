package me.obsilabor.layercontrol

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.obsilabor.layercontrol.config.ClothConfigManager

class ModMenuImpl : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory {
            ClothConfigManager.buildScreen()
        }
    }

}