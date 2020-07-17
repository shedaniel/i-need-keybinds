package me.shedaniel.ink.gui;

import me.shedaniel.math.Rectangle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Widget extends AbstractParentElement {
    
    /**
     * The Minecraft Client instance
     */
    protected final MinecraftClient client = MinecraftClient.getInstance();
    /**
     * The font for rendering text
     */
    protected final TextRenderer textRenderer = client.textRenderer;
    
    abstract public void render(MatrixStack matrices, float var3, long ms);
    
    abstract public Rectangle getBounds();
    
}
