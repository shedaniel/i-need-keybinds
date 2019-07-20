package me.shedaniel.ink.mixin;

import me.shedaniel.ink.INeedKeybinds;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    
    @Inject(method = "render", at = @At(value = "INVOKE",
                                        target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay()V",
                                        ordinal = 0))
    public void render(float float_1, CallbackInfo info) {
        INeedKeybinds.renderHud(float_1);
    }
    
}
