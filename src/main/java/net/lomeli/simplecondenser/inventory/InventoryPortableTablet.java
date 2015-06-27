package net.lomeli.simplecondenser.inventory;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import net.lomeli.lomlib.util.entity.EntityUtil;

import net.lomeli.simplecondenser.item.ModItems;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;

/**
 * A lot of this is directly copied from the Transmutation Tablet
 * Go see that to see how all this works.
 */
public class InventoryPortableTablet implements IInventory {
    public static final String INVENTORY_TAG = "Items";
    public static final String UUID_MOST_SIG = "UUIDMostSig";
    public static final String UUID_LEAST_SIG = "UUIDLeastSig";
    public static final String CUSTOM_NAME = "customName";
    public static final String TABLE_LOCAL = "tile.ee3:transmutationTablet.name";
    public static final int ITEM_INPUT_1 = 0;
    public static final int ITEM_INPUT_2 = 1;
    public static final int ITEM_INPUT_3 = 2;
    public static final int ITEM_INPUT_4 = 3;
    public static final int ITEM_INPUT_5 = 4;
    public static final int ITEM_INPUT_6 = 5;
    public static final int ITEM_INPUT_7 = 6;
    public static final int ITEM_INPUT_8 = 7;
    public static final int STONE_INDEX = 8;
    public static final int ALCHEMICAL_TOME_INDEX = 9;
    private EnergyValue storedEnergyValue;
    private EnergyValue availableEnergyValue;
    private ItemStack[] inventory;
    private ItemStack parentStack;
    private UUID uuid;
    private String customName;
    private World world;

    public InventoryPortableTablet(World world, int size, ItemStack parentStack) {
        this.inventory = new ItemStack[size];
        this.parentStack = parentStack;
        this.world = world;
        this.storedEnergyValue = new EnergyValue();
        this.availableEnergyValue = new EnergyValue();

        //if (!parentStack.hasTagCompound())
        //    parentStack.setTagCompound(new NBTTagCompound());

        readFromNBT(parentStack.getTagCompound());
    }

    public void consumeInventoryForEnergyValue(ItemStack outputItemStack) {
        EnergyValue outputEnergyValue = EnergyValueRegistryProxy.getEnergyValue(outputItemStack);

        if (this.storedEnergyValue.compareTo(outputEnergyValue) >= 0)
            this.storedEnergyValue = new EnergyValue(this.storedEnergyValue.getValue() - outputEnergyValue.getValue());
        else {
            while (this.storedEnergyValue.compareTo(outputEnergyValue) < 0 && this.availableEnergyValue.compareTo(outputEnergyValue) >= 0) {
                for (int i = 0; i < STONE_INDEX; i++) {
                    ItemStack stackInSlot = getStackInSlot(i);
                    if (stackInSlot != null && EnergyValueRegistryProxy.hasEnergyValue(stackInSlot)) {
                        this.storedEnergyValue = new EnergyValue(this.storedEnergyValue.getValue() + EnergyValueRegistryProxy.getEnergyValue(stackInSlot).getValue());
                        decrStackSize(i, 1);
                    }
                }
            }

            if (this.storedEnergyValue.getValue() >= outputEnergyValue.getValue())
                this.storedEnergyValue = new EnergyValue(this.storedEnergyValue.getValue() - outputEnergyValue.getValue());
        }

        updateEnergyValueFromInventory();
    }

    public void updateEnergyValueFromInventory() {
        float newEnergyValue = storedEnergyValue.getValue();
        for (int i = 0; i <= STONE_INDEX; i++) {
            if (inventory[i] != null && EnergyValueRegistryProxy.hasEnergyValue(inventory[i]))
                newEnergyValue += EnergyValueRegistryProxy.getEnergyValueForStack(inventory[i]).getValue();
        }
        this.availableEnergyValue = new EnergyValue(newEnergyValue);
    }

    public void tickInventory() {
        updateEnergyValueFromInventory();
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return (slot >= 0 && slot < getSizeInventory()) ? inventory[slot] : null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrementAmount) {
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= decrementAmount)
                setInventorySlotContents(slot, null);
            else {
                itemStack = itemStack.splitStack(decrementAmount);
                if (itemStack.stackSize == 0)
                    setInventorySlotContents(slot, null);
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (slot >= 0 && slot < getSizeInventory() && inventory[slot] != null) {
            ItemStack itemStack = inventory[slot];
            inventory[slot] = null;
            return itemStack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot >= 0 && slot < getSizeInventory())
            inventory[slot] = stack;
    }

