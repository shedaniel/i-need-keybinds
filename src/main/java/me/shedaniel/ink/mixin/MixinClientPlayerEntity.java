package me.shedaniel.ink.mixin;

import me.shedaniel.ink.cloth.ClothConfigCombat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    
    @Shadow @Final protected MinecraftClient client;
    
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        String[] split = message.toLowerCase().split(" ");
        if (split.length > 0 && split[0].contentEquals("/inkconfig")) {
            client.openScreen(ClothConfigCombat.getScreen(client.currentScreen instanceof ChatScreen ? null : client.currentScreen));
            ci.cancel();
        }
    }
    
}