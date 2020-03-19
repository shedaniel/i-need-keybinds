package me.shedaniel.ink.mixin;

import me.shedaniel.ink.HudState;
import me.shedaniel.ink.INeedKeybinds;
import me.shedaniel.ink.api.KeyBindingHooks;
import me.shedaniel.ink.api.KeyFunction;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding implements KeyBindingHooks {
    
    @Shadow private boolean pressed;
    
    @Shadow private int timesPressed;
    
    @Shadow
    public abstract String getId();
    
    @Override
    public void ink_setPressed(boolean pressed) {
        this.pressed = pressed;
    }
    
    @Override
    public void ink_setTimesPressed(int timesPressed) {
        this.timesPressed = timesPressed;
    }
    
    @Override
    public int ink_getTimesPressed() {
        return timesPressed;
    }
    
    @Inject(method = "isPressed", at = @At("HEAD"), cancellable = true)
    public void isPressed(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (INeedKeybinds.pressed.stream().anyMatch(keyBinding -> keyBinding.getId().equalsIgnoreCase(getId())))
            callbackInfo.setReturnValue(true);
    }
    
    @Inject(method = "matchesKey", at = @At("HEAD"), cancellable = true)
    public void matchesKey(int int_1, int int_2, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (getId().startsWith("key.i-need-keybinds.number_"))
            return;
        for (int i = 0; i < 8; i++) {
            if (INeedKeybinds.numbers[i].matchesKey(int_1, int_2)) {
                if (INeedKeybinds.hudState == HudState.CATEGORY && i < INeedKeybinds.configObject.categories.get(INeedKeybinds.category).getFunctions().size()) {
                    KeyFunction keyFunction = INeedKeybinds.configObject.categories.get(INeedKeybinds.category).getFunctions().get(i);
                    if (!keyFunction.hasCommand())
                        if (keyFunction.getKeybind().getId().equalsIgnoreCase(getId()))
                            callbackInfo.setReturnValue(true);
                }
            }
        }
    }
}
