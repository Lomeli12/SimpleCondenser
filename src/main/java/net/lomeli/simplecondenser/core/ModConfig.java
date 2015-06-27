package net.lomeli.simplecondenser.core;

import net.minecraft.util.StatCollector;

import net.minecraftforge.common.config.Configuration;

import net.lomeli.lomlib.util.SimpleConfig;

import net.lomeli.simplecondenser.SimpleCondenser;

public class ModConfig extends SimpleConfig {
    public static boolean checkForUpdates = true;
    public static boolean canPullFromSides = true;

    public ModConfig(Configuration config) {
        super(SimpleCondenser.MOD_ID, config);
    }

    @Override
    public void loadConfig() {
        checkForUpdates = getConfig().getBoolean("checkForUpdates", Configuration.CATEGORY_GENERAL, true, StatCollector.translateToLocal("text.simplecondenser.update"));
        canPullFromSides = getConfig().getBoolean("canPullFromSides", Configuration.CATEGORY_GENERAL, false, StatCollector.translateToLocal("text.simplecondenser.canPullFromSides"));

        if (getConfig().hasChanged())
            getConfig().save();
    }
}
