package me.obsilabor.layercontrol.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import me.obsilabor.layercontrol.config.ClothConfigManager;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SkullBlock;
import java.util.Map;

public class CustomBigHeadLayer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> extends RenderLayer<T, M> {
    private final float scaleX;
    private final float scaleY;
    private final float scaleZ;
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;
    private final ItemInHandRenderer itemInHandRenderer;

    public CustomBigHeadLayer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet, ItemInHandRenderer itemInHandRenderer) {
        this(renderLayerParent, entityModelSet, 1.0F, 1.0F, 1.0F, itemInHandRenderer);
    }

    public CustomBigHeadLayer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet, float f, float g, float h, ItemInHandRenderer itemInHandRenderer) {
        super(renderLayerParent);
        this.scaleX = f;
        this.scaleY = g;
        this.scaleZ = h;
        this.skullModels = SkullBlockRenderer.createSkullRenderers(entityModelSet);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        if(livingEntity instanceof Player) {
            float multiplier = ClothConfigManager.INSTANCE.getConfig().getBigHeadScale();
            poseStack.pushPose();
            poseStack.scale(this.scaleX, this.scaleY, this.scaleZ);
            this.getParentModel().getHead().translateAndRotate(poseStack);
            poseStack.scale(1.1875F * multiplier, -1.1875F * multiplier, -1.1875F * multiplier);
            GameProfile gameProfile = ((Player) livingEntity).getGameProfile();
            poseStack.translate(-0.5, 0.0, -0.5);
            SkullBlock.Type type = SkullBlock.Types.PLAYER;
            SkullModelBase skullModelBase = (SkullModelBase)this.skullModels.get(type);
            RenderType renderType = SkullBlockRenderer.getRenderType(type, gameProfile);
            SkullBlockRenderer.renderSkull((Direction)null, 180.0F, f, poseStack, multiBufferSource, i, skullModelBase, renderType);
            poseStack.popPose();
        }
    }
}
