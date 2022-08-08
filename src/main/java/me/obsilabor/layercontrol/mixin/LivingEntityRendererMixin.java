package me.obsilabor.layercontrol.mixin;

import me.obsilabor.layercontrol.config.CustomizableRenderLayer;
import me.obsilabor.layercontrol.render.EmptyRenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @ModifyVariable(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("STORE")
    )
    private RenderLayer cancelRendering(RenderLayer renderLayer, LivingEntity livingEntity) {
        if(CustomizableRenderLayer.Companion.getTargets().contains(renderLayer.getClass())) {
            if(!CustomizableRenderLayer.Companion.getForRenderLayer(renderLayer).shouldRender(livingEntity.getUUID())) {
                return new EmptyRenderLayer(((RenderLayerAccessor) renderLayer).getParentRenderer());
            }
        }
        return renderLayer;
    }
}
