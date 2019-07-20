package me.shedaniel.ink.gui;

import com.google.common.collect.Lists;
import me.shedaniel.ink.HudState;
import net.minecraft.client.render.GuiLighting;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static me.shedaniel.ink.INeedKeybinds.*;

public class HudWidget extends Widget {
    
    private List<Widget> generalWidgets;
    private List<Widget> categoryWidgets;
    
    public HudWidget() {
        generalWidgets = Lists.newArrayList(new TitleWidget(new Rectangle(10, 10, WIDTH, 16), HudState.GENERAL));
        for(int i = 0; i < 8; i++)
            generalWidgets.add(new MenuItemWidget(new Rectangle(10, 31 + i * 21, WIDTH, 16), HudState.GENERAL, i));
        generalWidgets.add(new BackHudWidget(new Rectangle(10, 31 + 8 * 21, WIDTH, 16), HudState.GENERAL));
        categoryWidgets = Lists.newArrayList(new CategoryTitleWidget(new Rectangle(10, 10, WIDTH, 16), HudState.CATEGORY));
        for(int i = 0; i < 8; i++)
            categoryWidgets.add(new KeybindingItemWidget(new Rectangle(10, 31 + i * 21, WIDTH, 16), HudState.CATEGORY, i));
        categoryWidgets.add(new BackMenuWidget(new Rectangle(10, 31 + 8 * 21, WIDTH, 16), HudState.CATEGORY));
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (10 - (1 - getAlpha()) * (WIDTH + 10)), 10, WIDTH, 16 * 10 + 5 * 9);
    }
    
    @Override
    public void render(float var3) {
        GuiLighting.disable();
        float alpha = getAlpha();
        Rectangle title = new Rectangle((int) (10 - (1 - alpha) * (WIDTH + 10)), 10, WIDTH, 16);
        fill(title.x, title.y, title.x + title.width, title.y + title.height, color(0, 0, 0, (int) (225f * alpha)));
        generalWidgets.forEach(widget -> {
            GuiLighting.disable();
            widget.render(var3);
        });
        categoryWidgets.forEach(widget -> {
            GuiLighting.disable();
            widget.render(var3);
        });
        
    }
    
    float getAlpha() {
        return ease(getLinearAlpha());
    }
    
    private float getLinearAlpha() {
        if (lastState != HudState.HIDDEN && hudState == HudState.HIDDEN)
            return 1f - Math.min((System.currentTimeMillis() - lastSwitch) / ANIMATE, 1f);
        else if (lastState == HudState.HIDDEN && hudState != HudState.HIDDEN)
            return Math.min((System.currentTimeMillis() - lastSwitch) / ANIMATE, 1f);
        return hudState != HudState.HIDDEN ? 1 : 0;
    }
    
    @Override
    public List<Widget> children() {
        if (hudState == HudState.GENERAL)
            return generalWidgets;
        if (hudState == HudState.CATEGORY)
            return categoryWidgets;
        return Collections.emptyList();
    }
    
}
