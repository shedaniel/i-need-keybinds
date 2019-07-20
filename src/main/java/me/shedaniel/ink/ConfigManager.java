package me.shedaniel.ink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ConfigManager {
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile;
    private boolean craftableOnly;
    
    public ConfigManager() {
        this.configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "i-need-keybinds/config.json");
        this.craftableOnly = false;
        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveConfig() throws IOException {
        configFile.getParentFile().mkdirs();
        if (!configFile.exists() && !configFile.createNewFile()) {
            INeedKeybinds.configObject = new ConfigObject();
            return;
        }
        try {
            String result = GSON.toJson(INeedKeybinds.configObject);
            if (!configFile.exists())
                configFile.createNewFile();
            FileOutputStream out = new FileOutputStream(configFile, false);
            
            out.write(result.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            INeedKeybinds.configObject = new ConfigObject();
            return;
        }
    }
    
    public void loadConfig() throws IOException {
        configFile.getParentFile().mkdirs();
        if (!configFile.exists() || !configFile.canRead()) {
            INeedKeybinds.configObject = new ConfigObject();
            saveConfig();
            return;
        }
        boolean failed = false;
        try {
            INeedKeybinds.configObject = GSON.fromJson(new FileReader(configFile), ConfigObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            failed = true;
        }
        if (failed || INeedKeybinds.configObject == null) {
            INeedKeybinds.configObject = new ConfigObject();
        }
        saveConfig();
    }
    
}