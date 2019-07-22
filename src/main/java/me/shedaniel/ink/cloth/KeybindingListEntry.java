package me.shedaniel.ink.cloth;

import me.shedaniel.clothconfig2.gui.entries.BaseListCell;
import me.shedaniel.clothconfig2.gui.entries.BaseListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.ink.INeedKeybinds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KeybindingListEntry extends BaseListEntry<String, KeybindingListEntry.KeybindingListCell> {
    
    private StringListEntry entry;
    
    public KeybindingListEntry(StringListEntry entry, String fieldName, List<String> value, boolean defaultExpended, Consumer<List<String>> saveConsumer, String resetButtonKey, boolean requiresRestart) {
        super(fieldName, null, Collections::emptyList, baseListEntry -> new KeybindingListCell(null, (KeybindingListEntry) baseListEntry), saveConsumer, resetButtonKey, requiresRestart);
        for(String str : value)
            cells.add(new KeybindingListCell(INeedKeybinds.getKeysByIdMap().get(str), this));
        this.widgets.addAll(cells);
        expended = defaultExpended;
        this.entry = entry;
    }
    
    @Override
    public boolean isDeleteButtonEnabled() {
        return false;
    }
    
    @Override
    public boolean insertInFront() {
        return false;
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
    
    @Override
    protected KeybindingListCell getFromValue(String value) {
        return new KeybindingListCell(INeedKeybinds.getKeysByIdMap().get(value), this);
    }
    
    @Override
    public List<String> getValue() {
        return cells.stream().map(keybindingListCell -> keybindingListCell.keyBinding).filter(Objects::nonNull).map(KeyBinding::getId).collect(Collectors.toList());
    }
    
    public static class KeybindingListCell extends BaseListCell {
        
        private KeyBinding keyBinding;
        private KeybindingListEntry listEntry;
        private AbstractButtonWidget widget;
        
        public KeybindingListCell(KeyBinding keyBinding, KeybindingListEntry keybindingListEntry) {
            this.keyBinding = keyBinding;
            this.listEntry = keybindingListEntry;
            widget = new AbstractPressableButtonWidget(0, 0, 100, 18, "") {
                @Override
                public void onPress() {
                    Screen screen = MinecraftClient.getInstance().currentScreen;
                    MinecraftClient.getInstance().openScreen(new KeybindingSelectionScreen(keyBinding1 -> {
                        MinecraftClient.getInstance().openScreen(screen);
                        if (keyBinding1 == null) {
                            listEntry.cells.remove(KeybindingListCell.this);
                            listEntry.widgets.remove(KeybindingListCell.this);
                            keybindingListEntry.getScreen().setEdited(true, keybindingListEntry.isRequiresRestart());
                            return;
                        }
                        KeybindingListCell.this.keyBinding = keyBinding1.orElse(null);
                    }));
                }
            };
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
            widget.active = listEntry.isEditable();
            widget.setMessage(keyBinding == null ? I18n.translate("config.category.key.not-set") : I18n.translate("config.category.key", I18n.translate(keyBinding.getId())));
            widget.render(mouseX, mouseY, delta);
        }
        
        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(widget);
        }
    }
}
