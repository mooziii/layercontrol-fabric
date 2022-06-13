package me.obsilabor.layercontrol.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.layercontrol.json
import me.obsilabor.layercontrol.minecraft
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.entity.ParrotRenderer
import net.minecraft.network.chat.Component
import java.io.File

object ClothConfigManager {

    private fun saveConfigToFile() {
        configFile.writeText(json.encodeToString(config))
    }

    fun buildScreen(): Screen {
        val builder = ConfigBuilder.create()
            .setParentScreen(minecraft.screen)
            .setTitle(Component.translatable("layercontrol.config"))
            .setSavingRunnable {
                saveConfigToFile()
            }
        val general = builder.getOrCreateCategory(Component.translatable("layercontrol.category.general"))
        val mojangLayers = builder.getOrCreateCategory(Component.nullToEmpty("Mojang Layers"))
        val customLayers = builder.getOrCreateCategory(Component.nullToEmpty("Custom Layers"))
        val entryBuilder = builder.entryBuilder()
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("layercontrol.option.renderOnOthers"), config?.renderOnOthers ?: true)
            .setSaveConsumer {
                config?.renderOnOthers = it
            }
            .setDefaultValue(LayerControlConfig.DEFAULT.renderOnOthers)
            .build())
        general.addEntry(entryBuilder.startIntSlider(Component.translatable("layercontrol.option.parrotVariantLeft"), config?.parrotVariantLeft ?: 0, 0, ParrotRenderer.PARROT_LOCATIONS.size-1)
            .setSaveConsumer {
                config?.parrotVariantLeft = it
            }
            .setDefaultValue(LayerControlConfig.DEFAULT.parrotVariantLeft)
            .build())
        general.addEntry(entryBuilder.startFloatField(Component.translatable("layercontrol.option.parrotScale"), config?.parrotScale ?: 0.5f)
            .setSaveConsumer {
                config?.parrotScale = it
            }
            .setDefaultValue(LayerControlConfig.DEFAULT.parrotScale ?: 0.5f)
            .build())
        general.addEntry(entryBuilder.startIntSlider(Component.translatable("layercontrol.option.parrotVariantRight"), config?.parrotVariantRight ?: 0, 0, ParrotRenderer.PARROT_LOCATIONS.size-1)
            .setSaveConsumer {
                config?.parrotVariantRight = it
            }
            .setDefaultValue(LayerControlConfig.DEFAULT.parrotVariantRight)
            .build())
        general.addEntry(entryBuilder.startFloatField(Component.translatable("layercontrol.option.bigHeadSize"), config?.bigHeadScale ?: 2f)
            .setSaveConsumer {
                if (it > 100f)
                    config?.bigHeadScale = 100f
                else
                    config?.bigHeadScale = it
            }
            .setDefaultValue(LayerControlConfig.DEFAULT.bigHeadScale ?: 2f)
            .build())
        CustomizableRenderLayer.values().forEach { layer ->
            (if(layer.mojang) mojangLayers else customLayers).addEntry(entryBuilder.startBooleanToggle(Component.nullToEmpty(layer.name), config?.enabledLayers?.get(layer) ?: layer.mojang)
                .setSaveConsumer {
                    config?.enabledLayers?.set(layer, it)
                }
                .setDefaultValue(layer.mojang)
                .build())
        }
        return builder.build()
    }

    private val configFile = File(System.getProperty("user.dir") + "/config", "layercontrol.json")
    var config: LayerControlConfig? = null

    init {
        if(!configFile.parentFile.exists()) {
            configFile.parentFile.mkdirs()
        }
        if(!configFile.exists()) {
            configFile.createNewFile()
            configFile.writeText(json.encodeToString(LayerControlConfig.DEFAULT))
        }
        runCatching {
            config = json.decodeFromString(configFile.readText())
        }
    }

}