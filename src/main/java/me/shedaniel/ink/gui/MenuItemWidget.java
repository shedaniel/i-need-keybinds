package me.shedaniel.ink.gui;

import me.shedaniel.ink.ConfigObject;
import me.shedaniel.ink.HudState;
import me.shedaniel.ink.INeedKeybinds;
import net.minecraft.client.gui.Element;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static me.shedaniel.ink.INeedKeybinds.*;

public class MenuItemWidget extends Widget {
    
    private Rectangle bounds;
    private HudState hudState;
    private int id;
    
    public MenuItemWidget(Rectangle bounds, HudState hudState, int id) {
        this.bounds = bounds;
        this.hudState = hudState;
        this.id = id;
    }
    
    @Override
    public void render(float var3) {
        if (INeedKeybinds.hudState == hudState || (INeedKeybinds.hudState == HudState.HIDDEN && lastState == hudState)) {
            Rectangle bounds = getBounds();
            float alpha = INeedKeybinds.hudWidget.getAlpha();
            Rectangle title = new Rectangle((int) (10 - (1 - alpha) * (WIDTH + 10)), bounds.y, WIDTH, 16);
            ConfigObject.CategoryObject categoryObject = configObject.categories.get(id);
            fill(title.x, title.y, title.x + 16, title.y + title.height, color(categoryObject.name == null ? 50 : 0, 0, 0, (int) (200f * alpha)));
            fill(title.x + 21, title.y, title.x + title.width, title.y + title.height, color(categoryObject.name == null ? 50 : 0, 0, 0, (int) (200f * alpha)));
            font.drawWithShadow((id + 1) + "", bounds.x + 5, bounds.y + 4, 16777215);
            font.drawWithShadow(categoryObject.name == null ? "Not Set" : categoryObject.name, bounds.x + 25, bounds.y + 4, 16777215);
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
