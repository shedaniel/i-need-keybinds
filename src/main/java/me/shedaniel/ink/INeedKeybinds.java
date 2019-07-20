package me.shedaniel.ink;

import me.shedaniel.cloth.hooks.ClothClientHooks;
import me.shedaniel.ink.api.KeyBindingHooks;
import me.shedaniel.ink.gui.HudWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

public class INeedKeybinds implements ClientModInitializer {
    
    public static final float ANIMATE = 250f;
    public static final int WIDTH = 150;
    public static FabricKeyBinding toggleHud, numbers[] = new FabricKeyBinding[8];
    public static long lastSwitch = -1;
    public static int category = -1;
    public static HudState hudState = HudState.HIDDEN;
    public static HudState lastState = HudState.HIDDEN;
    public static HudWidget hudWidget;
    public static ConfigObject configObject = new ConfigObject();
    
    static {
        ConfigObject.CategoryObject category = configObject.categories.get(0);
        category.name = "Test Category";
        category.keybinds.add("key.screenshot");
    }
    
    public static void renderHud(float delta) {
        if (hudWidget == null)
            hudWidget = new HudWidget();
        hudWidget.render(delta);
    }
    
    public static void switchState(HudState hudState) {
        if (INeedKeybinds.hudState == hudState)
            return;
        lastState = INeedKeybinds.hudState;
        lastSwitch = System.currentTimeMillis();
        INeedKeybinds.hudState = hudState;
    }
    
    public static void switchCategory(int category) {
        INeedKeybinds.category = category;
    }
    
    public static int color(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0);
    }
    
    public static float ease(float t) {
        float sqt = t * t;
        return sqt / (2.0f * (sqt - t) + 1.0f);
        //        return 1 - (--t) * t * t * t;
    }
    
    public static Map<String, KeyBinding> getKeysByIdMap() {
        try {
            Field field = KeyBinding.class.getDeclaredFields()[0];
            if (!field.isAccessible())
                field.setAccessible(true);
            return (Map) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
    
    @Override
    public void onInitializeClient() {
        String category = "category.ink.keybinds";
        KeyBindingRegistry.INSTANCE.addCategory(category);
        KeyBindingRegistry.INSTANCE.register(toggleHud = FabricKeyBinding.Builder.create(new Identifier("i-need-keybinds", "toggle_hud"), InputUtil.Type.KEYSYM, 320, category).build());
        for(int i = 0; i < 8; i++)
            KeyBindingRegistry.INSTANCE.register(numbers[i] = FabricKeyBinding.Builder.create(new Identifier("i-need-keybinds", "number_" + i), InputUtil.Type.KEYSYM, 321 + i, category).build());
        ClothClientHooks.HANDLE_INPUT.register(client -> {
            while (toggleHud.wasPressed())
                if (hudState == HudState.HIDDEN)
                    switchState(HudState.GENERAL);
                else if (hudState == HudState.CATEGORY)
                    switchState(HudState.GENERAL);
                else
                    switchState(HudState.HIDDEN);
            for(int i = 0; i < 8; i++) {
                boolean a = false;
                while (numbers[i].wasPressed()) {
                    if (hudState == HudState.GENERAL && configObject.categories.get(i).name != null) {
                        a = true;
                        switchState(HudState.CATEGORY);
                        switchCategory(i);
                    } else if (hudState == HudState.CATEGORY && i < configObject.categories.get(INeedKeybinds.category).keybinds.size()) {
                        KeyBinding keyBinding = INeedKeybinds.getKeysByIdMap().get(configObject.categories.get(INeedKeybinds.category).keybinds.get(i));
                        if (keyBinding != null) {
                            a = true;
                            ((KeyBindingHooks) keyBinding).ink_setPressed(true);
                            ((KeyBindingHooks) keyBinding).ink_setTimesPressed(((KeyBindingHooks) keyBinding).ink_getTimesPressed() + 1);
                        }
                    }
                }
                if (a)
                    break;
            }
        });
    }
    
}
