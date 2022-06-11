package me.obsilabor.layercontrol.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class CustomTurtleHelmetLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final A outerModel;

    public CustomTurtleHelmetLayer(RenderLayerParent<T, M> renderLayerParent, A humanoidModel2) {
        super(renderLayerParent);
        this.outerModel = humanoidModel2;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        this.renderArmorPiece(poseStack, multiBufferSource, EquipmentSlot.HEAD, i, outerModel);
    }

    private void renderArmorPiece(PoseStack matrices, MultiBufferSource vertexConsumers, EquipmentSlot armorSlot, int light, A model) {
        ItemStack itemStack = new ItemStack(Items.TURTLE_HELMET);
        ArmorItem armorItem = (ArmorItem)itemStack.getItem();
        if (armorItem.getSlot() == armorSlot) {
            this.getParentModel().copyPropertiesTo(model);
            this.setPartVisibility(model, armorSlot);
            boolean foil = itemStack.hasFoil();
            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(this.getArmorLocation(armorItem, false, null)), false, foil);
            model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    protected void setPartVisibility(A bipedModel, EquipmentSlot slot) {
        bipedModel.setAllVisible(false);
        switch (slot) {
            case HEAD:
                bipedModel.head.visible = true;
                bipedModel.hat.visible = true;
                break;
            case CHEST:
                bipedModel.body.visible = true;
                bipedModel.rightArm.visible = true;
                bipedModel.leftArm.visible = true;
                break;
            case LEGS:
                bipedModel.body.visible = true;
                bipedModel.rightLeg.visible = true;
                bipedModel.leftLeg.visible = true;
                break;
            case FEET:
                bipedModel.rightLeg.visible = true;
                bipedModel.leftLeg.visible = true;
        }

    }

    private ResourceLocation getArmorLocation(ArmorItem item, boolean legs, @Nullable String overlay) {
        String materialName = item.getMaterial().getName();
        String string = "textures/models/armor/" + materialName + "_layer_" + (legs ? 2 : 1) + (overlay == null ? "" : "_" + overlay) + ".png";
        return ARMOR_LOCATION_CACHE.computeIfAbsent(string, ResourceLocation::new);
    }
}
