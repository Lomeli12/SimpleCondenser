package net.lomeli.simplecondenser.lib.enums;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public enum RedstoneState {
    HIGH(0, "high"), LOW(1, "low"), OFF(2, "off");
    public static final RedstoneState[] VALID_TYPES = {HIGH, LOW, OFF};
    private final int type;
    private final String unlocal;

    RedstoneState(int type, String name) {
        this.type = type;
        this.unlocal = "text.simplecondenser.redstonecontrol." + name;
    }

    public static RedstoneState readFromNBT(NBTTagCompound tagCompound) {
        return getStateFromType(tagCompound.getInteger("redstoneState"));
    }

    public static RedstoneState getStateFromType(int type) {
        return (type >= 0 && type < VALID_TYPES.length) ? VALID_TYPES[type] : VALID_TYPES[0];
    }

    public int getType() {
        return type;
    }

    public String getUnlocal() {
        return unlocal;
    }

    public boolean meetsCondition(World world, int x, int y, int z) {
        switch (getType()) {
            case 1:
                return !world.isBlockIndirectlyGettingPowered(x, y, z);
            case 2:
                return true;
            default:
                return world.isBlockIndirectlyGettingPowered(x, y, z);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("redstoneState", this.getType());
        return tagCompound;
    }
}
