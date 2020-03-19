package me.shedaniel.ink.cloth;

import me.shedaniel.clothconfig2.gui.entries.AbstractListListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.ink.api.KeyFunction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.resource.language.I18n;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class KeybindingListEntry extends AbstractListListEntry<KeyFunction, KeybindingListEntry.KeybindingListCell, KeybindingListEntry> {
    
    private StringListEntry entry;
    
    public KeybindingListEntry(StringListEntry entry, String fieldName, List<KeyFunction> values, boolean defaultExpended, Consumer<List<KeyFunction>> saveConsumer, String resetButtonKey, boolean requiresRestart) {
//        super(fieldName, null, Collections::emptyList, baseListEntry -> new KeybindingListCell(null, (KeybindingListEntry) baseListEntry), saveConsumer, resetButtonKey, requiresRestart, false, false);
        super(fieldName, values, defaultExpended, null, saveConsumer, Collections::emptyList, resetButtonKey, requiresRestart, false, false, (value, self) -> new KeybindingListCell(value, self));
//        for (KeyFunction keyFunction : value)
//            cells.add(new KeybindingListCell(keyFunction, this));
//        this.widgets.addAll(cells);
//        expanded = defaultExpended;
        this.entry = entry;
    }
    
    @Override
    public KeybindingListEntry self() {
        return this;
    }
    
    @Override
    public Optional<String> getError() {
        Optional<String> error = super.getError();
        if (error.isPresent())
            return error;
        if (entry != null && !cells.isEmpty()) {
            if (entry.getValue() == null || entry.getValue().isEmpty() || entry.getValue().equalsIgnoreCase("null"))
                return Optional.of(I18n.translate("error.category.need_name"));
        }
        return Optional.empty();
    }

//    @Override
//    protected KeybindingListCell getFromValue(KeyFunction value) {
//        return new KeybindingListCell(value, this);
//    }

//    @Override
//    public List<KeyFunction> getValue() {
//        return cells.stream().map(keybindingListCell -> keybindingListCell.keyFunction).filter(Objects::nonNull).collect(Collectors.toList());
//    }
    
    public static class KeybindingListCell extends AbstractListListEntry.AbstractListCell<KeyFunction, KeybindingListCell, KeybindingListEntry> {
        
        private KeyFunction keyFunction;
        private AbstractButtonWidget widget;
        
        public KeybindingListCell(KeyFunction keyFunction, KeybindingListEntry keybindingListEntry) {
            super(keyFunction, keybindingListEntry);
            this.keyFunction = keyFunction;
            widget = new AbstractPressableButtonWidget(0, 0, 100, 18, "") {
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
                        keybindingListEntry.getScreen().setEdited(true, keybindingListEntry.isRequiresRestart());
                    }, keyFunction == null || keyFunction.isNull() || !keyFunction.hasCommand() ? "" : keyFunction.getCommand()));
                }
            };
        }
        
        @Override
        public KeyFunction getValue() {
            return keyFunction;
        }
        
        @Override
        public Optional<String> getError() {
            return Optional.empty();
        }
        
        @Override
        public int getCellHeight() {
            return 20;
        }
        
        @Override
        public void render(int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            widget.setWidth(entryWidth - 12);
            widget.x = x;
            widget.y = y + 1;
            widget.active = listListEntry.isEditable();
            widget.setMessage(keyFunction == null || keyFunction.isNull() ? I18n.translate("config.category.key.not-set") : I18n.translate("config.category.key", keyFunction.getFormattedName()));
            widget.render(mouseX, mouseY, delta);
        }
        
        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(widget);
        }
    }
}
