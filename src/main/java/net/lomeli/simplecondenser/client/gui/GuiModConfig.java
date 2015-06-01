package net.lomeli.simplecondenser.client.gui;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.config.GuiConfig;

import net.lomeli.lomlib.lib.ModLibs;

import net.lomeli.simplecondenser.SimpleCondenser;

public class GuiModConfig extends GuiConfig {
    public GuiModConfig(GuiScreen parent) {
        super(parent, new ConfigElement(SimpleCondenser.config.getConfig().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), ModLibs.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(SimpleCondenser.config.getConfig().toString()));

    }
}
