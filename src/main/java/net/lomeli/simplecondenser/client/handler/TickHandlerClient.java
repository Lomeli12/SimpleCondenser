package net.lomeli.simplecondenser.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TickHandlerClient {
    public static float clientTicks;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (Minecraft.getMinecraft().inGameHasFocus)
                clientTicks++;
            else {
                GuiScreen gui = Minecraft.getMinecraft().currentScreen;
                if (gui == null || !gui.doesGuiPauseGame())
                    clientTicks++;
            }
        }
    }
}
