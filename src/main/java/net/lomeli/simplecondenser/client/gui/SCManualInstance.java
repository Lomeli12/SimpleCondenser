package net.lomeli.simplecondenser.client.gui;

import net.minecraft.util.StatCollector;

import cpw.mods.fml.client.FMLClientHandler;

import net.lomeli.repackaged.blusunrize.lib.manual.ManualInstance;
import net.lomeli.simplecondenser.SimpleCondenser;

public class SCManualInstance extends ManualInstance {

    public SCManualInstance() {
        super(FMLClientHandler.instance().getClient().fontRenderer, SimpleCondenser.MOD_ID + ":textures/gui/manual.png");
    }

    @Override
    public String getManualName() {
        return StatCollector.translateToLocal("manual.simplecondenser.manualName");
    }

    @Override
    public String[] getSortedCategoryList() {
        return new String[]{"basics", "constructs", "tools"};
    }

    @Override
    public String formatCategoryName(String s) {
        return StatCollector.translateToLocal("manual.simplecondenser.category." + s + ".name");
    }

    @Override
    public String formatEntryName(String s) {
        return StatCollector.translateToLocal("manual.simplecondenser.entry." + s + ".name");
    }

    @Override
    public String formatEntrySubtext(String s) {
        return StatCollector.translateToLocal("manual.simplecondenser.entry." + s + ".subtext");
    }

    @Override
    public String formatText(String par0) {
        String s = StatCollector.translateToLocal(par0);
        s = s.replaceAll("<br>", "\n");
        String s1 = "";
        int i = -1;
        int j = s.length();

        while ((i = s.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = s.charAt(i + 1);

                if (isFormatColor(c0))
                    s1 = "\u00a7" + c0;
                else if (isFormatSpecial(c0))
                    s1 = s1 + "\u00a7" + c0;
            }
        }

        return s1;
    }

    @Override
    public boolean showCategoryInList(String category) {
        return true;
    }

    @Override
    public boolean showEntryInList(ManualEntry entry) {
        return true;
    }

    @Override
    public int getTitleColour() {
        return 0xf78034;
    }

    @Override
    public int getSubTitleColour() {
        return 0xf78034;
    }

    @Override
    public int getTextColour() {
        return 0x555555;
    }

    @Override
    public int getHighlightColour() {
        return 0xd4804a;
    }

    @Override
    public int getPagenumberColour() {
        return 0x9c917c;
    }

    public boolean isFormatColor(char par0) {
        return par0 >= 48 && par0 <= 57 || par0 >= 97 && par0 <= 102 || par0 >= 65 && par0 <= 70;
    }

    public boolean isFormatSpecial(char par0) {
        return par0 >= 107 && par0 <= 111 || par0 >= 75 && par0 <= 79 || par0 == 114 || par0 == 82;
    }
}
