package me.shedaniel.ink;

import com.google.common.collect.Lists;
import me.shedaniel.ink.api.KeyFunction;
import me.shedaniel.ink.impl.KeyFunctionImpl;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigObject {
    public float animate = 300;
    public List<CategoryObject> categories;
    
    public ConfigObject() {
        this.categories = Lists.newArrayList(new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject(), new CategoryObject());
    }
    
    public static class CategoryObject {
        public String name;
        private List<String> keybinds;
        
        public List<KeyFunction> getFunctions() {
            return keybinds.stream().map(KeyFunctionImpl::new).collect(Collectors.toList());
        }
        
        public List<String> getKeybinds() {
            return keybinds;
        }
        
        public CategoryObject() {
            name = null;
            keybinds = Lists.newArrayList();
        }
    }
}
