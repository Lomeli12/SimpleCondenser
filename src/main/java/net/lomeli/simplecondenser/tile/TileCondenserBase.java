package net.lomeli.simplecondenser.tile;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.util.ItemUtil;

import net.lomeli.simplecondenser.lib.EnumAlchemicalType;
import net.lomeli.simplecondenser.lib.ItemLib;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;

public class TileCondenserBase extends TileEntity implements ISidedInventory {
    public static final int TOME_SLOT = 0;
    public static final int TARGET_SLOT = 1;
    public static final int CONDENSER_SIZE = 38;
    private ItemStack[] inventory;
    private ItemStack target;
    private EnergyValue storedEnergyValue;
    private EnergyValue targetEnergyValue;
    private String customName;
    private EnumAlchemicalType type;

    public TileCondenserBase() {
        this(EnumAlchemicalType.VERDANT);
    }

    public TileCondenserBase(EnumAlchemicalType type) {
        inventory = new ItemStack[CONDENSER_SIZE];
        storedEnergyValue = new EnergyValue(0);
        this.type = type;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote)
            return;
        if (hasTome() && worldObj.getTotalWorldTime() % type.getSpeed() == 0) {
            if (!hasTarget()) {
                targetEnergyValue = null;
                target = null;
            }
            if (hasTarget()) {
                if (targetEnergyValue == null || !OreDictionary.itemMatches(target, getStackInSlot(TARGET_SLOT), false)) {
                    targetEnergyValue = getTargetEnergyValue();
                    target = getStackInSlot(TARGET_SLOT);
                }
                for (int i = 2; i < getSizeInventory(); i++) {
                    if (!matchesTarget(i))
                        consumeItem(i);
                }
                if (storedEnergyValue.compareTo(targetEnergyValue) >= 0)
                    createNewItem();
            } else if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
                for (int i = 2; i < getSizeInventory(); i++)
                    consumeItem(i);
            }
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        int[] all = new int[getSizeInventory() - 2];
        for (int i = 2; i < all.length; i++)
            all[i] = i;
        return side == 1 ? new int[]{TOME_SLOT, TARGET_SLOT} : all;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        if (side == 1) {
            if (slot == TOME_SLOT && getStackInSlot(TOME_SLOT) == null)
                return ItemLib.isTome(stack);
            else if (slot == TARGET_SLOT && getStackInSlot(TARGET_SLOT) == null)
                return isStackKnown(stack);
        } else if (side > TARGET_SLOT)
            return (!matchesTarget(stack) && EnergyValueRegistryProxy.hasEnergyValue(stack));
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        if (side == 0 && slot > TARGET_SLOT)
            return hasTarget() ? matchesTarget(stack) : true;
        return false;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot < getSizeInventory() ? inventory[slot] : null;
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
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
            setInventorySlotContents(slot, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot < getSizeInventory()) {
            inventory[slot] = stack;
            if (stack != null && stack.stackSize > getInventoryStackLimit())
                stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return hasCustomInventoryName() ? customName : "tile.simplecondenser." + type.getName() + "Condenser.name";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return customName != null && !customName.isEmpty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == TOME_SLOT)
            return ItemLib.isTome(stack);
        else if (slot == TARGET_SLOT)
            return isStackKnown(stack);
        else if (slot > TARGET_SLOT && slot < inventory.length)
            return true;
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        // Read in the ItemStacks in the inventory from NBT
        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slotIndex = tagCompound.getByte("Slot");
            if (slotIndex >= 0 && slotIndex < inventory.length)
                inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
        }

        NBTTagCompound energyValueTagCompound = tag.getCompoundTag("storedEnergyValue");
        if (!energyValueTagCompound.hasNoTags())
            storedEnergyValue = EnergyValue.loadEnergyValueFromNBT(energyValueTagCompound);
        else
            storedEnergyValue = new EnergyValue(0);

        this.type = EnumAlchemicalType.getType(tag.getInteger("CondenserType"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tag.setTag("Items", tagList);

        NBTTagCompound energyValueTagCompound = new NBTTagCompound();
        if (storedEnergyValue != null)
            storedEnergyValue.writeToNBT(energyValueTagCompound);
        tag.setTag("storedEnergyValue", energyValueTagCompound);

        tag.setInteger("CondenserType", type.getIndex());
    }

    @Override
    public Packet getDescriptionPacket() {
        S35PacketUpdateTileEntity packet = (S35PacketUpdateTileEntity) super.getDescriptionPacket();
        NBTTagCompound dataTag = packet != null ? packet.func_148857_g() : new NBTTagCompound();
        writeToNBT(dataTag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, dataTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt != null ? pkt.func_148857_g() : new NBTTagCompound();
        readFromNBT(tag);
    }

    public EnumAlchemicalType getType() {
        return type;
    }

    public void setCustomName(String name) {
        customName = name;
    }

    public EnergyValue getStoredEnergyValue() {
        return storedEnergyValue;
    }

    public EnergyValue consumeItem(int slot) {
        if (getStackInSlot(slot) != null) {
            ItemStack stack = getStackInSlot(slot).copy();
            if (stack.stackSize > 1)
                stack.stackSize = 1;
            EnergyValue value = EnergyValueRegistryProxy.getEnergyValue(stack);
            if (value != null && value.getValue() > 0) {
                storedEnergyValue = new EnergyValue(storedEnergyValue.getValue() + value.getValue());
                decrStackSize(slot, 1);
            }
        }
        return storedEnergyValue;
    }

    public EnergyValue setStoredEnergy(float value) {
        storedEnergyValue = new EnergyValue(value);
        return storedEnergyValue;
    }

    private boolean isStackKnown(ItemStack stack) {
        ItemStack tome = getStackInSlot(TOME_SLOT);
        if (tome != null && tome.getItem() != null) {
            UUID playerUUID = ItemLib.getOwnerUUID(tome);
            return ItemLib.playerKnowsItem(playerUUID, stack);
        }
        return false;
    }

    private boolean matchesTarget(ItemStack stack) {
        ItemStack target = getStackInSlot(TARGET_SLOT);
        if (target != null && stack != null)
            return OreDictionary.itemMatches(target, stack, false);
        return false;
    }

    private boolean matchesTarget(int slot) {
        if (slot > TARGET_SLOT && slot < getSizeInventory()) {
            ItemStack stack = getStackInSlot(slot);
            return matchesTarget(stack);
        }
        return false;
    }

    private boolean hasTome() {
        ItemStack stack = getStackInSlot(TOME_SLOT);
        return stack != null && ItemLib.isTome(stack) && ItemLib.getOwnerUUID(stack) != null;
    }

    private boolean hasTarget() {
        ItemStack stack = getStackInSlot(TARGET_SLOT);
        return stack != null && isStackKnown(stack);
    }

    private EnergyValue getTargetEnergyValue() {
        ItemStack stack = getStackInSlot(TARGET_SLOT).copy();
        if (stack.stackSize > 1)
            stack.stackSize = 1;
        return EnergyValueRegistryProxy.getEnergyValue(stack);
    }

    private void depleteStoredEnergy(EnergyValue value) {
        float f = storedEnergyValue.getValue() - value.getValue();
        storedEnergyValue = new EnergyValue(f >= 0 ? f : 0);
    }

    private void createNewItem() {
        ItemStack newStack = getStackInSlot(TARGET_SLOT).copy();
        newStack.stackSize = 1;
        for (int i = 2; i < getSizeInventory(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack == null) {
                setInventorySlotContents(i, newStack);
                depleteStoredEnergy(targetEnergyValue);
                return;
            } else if (ItemUtil.canStacksMerge(newStack, stack) && (stack.stackSize + newStack.stackSize) <= stack.getMaxStackSize() && (stack.stackSize + newStack.stackSize) <= getInventoryStackLimit()) {
                stack.stackSize += newStack.stackSize;
                setInventorySlotContents(i, stack);
                depleteStoredEnergy(targetEnergyValue);
                return;
            }
        }
    }
}
