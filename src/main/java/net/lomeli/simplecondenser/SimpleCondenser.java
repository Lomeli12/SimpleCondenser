package net.lomeli.simplecondenser;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.lomeli.simplecondenser.core.Proxy;

@Mod(modid = SimpleCondenser.MOD_ID, name = SimpleCondenser.NAME, version = SimpleCondenser.VERSION, dependencies = SimpleCondenser.DEPENDENCIES)
public class SimpleCondenser {
    public static final String MOD_ID = "simplecondenser";
    public static final String NAME = "Simple Condenser";
    public static final int MAJOR = 1, MINOR = 0, REV = 0;
    public static final String VERSION = MAJOR + "." + MINOR + "." + REV;
    public static final String DEPENDENCIES = "required-after:LomLib;required-after:EE3;";
    public static final String UPDATE = "null";

    @Mod.Instance(SimpleCondenser.MOD_ID)
    public static SimpleCondenser modInstance;

    @SidedProxy(serverSide = "net.lomeli.simplecondenser.core.Proxy", clientSide = "net.lomeli.simplecondenser.client.ClientProxy")
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
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
}