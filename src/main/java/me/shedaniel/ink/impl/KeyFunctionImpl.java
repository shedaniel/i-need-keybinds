package me.shedaniel.ink.impl;

import me.shedaniel.ink.INeedKeybinds;
import me.shedaniel.ink.api.KeyFunction;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;

public class KeyFunctionImpl implements KeyFunction {
    
    private String keyBinding;
    private String command;
    
    public KeyFunctionImpl(String configStr) {
        if (configStr.startsWith("cmd:"))
            this.command = configStr.substring(4);
        this.keyBinding = configStr;
    }
    
    public KeyFunctionImpl(KeyBinding keyBinding) {
        this.keyBinding = keyBinding.getId();
    }
    
    @Override
    public KeyBinding getKeybind() {
        return INeedKeybinds.getKeyBindingByString(keyBinding);
    }
    
    @Override
    public String getCommand() {
        return command;
    }
    
    @Override
    public boolean hasCommand() {
        return command != null;
    }
    
    @Override
    public boolean isNull() {
        return !hasCommand() && getKeybind() == null;
    }
    
    @Override
    public String getFormattedName() {
        return isNull() ? "Not Set" : hasCommand() ? "Command: " + getCommand() : I18n.translate(getKeybind().getId());
    }
    
    @Override
    public String toString() {
        return hasCommand() ? "cmd:" + getCommand() : getKeybind().getId();
    }
    
}
