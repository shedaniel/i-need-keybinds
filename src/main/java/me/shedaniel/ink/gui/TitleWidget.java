package me.shedaniel.ink.gui;

import me.shedaniel.ink.HudState;
import me.shedaniel.ink.INeedKeybinds;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.List;

import static me.shedaniel.ink.INeedKeybinds.WIDTH;
import static me.shedaniel.ink.INeedKeybinds.lastState;

public class TitleWidget extends Widget {
    
    protected HudState hudState;
    private Rectangle bounds;
    
    public TitleWidget(Rectangle bounds, HudState hudState) {
        this.bounds = bounds;
        this.hudState = hudState;
    }
    
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    public void render(MatrixStack matrices, float var3, long ms) {
        if (INeedKeybinds.hudState == hudState || (INeedKeybinds.hudState == HudState.HIDDEN && lastState == hudState)) {
            Rectangle bounds = getBounds();
            String s = "Select Category";
            textRenderer.drawWithShadow(matrices, s, bounds.x + bounds.width / 2 - textRenderer.getStringWidth(s) / 2, bounds.y + 4, 16777215);
        }
    }
    
    public float getProgress() {
        return MathHelper.clamp(INeedKeybinds.hudWidget.getAlpha(), 0, 1);
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (bounds.x - (1 - getProgress()) * (WIDTH + 10)), bounds.y, bounds.width, bounds.y + bounds.height);
    }
    
    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }
    
}