    @Override
    public String getInventoryName() {
        return hasCustomInventoryName() ? this.customName : TABLE_LOCAL;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && this.customName.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return !EntityUtil.isFakePlayer(player);
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    public ItemStack getParentStack() {
        return parentStack;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public void readFromNBT(NBTTagCompound tag) {
        if (tag != null) {
            if (tag.hasKey(UUID_MOST_SIG, 4) && tag.hasKey(UUID_LEAST_SIG, 4)) {
                long most = tag.getLong(UUID_MOST_SIG);
                long least = tag.getLong(UUID_LEAST_SIG);
                this.uuid = new UUID(most, least);
            }

            NBTTagList tagList = tag.getTagList(INVENTORY_TAG, 10);
            inventory = new ItemStack[this.getSizeInventory()];
            for (int i = 0; i < tagList.tagCount(); ++i) {
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                byte slotIndex = tagCompound.getByte("Slot");
                if (slotIndex >= 0 && slotIndex < inventory.length) {
                    inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
                }
            }

            NBTTagCompound energyValueTagCompound = tag.getCompoundTag("storedEnergyValue");
            if (!energyValueTagCompound.hasNoTags())
                storedEnergyValue = EnergyValue.loadEnergyValueFromNBT(energyValueTagCompound);
            else
                storedEnergyValue = new EnergyValue(0);

            if (tag.hasKey(CUSTOM_NAME, 8))
                this.customName = tag.getString(CUSTOM_NAME);
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        if (tag != null) {
            if (this.uuid == null)
                this.uuid = UUID.randomUUID();
            tag.setLong(UUID_MOST_SIG, uuid.getMostSignificantBits());
            tag.setLong(UUID_LEAST_SIG, uuid.getLeastSignificantBits());

            NBTTagList tagList = new NBTTagList();
            for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
                if (inventory[currentIndex] != null) {
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setByte("Slot", (byte) currentIndex);
                    inventory[currentIndex].writeToNBT(tagCompound);
                    tagList.appendTag(tagCompound);
                }
            }
            tag.setTag(INVENTORY_TAG, tagList);

            NBTTagCompound energyValueTagCompound = new NBTTagCompound();
            if (storedEnergyValue != null)
                storedEnergyValue.writeToNBT(energyValueTagCompound);
            tag.setTag("storedEnergyValue", energyValueTagCompound);

            if (hasCustomInventoryName())
                tag.setString(CUSTOM_NAME, this.customName);
        }
    }

    public void onGuiSave() {
        if (parentStack != null)
            save();
    }

    public void save() {
        NBTTagCompound nbtTagCompound = parentStack.getTagCompound();

        if (nbtTagCompound == null) {
            nbtTagCompound = new NBTTagCompound();

            UUID uuid = UUID.randomUUID();
            nbtTagCompound.setLong(UUID_MOST_SIG, uuid.getMostSignificantBits());
            nbtTagCompound.setLong(UUID_LEAST_SIG, uuid.getLeastSignificantBits());
        }

        writeToNBT(nbtTagCompound);
        parentStack.setTagCompound(nbtTagCompound);
    }

    public boolean matchesParentStack(ItemStack stack) {
        if (stack != null && ModItems.portableTablet == stack.getItem()) {
            if (this.uuid == null)
                return true;
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(UUID_MOST_SIG, 4) && stack.stackTagCompound.hasKey(UUID_LEAST_SIG, 4))
                return this.uuid.getMostSignificantBits() == stack.stackTagCompound.getLong(UUID_MOST_SIG) && this.uuid.getLeastSignificantBits() == stack.stackTagCompound.getLong(UUID_LEAST_SIG);
        }
        return false;
    }

    public EnergyValue getAvailableEnergyValue() {
        return availableEnergyValue;
    }

    public EnergyValue getStoredEnergyValue() {
        return storedEnergyValue;
    }

    public World getWorld() {
        return world;
    }
}
