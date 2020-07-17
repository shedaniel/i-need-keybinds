package me.shedaniel.ink.api;

public interface KeyBindingHooks {
    
    void ink_setPressed(boolean pressed);
    
    void ink_setTimesPressed(int timesPressed);
    
    int ink_getTimesPressed();
    
}
