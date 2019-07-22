package me.shedaniel.ink.api;

import net.minecraft.client.options.KeyBinding;

public interface KeyFunction {
    
    KeyBinding getKeybind();
    
    String getCommand();
    
    boolean hasCommand();
    
    boolean isNull();
    
    String getFormattedName();
}
