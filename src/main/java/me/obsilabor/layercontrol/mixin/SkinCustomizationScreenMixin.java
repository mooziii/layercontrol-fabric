package me.obsilabor.layercontrol.mixin;

import me.obsilabor.layercontrol.config.ClothConfigManager;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkinCustomizationScreen.class)
public abstract class SkinCustomizationScreenMixin extends Screen {

    protected SkinCustomizationScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void addCustomButton(CallbackInfo ci) {
        addRenderableWidget(new Button(0, 0, 200, 20, Component.translatable("layercontrol.config"), (button) -> this.minecraft.setScreen(ClothConfigManager.INSTANCE.buildScreen())));
    }

}
