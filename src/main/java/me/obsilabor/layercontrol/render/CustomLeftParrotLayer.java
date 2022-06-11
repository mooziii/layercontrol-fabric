package me.obsilabor.layercontrol.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.obsilabor.layercontrol.config.ClothConfigManager;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.player.Player;

public class CustomLeftParrotLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final ParrotModel model;

    public CustomLeftParrotLayer(RenderLayerParent<T, PlayerModel<T>> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new ParrotModel(entityModelSet.bakeLayer(ModelLayers.PARROT));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T player, float f, float g, float h, float j, float k, float l) {
        this.render(poseStack, multiBufferSource, i, player, f, g, k, l, true);
    }

    private void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, T player, float limbAngle, float limbDistance, float headYaw, float headPitch, boolean leftShoulder) {
        float multiplier = ClothConfigManager.INSTANCE.getConfig().getParrotScale();
        matrices.pushPose();
        matrices.translate(leftShoulder ? 0.4000000059604645 : -0.4000000059604645, player.isCrouching() ? -1.2999999523162842 * multiplier: -1.5 * multiplier, 0.0);
        matrices.scale(multiplier, multiplier, multiplier);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.model.renderType(ParrotRenderer.PARROT_LOCATIONS[ClothConfigManager.INSTANCE.getConfig().getParrotVariantLeft()]));
        this.model.renderOnShoulder(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, limbAngle, limbDistance, headYaw, headPitch, player.tickCount);
        matrices.popPose();
    }
}