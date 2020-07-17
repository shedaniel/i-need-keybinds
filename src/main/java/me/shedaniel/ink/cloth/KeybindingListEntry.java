package me.shedaniel.ink.cloth;

import me.shedaniel.clothconfig2.gui.entries.AbstractListListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.ink.api.KeyFunction;
import me.shedaniel.ink.impl.KeyFunctionImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class KeybindingListEntry extends AbstractListListEntry<KeyFunction, KeybindingListEntry.KeybindingListCell, KeybindingListEntry> {
    
    private StringListEntry entry;
    
    public KeybindingListEntry(StringListEntry entry, String fieldName, List<KeyFunction> values, boolean defaultExpended, Consumer<List<KeyFunction>> saveConsumer, Text resetButtonKey, boolean requiresRestart) {
        super(new TranslatableText(fieldName), values, defaultExpended, null, saveConsumer, Collections::emptyList, resetButtonKey, requiresRestart, false, false, KeybindingListCell::new);
        this.entry = entry;
    }
    
    @Override
    public KeybindingListEntry self() {
        return this;
    }
    
    @Override
    public Optional<Text> getError() {
        Optional<Text> error = super.getError();
        if (error.isPresent())
            return error;
        if (entry != null && !cells.isEmpty()) {
            if (entry.getValue() == null || entry.getValue().isEmpty() || entry.getValue().equalsIgnoreCase("null"))
                return Optional.of(new TranslatableText("error.category.need_name"));
        }
        return Optional.empty();
    }
    
    public static class KeybindingListCell extends AbstractListListEntry.AbstractListCell<KeyFunction, KeybindingListCell, KeybindingListEntry> {
        private final KeyFunction ogFunction;
        private KeyFunction keyFunction;
        private AbstractButtonWidget widget;
        
        public KeybindingListCell(KeyFunction keyFunction, KeybindingListEntry keybindingListEntry) {
            super(keyFunction, keybindingListEntry);
            keyFunction = Optional.ofNullable(keyFunction).orElse(new KeyFunctionImpl()).copy();
            this.ogFunction = keyFunction.copy();
            this.keyFunction = keyFunction;
            widget = new AbstractPressableButtonWidget(0, 0, 100, 20, Text.method_30163("")) {
                @Override
                public void onPress() {
                    Screen screen = MinecraftClient.getInstance().currentScreen;
                    MinecraftClient.getInstance().openScreen(new KeybindingSelectionScreen(function -> {
                        MinecraftClient.getInstance().openScreen(screen);
                        if (function == null) {
                            listListEntry.cells.remove(KeybindingListCell.this);
                            listListEntry.widgets.remove(KeybindingListCell.this);
                            return;
                        }
                        KeybindingListCell.this.keyFunction = function.orElse(null);
                    }, KeybindingListCell.this.keyFunction == null || KeybindingListCell.this.keyFunction.isNull() || !KeybindingListCell.this.keyFunction.hasCommand() ? "" : KeybindingListCell.this.keyFunction.getCommand()));
                }
            };
        }
        
        @Override
        public boolean isEdited() {
            return super.isEdited() || Objects.equals(keyFunction, ogFunction);
        }
        
        @Override
        public KeyFunction getValue() {
            return keyFunction;
        }
        
        @Override
        public Optional<Text> getError() {
            return Optional.empty();
        }
        
        @Override
        public int getCellHeight() {
            return 20;
        }
        
        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            widget.setWidth(entryWidth - 12);
            widget.x = x;
            widget.y = y + 1;
            widget.active = listListEntry.isEditable();
            widget.setMessage(keyFunction == null || keyFunction.isNull() ? new TranslatableText("config.category.key.not-set") : new TranslatableText("config.category.key", keyFunction.getFormattedName()));
            widget.render(matrices, mouseX, mouseY, delta);
        }
        
        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(widget);
        }
    }
}
