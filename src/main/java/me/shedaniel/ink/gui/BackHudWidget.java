package me.shedaniel.ink.gui;

import me.shedaniel.ink.HudState;
import me.shedaniel.ink.INeedKeybinds;
import net.minecraft.client.gui.Element;
import net.minecraft.client.resource.language.I18n;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static me.shedaniel.ink.INeedKeybinds.*;

public class BackHudWidget extends Widget {
    
    protected HudState hudState;
    private Rectangle bounds;
    
    public BackHudWidget(Rectangle bounds, HudState hudState) {
        this.bounds = bounds;
        this.hudState = hudState;
    }
    
    @Override
    public void render(float var3) {
        if (INeedKeybinds.hudState == hudState || (INeedKeybinds.hudState == HudState.HIDDEN && lastState == hudState)) {
            Rectangle bounds = getBounds();
            float alpha = INeedKeybinds.hudWidget.getAlpha();
            Rectangle title = new Rectangle((int) (10 - (1 - alpha) * (WIDTH + 10)), bounds.y, WIDTH, 16);
            fill(title.x, title.y, title.x + title.width, title.y + title.height, color(0, 0, 0, (int) (200f * alpha)));
            drawCenteredString(font, "Press " + I18n.translate(toggleHud.getBoundKey().getName()) + " to HIDE", bounds.x + bounds.width / 2, bounds.y + 4, 16777215);
        }
    }
    
    public float getProgress() {
        return INeedKeybinds.hudWidget.getAlpha();
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