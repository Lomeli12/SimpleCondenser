package net.lomeli.simplecondenser.core;

import net.minecraftforge.common.config.Configuration;

import net.lomeli.lomlib.util.SimpleConfig;

import net.lomeli.simplecondenser.SimpleCondenser;

public class ModConfig extends SimpleConfig {
    public static boolean checkForUpdates = true;

    public ModConfig(Configuration config) {
        super(SimpleCondenser.MOD_ID, config);
    }

    @Override
    public void loadConfig() {
        checkForUpdates = getConfig().getBoolean("checkForUpdates", Configuration.CATEGORY_GENERAL, true, "");

        if (getConfig().hasChanged())
            getConfig().save();
    }
}
