package me.shedaniel.ink;

import com.google.common.collect.Lists;

import java.util.List;

public class ConfigObject {
    public float animate = 300;
    public List<CategoryObject> categories;
    
    public ConfigObject() {
        this.categories = Lists.newArrayList(new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject());
    }
    
    public static class CategoryObject {
        public String name;
        public List<String> keybinds;
        
        public CategoryObject() {
            name = null;
            keybinds = Lists.newArrayList();
        }
    }
}
