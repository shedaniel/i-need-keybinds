package me.shedaniel.ink.cloth;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class ModMenuCombat implements ModMenuApi {
    
    @Override
    public String getModId() {
        return "i-need-keybinds";
    }
    
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ClothConfigCombat::getScreen;
    }
    
}
