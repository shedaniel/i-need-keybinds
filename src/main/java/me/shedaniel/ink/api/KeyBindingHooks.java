package me.shedaniel.ink.api;

import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;

public interface KeyBindingHooks {
    
    void ink_setPressed(boolean pressed);
    
    void ink_setTimesPressed(int timesPressed);
    
    int ink_getTimesPressed();
    
}
