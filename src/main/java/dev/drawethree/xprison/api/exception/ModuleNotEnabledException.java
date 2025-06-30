package dev.drawethree.xprison.api.exception;

import dev.drawethree.xprison.api.XPrisonModule;

public class ModuleNotEnabledException extends Exception {

    public ModuleNotEnabledException(XPrisonModule module) {
        super("Module " + module.getName() + " is not enabled");
    }
}