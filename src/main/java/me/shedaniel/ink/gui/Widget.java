package me.shedaniel.ink.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.AbstractParentElement;

import java.awt.*;

public abstract class Widget extends AbstractParentElement {
    
    /**
     * The Minecraft Client instance
     */
    protected final MinecraftClient minecraft = MinecraftClient.getInstance();
    /**
     * The font for rendering text
     */
    protected final TextRenderer font = minecraft.textRenderer;
    
    abstract public void render(float var3);
    
    abstract public Shape getBounds();
    
}
