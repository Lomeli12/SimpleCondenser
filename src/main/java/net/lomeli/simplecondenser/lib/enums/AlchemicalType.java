package net.lomeli.simplecondenser.lib.enums;

public enum AlchemicalType {
    VERDANT(0, "verdant", 20L), AZURE(1, "azure", 10L), MINIUM(2, "minium", 1L);

    public static AlchemicalType[] VALID_TYPES = {VERDANT, AZURE, MINIUM};
    private final String name;
    private final int index;
    private final long speed;

    AlchemicalType(int index, String name, long speed) {
        this.name = name;
        this.index = index;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public long getSpeed() {
        return speed;
    }

    public static AlchemicalType getType(int index) {
        return (index >= 0 && index < VALID_TYPES.length) ? VALID_TYPES[index] : VALID_TYPES[0];
    }
}
