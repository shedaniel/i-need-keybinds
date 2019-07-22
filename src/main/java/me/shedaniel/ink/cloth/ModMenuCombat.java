package me.shedaniel.ink.cloth;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

public class ModMenuCombat implements ModMenuApi {
    
    @Override
    public String getModId() {
        return "i-need-keybinds";
    }
    
    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return ClothConfigCombat::getScreen;
    }
    
}
