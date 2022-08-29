package me.obsilabor.layercontrol.config

import me.obsilabor.layercontrol.minecraft
import me.obsilabor.layercontrol.render.CustomBigHeadLayer
import me.obsilabor.layercontrol.render.CustomTurtleHelmetLayer
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.entity.layers.ArrowLayer
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer
import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer
import net.minecraft.world.entity.LivingEntity
import java.util.*

enum class CustomizableRenderLayer(val clazz: Class<*>, var mojang: Boolean = true) {

    // mojang
    HumanoidArmorLayer(net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer::class.java),
    PlayerItemInHandLayer(net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer::class.java),
    ArrowLayer(net.minecraft.client.renderer.entity.layers.ArrowLayer::class.java),
    Deadmau5EarsLayer(net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer::class.java),
    CapeLayer(net.minecraft.client.renderer.entity.layers.CapeLayer::class.java),
    CustomHeadLayer(net.minecraft.client.renderer.entity.layers.CustomHeadLayer::class.java),
    ElytraLayer(net.minecraft.client.renderer.entity.layers.ElytraLayer::class.java),
    ParrotOnShoulderLayer(net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer::class.java),
    SpinAttackEffectLayer(net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer::class.java),
    BeeStingerLayer(net.minecraft.client.renderer.entity.layers.BeeStingerLayer::class.java),
    // custom
    CustomTurtleHelmetLayer(me.obsilabor.layercontrol.render.CustomTurtleHelmetLayer::class.java, false),
    CustomLeftParrotLayer(me.obsilabor.layercontrol.render.CustomLeftParrotLayer::class.java, false),
    CustomRightParrotLayer(me.obsilabor.layercontrol.render.CustomRightParrotLayer::class.java, false),
    CustomBigHeadLayer(me.obsilabor.layercontrol.render.CustomBigHeadLayer::class.java, false),
    CustomTridentRiptideLayer(me.obsilabor.layercontrol.render.CustomTridentRiptideLayer::class.java, false);

    companion object {

        fun <T : LivingEntity, M : HumanoidModel<T>> getForRenderLayer(renderLayer: RenderLayer<T, M>): CustomizableRenderLayer {
            return values().first { it.clazz == renderLayer.javaClass }
        }

        val targets: List<Class<*>>
        get() = values().map { it.clazz }

    }

    fun shouldRender(entityUniqueId: UUID): Boolean {
        return if(ClothConfigManager.config?.renderOnOthers == true) {
            isEnabledFor(entityUniqueId)
        } else {
            if(entityUniqueId == minecraft.player?.uuid) {
                isEnabled
            } else {
                mojang
            }
        }
    }

    val isEnabled: Boolean
    get() = ClothConfigManager.config?.enabledLayers?.get(this) ?: mojang

    fun isEnabledFor(uuid: UUID): Boolean {
        if (minecraft.player?.uuid == uuid) {
            return isEnabled
        }
        return ClothConfigManager.getConfigFor(uuid).enabledLayers[this] ?: mojang
    }
}