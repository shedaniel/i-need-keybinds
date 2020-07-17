package me.shedaniel.ink.impl;

import com.google.common.base.Objects;
import me.shedaniel.ink.INeedKeybinds;
import me.shedaniel.ink.api.KeyFunction;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;

public class KeyFunctionImpl implements KeyFunction {
    
    private String keyBinding;
    private String command;
    
    public KeyFunctionImpl() {
    }
    
    public KeyFunctionImpl(String configStr) {
        if (configStr != null) {
            if (configStr.startsWith("cmd:"))
                this.command = configStr.substring(4);
            this.keyBinding = configStr;
        }
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
        return hasCommand() ? "cmd:" + getCommand() : keyBinding;
    }
    
    @Override
    public KeyFunction copy() {
        return new KeyFunctionImpl(toString());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equal(toString(), o.toString());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(keyBinding, command);
    }
}
