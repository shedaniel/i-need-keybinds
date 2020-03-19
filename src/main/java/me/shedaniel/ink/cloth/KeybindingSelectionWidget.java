package me.shedaniel.ink.cloth;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import me.shedaniel.ink.api.KeyFunction;
import me.shedaniel.ink.impl.KeyFunctionImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class KeybindingSelectionWidget extends DynamicElementListWidget<KeybindingSelectionWidget.Entry> {
    
    public KeybindingSelectionWidget(MinecraftClient client, int width, int height, int top, int bottom, Identifier backgroundLocation) {
        super(client, width, height, top, bottom, backgroundLocation);
    }
    
    @Override
    public int getItemWidth() {
        return width - 100;
    }
    
    @Override
    protected int getScrollbarPosition() {
        return width - 50 + 6;
    }
    
    public void addEntry(Entry entry) {
        addItem(entry);
    }
    
    public static abstract class Entry extends DynamicElementListWidget.ElementEntry<Entry> {
        @Override
        public int getItemHeight() {
            return 20;
        }
    }
    
    public static class CategoryEntry extends Entry {
        private String text;
        
        public CategoryEntry(String string_2) {
            this.text = I18n.translate(string_2);
        }
        
        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
        @Override
        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            TextRenderer font = MinecraftClient.getInstance().textRenderer;
            font.draw(text, x + entryWidth / 2 - font.getStringWidth(text) / 2, y, 16777215);
        }
        
        @Override
        public List<? extends Element> children() {
            return Collections.emptyList();
        }
    }
    
    public static class KeyBindingEntry extends Entry {
        private KeyBinding keyBinding_1;
        private AtomicInteger entryWidth;
        private AbstractButtonWidget widget;
        
        public KeyBindingEntry(Consumer<Optional<KeyFunction>> consumer, KeyBinding keyBinding_1, AtomicInteger entryWidth) {
            this.keyBinding_1 = keyBinding_1;
            this.entryWidth = entryWidth;
            this.widget = new ButtonWidget(0, 0, 100, 20, I18n.translate("text.ink.select"), var1 -> {
                consumer.accept(Optional.ofNullable(new KeyFunctionImpl(keyBinding_1)));
            });
        }
        
        @Override
        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            TextRenderer font = MinecraftClient.getInstance().textRenderer;
            font.draw(I18n.translate(keyBinding_1.getId()), x, y, 16777215);
            widget.y = y - 5;
            widget.x = x + entryWidth - widget.getWidth();
            widget.render(mouseX, mouseY, delta);
        }
        
        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(widget);
        }
    }
    
    public static class CommandEntry extends Entry {
        private TextFieldWidget textField;
        private AbstractButtonWidget widget;
        
        public CommandEntry(Consumer<Optional<KeyFunction>> consumer, String s) {
            int i = MinecraftClient.getInstance().textRenderer.getStringWidth(I18n.translate("text.ink.select")) + 8;
            this.widget = new ButtonWidget(0, 0, i, 20, I18n.translate("text.ink.select"), var1 -> {
                consumer.accept(Optional.ofNullable(new KeyFunctionImpl("cmd:" + textField.getText())));
            });
            this.textField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 150 - i - 10, 16, "");
            this.textField.setMaxLength(1000000);
            this.textField.setText(s);
        }
        
        @Override
        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            TextRenderer font = MinecraftClient.getInstance().textRenderer;
            font.draw(I18n.translate("text.category.ink.commands"), x, y, 16777215);
            widget.y = y - 7;
            widget.x = x + entryWidth - widget.getWidth();
            widget.active = textField.getText().startsWith("/");
            textField.y = y - 5;
            textField.x = widget.x - 10 - textField.getWidth();
            widget.render(mouseX, mouseY, delta);
            textField.render(mouseX, mouseY, delta);
        }
        
        @Override
        public List<? extends Element> children() {
            return Lists.newArrayList(widget, textField);
        }
    }
    
}
