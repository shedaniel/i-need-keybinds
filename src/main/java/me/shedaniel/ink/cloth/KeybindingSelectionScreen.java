package me.shedaniel.ink.cloth;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class KeybindingSelectionScreen extends Screen {
    
    protected KeybindingSelectionWidget widget;
    protected Consumer<Optional<KeyBinding>> consumer;
    
    public KeybindingSelectionScreen(Consumer<Optional<KeyBinding>> consumer) {
        super(new TranslatableText("title.ink.select_keybind"));
        this.consumer = consumer;
    }
    
    @Override
    protected void init() {
        super.init();
        widget = new KeybindingSelectionWidget(minecraft, width, height, 23, height - 32, DrawableHelper.BACKGROUND_LOCATION);
        AtomicInteger entryWidth = new AtomicInteger(0);
        String string_1 = null;
        KeyBinding[] clone = minecraft.options.keysAll.clone();
        KeyBinding[] var5 = minecraft.options.keysAll.clone();
        int var6 = clone.length;
        for(int var7 = 0; var7 < var6; ++var7) {
            KeyBinding keyBinding_1 = var5[var7];
            String string_2 = keyBinding_1.getCategory();
            if (string_2.equalsIgnoreCase("category.ink.keybinds"))
                continue;
            if (!string_2.equals(string_1)) {
                string_1 = string_2;
                widget.addEntry(new KeybindingSelectionWidget.CategoryEntry(string_2));
            }
            int int_1 = minecraft.textRenderer.getStringWidth(I18n.translate(keyBinding_1.getId()));
            if (int_1 > entryWidth.get())
                entryWidth.set(int_1);
            widget.addEntry(new KeybindingSelectionWidget.KeyBindingEntry(consumer, keyBinding_1, entryWidth));
        }
        children.add(widget);
        addButton(new ButtonWidget(width / 2 - 155, height - 25, 150, 20, I18n.translate("text.ink.cancel_select"), var1 -> {
            consumer.accept(Optional.empty());
        }));
        addButton(new ButtonWidget(width / 2 - 155 + 160, height - 25, 150, 20, I18n.translate("text.ink.delete_entry"), var1 -> {
            consumer.accept(null);
        }));
    }
    
    @Override
    public void render(int int_1, int int_2, float float_1) {
        renderDirtBackground(0);
        widget.render(int_1, int_2, float_1);
        drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 8, 16777215);
        super.render(int_1, int_2, float_1);
    }
}