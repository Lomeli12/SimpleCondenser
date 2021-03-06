package net.lomeli.simplecondenser;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.lomeli.lomlib.core.network.PacketHandler;
import net.lomeli.lomlib.core.version.VersionChecker;
import net.lomeli.lomlib.util.LogHelper;

import net.lomeli.simplecondenser.core.ModConfig;
import net.lomeli.simplecondenser.core.Proxy;

@Mod(modid = SimpleCondenser.MOD_ID, name = SimpleCondenser.NAME, version = SimpleCondenser.VERSION, dependencies = SimpleCondenser.DEPENDENCIES, guiFactory = SimpleCondenser.FACTORY)
public class SimpleCondenser {
    public static final String MOD_ID = "simplecondenser";
    public static final String NAME = "Simple Condensers";
    public static final int MAJOR = 1, MINOR = 2, REV = 1;
    public static final String VERSION = MAJOR + "." + MINOR + "." + REV;
    public static final String DEPENDENCIES = "required-after:LomLib;required-after:EE3;";
    public static final String UPDATE = "https://raw.githubusercontent.com/Lomeli12/SimpleCondenser/master/update.json";
    public static final String FACTORY = "net.lomeli.simplecondenser.client.gui.GuiModConfigFactory";

    @Mod.Instance(SimpleCondenser.MOD_ID)
    public static SimpleCondenser modInstance;

    public static VersionChecker checker;
    public static ModConfig config;
    public static PacketHandler packetHandler;
    public static LogHelper logger;

    @SidedProxy(serverSide = "net.lomeli.simplecondenser.core.Proxy", clientSide = "net.lomeli.simplecondenser.client.ClientProxy")
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogHelper(NAME);
        checker = new VersionChecker(UPDATE, MOD_ID, NAME, MAJOR, MINOR, REV);
        config = new ModConfig(new Configuration(event.getSuggestedConfigurationFile()));
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        proxy.loadComplete();
    }
}
