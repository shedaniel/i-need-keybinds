package me.shedaniel.ink.gui;

import me.shedaniel.ink.ConfigObject;
import me.shedaniel.ink.HudState;
import me.shedaniel.ink.INeedKeybinds;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;

import static me.shedaniel.ink.INeedKeybinds.*;
import static me.shedaniel.ink.gui.KeybindingItemWidget.SPIN;

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
    public void render(MatrixStack matrices, float var3, long ms) {
        if (INeedKeybinds.hudState == hudState || (INeedKeybinds.hudState == HudState.HIDDEN && lastState == hudState)) {
            Rectangle bounds = getBounds();
            float alpha = INeedKeybinds.hudWidget.getAlpha();
            Rectangle title = new Rectangle((int) (10 - (1 - alpha) * (WIDTH + 10)), bounds.y, WIDTH, 16);
            Window window = MinecraftClient.getInstance().getWindow();
            ConfigObject.CategoryObject categoryObject = configObject.categories.get(id);
            fill(matrices, title.x, title.y, title.x + 16, title.y + title.height, color(categoryObject.name == null ? 50 : 0, 0, 0, (int) (200f * alpha)));
            fill(matrices, title.x + 21, title.y, title.x + title.width, title.y + title.height, color(categoryObject.name == null ? 50 : 0, 0, 0, (int) (200f * alpha)));
            textRenderer.drawWithShadow(matrices, (id + 1) + "", bounds.x + 5, bounds.y + 4, 16777215);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(Math.round(window.getHeight() * ((title.x + 21f) / window.getScaledHeight())), Math.round(window.getHeight() - Math.round(window.getHeight() * (16f / window.getScaledHeight())) - Math.round(window.getHeight() * (title.getY() / window.getScaledHeight()))), Math.round(window.getWidth() * ((title.width - 21f) / window.getScaledWidth())), Math.round(window.getHeight() * (16f / window.getScaledHeight())));
            String s = categoryObject.name == null ? I18n.translate("text.ink.not-set") : categoryObject.name;
            int offset = 0;
            if (textRenderer.getStringWidth(s) > title.width - 21) {
                offset = textRenderer.getStringWidth(s) + 8 - (title.width - 21);
                offset *= ms % SPIN >= (SPIN / 2) ? 1 - ((ms % SPIN) - (SPIN / 2)) / (SPIN / 2f) : Math.min(ms % SPIN, (SPIN / 2)) / (SPIN / 2f);
            }
            textRenderer.drawWithShadow(matrices, s, bounds.x + 25 - offset, bounds.y + 4, 16777215);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
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
