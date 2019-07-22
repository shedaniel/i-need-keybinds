package me.shedaniel.ink;

import me.shedaniel.cloth.hooks.ClothClientHooks;
import me.shedaniel.ink.api.KeyBindingHooks;
import me.shedaniel.ink.api.KeyFunction;
import me.shedaniel.ink.gui.HudWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class INeedKeybinds implements ClientModInitializer {
    
    public static final int WIDTH = 150;
    public static FabricKeyBinding toggleHud, numbers[] = new FabricKeyBinding[8];
    public static long lastSwitch = -1;
    public static int category = -1;
    public static HudState hudState = HudState.HIDDEN;
    public static HudState lastState = HudState.HIDDEN;
    public static HudWidget hudWidget;
    public static ConfigObject configObject;
    public static ConfigManager configManager;
    public static List<KeyBinding> pressed = new ArrayList<>();
    
    public static float getAnimate() {
        return configObject.animate;
    }
    
    public static void renderHud(float delta) {
        if (hudWidget == null)
            hudWidget = new HudWidget();
        hudWidget.render(delta, System.currentTimeMillis());
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
        return (float) (1f * (-Math.pow(2, -10 * t / 1f) + 1));
    }
    
    private static Map<String, KeyBinding> getKeysByIdMap() {
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
    
    public static KeyBinding getKeyBindingByString(String s) {
        return getKeysByIdMap().get(s);
    }
    
    @Override
    public void onInitializeClient() {
        configManager = new ConfigManager();
        String category = "category.ink.keybinds";
        KeyBindingRegistry.INSTANCE.addCategory(category);
        KeyBindingRegistry.INSTANCE.register(toggleHud = FabricKeyBinding.Builder.create(new Identifier("i-need-keybinds", "toggle_hud"), InputUtil.Type.KEYSYM, 320, category).build());
        for(int i = 0; i < 8; i++)
            KeyBindingRegistry.INSTANCE.register(numbers[i] = FabricKeyBinding.Builder.create(new Identifier("i-need-keybinds", "number_" + i), InputUtil.Type.KEYSYM, 321 + i, category).build());
        List<KeyBinding> unPress = new ArrayList<>();
        ClothClientHooks.HANDLE_INPUT.register(client -> {
            if (!unPress.isEmpty()) {
                unPress.forEach(keyBinding -> ((KeyBindingHooks) keyBinding).ink_setPressed(false));
                unPress.clear();
            }
            List<KeyBinding> pressedKeys = new ArrayList<>();
            while (toggleHud.wasPressed())
                if (hudState == HudState.HIDDEN)
                    switchState(HudState.GENERAL);
                else if (hudState == HudState.CATEGORY)
                    switchState(HudState.GENERAL);
                else
                    switchState(HudState.HIDDEN);
            for(int i = 0; i < 8; i++) {
                boolean a = false;
                boolean b = false;
                while (numbers[i].wasPressed()) {
                    if (hudState == HudState.GENERAL && configObject.categories.get(i).name != null) {
                        if (!b) {
                            switchState(HudState.CATEGORY);
                            switchCategory(i);
                        }
                        a = b = true;
                    } else if (hudState == HudState.CATEGORY && i < configObject.categories.get(INeedKeybinds.category).getFunctions().size()) {
                        KeyFunction keyFunction = configObject.categories.get(INeedKeybinds.category).getFunctions().get(i);
                        if (!keyFunction.hasCommand() && keyFunction.getKeybind() != null) {
                            if (!b) {
                                KeyBinding keybind = keyFunction.getKeybind();
                                pressedKeys.add(keybind);
                                unPress.add(keybind);
                                ((KeyBindingHooks) keybind).ink_setPressed(true);
                                ((KeyBindingHooks) keybind).ink_setTimesPressed(((KeyBindingHooks) keybind).ink_getTimesPressed() + 1);
                            }
                            a = b = true;
                        } else if (keyFunction.hasCommand()) {
                            if (!b) {
                                String command = keyFunction.getCommand();
                                MinecraftClient.getInstance().player.sendChatMessage(command);
                            }
                            a = b = true;
                        }
                    }
                }
                if (a)
                    break;
            }
            pressed = pressedKeys.stream().distinct().collect(Collectors.toList());
        });
    }
    
}
