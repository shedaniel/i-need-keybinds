package me.shedaniel.ink.gui;

import me.shedaniel.ink.HudState;
import me.shedaniel.ink.INeedKeybinds;
import net.minecraft.client.gui.Element;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static me.shedaniel.ink.INeedKeybinds.*;

public class KeybindingItemWidget extends Widget {
    
    private Rectangle bounds;
    private HudState hudState;
    private int id;
    
    public KeybindingItemWidget(Rectangle bounds, HudState hudState, int id) {
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
            List<String> keybinds = configObject.categories.get(category).keybinds;
            KeyBinding keybind = id < keybinds.size() ? INeedKeybinds.getKeysByIdMap().get(keybinds.get(id)) : null;
            fill(title.x, title.y, title.x + 16, title.y + title.height, color(keybind == null ? 50 : 0, 0, 0, (int) (200f * alpha)));
            fill(title.x + 21, title.y, title.x + title.width, title.y + title.height, color(keybind == null ? 50 : 0, 0, 0, (int) (200f * alpha)));
            font.drawWithShadow((id + 1) + "", bounds.x + 5, bounds.y + 4, 16777215);
            font.drawWithShadow(keybind == null ? "Not Set" : I18n.translate(keybind.getId()), bounds.x + 25, bounds.y + 4, 16777215);
        }
    }
    
    public float getProgress() {
        return INeedKeybinds.hudWidget.getAlpha();
        //        return ease(getLinearProgress());
    }
    
    public float getLinearProgress() {
        if (INeedKeybinds.hudState != lastState && lastState == hudState)
            return 1f - Math.min((System.currentTimeMillis() - lastSwitch) / ANIMATE, 1f);
        else if (INeedKeybinds.hudState != lastState && INeedKeybinds.hudState == hudState)
            return Math.min((System.currentTimeMillis() - lastSwitch) / ANIMATE, 1f);
        return 0f;
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
