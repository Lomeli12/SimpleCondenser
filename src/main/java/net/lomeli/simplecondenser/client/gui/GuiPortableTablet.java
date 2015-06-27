package net.lomeli.simplecondenser.client.gui;

import java.text.DecimalFormat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.simplecondenser.inventory.ContainerPortableTablet;
import net.lomeli.simplecondenser.inventory.InventoryPortableTablet;

import com.pahimar.ee3.client.gui.element.ElementSearchField;
import com.pahimar.ee3.client.gui.element.ElementStatefulButton;
import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.network.message.MessageGuiElementClicked;
import com.pahimar.ee3.network.message.MessageSliderElementUpdated;
import com.pahimar.repackage.cofh.lib.gui.GuiBase;
import com.pahimar.repackage.cofh.lib.gui.GuiColor;
import com.pahimar.repackage.cofh.lib.gui.element.ElementSlider;
import com.pahimar.repackage.cofh.lib.gui.element.ElementTextField;
import com.pahimar.repackage.cofh.lib.render.RenderHelper;

@SideOnly(Side.CLIENT)
public class GuiPortableTablet extends GuiBase {
    public static final ResourceLocation TRANSMUTATION_TABLET = ResourceUtil.getGuiResource("ee3", "transmutationTablet.png");
    public static final String ENERGY_VALUE = "misc.ee3:energy-value";
    private static final int LEFT_MOUSE_BUTTON = 0;
    private static final int RIGHT_MOUSE_BUTTON = 1;
    private static final int SORT_BY_DISPLAY_NAME = 0;
    private static final int SORT_BY_ENERGY_VALUE = 1;
    private static final int SORT_BY_ID = 2;
    private static final int SORT_ASCENDING = 0;
    private static final int SORT_DESCENDING = 1;
    private static final String TOOLTIP_PREFIX = "tooltip.ee3:";
    public static final String TOOLTIP_SORT_BY_DISPLAY_NAME = TOOLTIP_PREFIX + "sortByDisplayName";
    public static final String TOOLTIP_SORT_BY_ENERGY_VALUE = TOOLTIP_PREFIX + "sortByEnergyValue";
    public static final String TOOLTIP_SORT_BY_ID = TOOLTIP_PREFIX + "sortByID";
    public static final String TOOLTIP_SORT_ASCENDING = TOOLTIP_PREFIX + "sortAscending";
    public static final String TOOLTIP_SORT_DESCENDING = TOOLTIP_PREFIX + "sortDescending";
    private static DecimalFormat energyValueDecimalFormat = new DecimalFormat("###,###,###,###,###.###");
    protected int tickCount;
    private InventoryPortableTablet tabletInventory;
    private ElementTextField searchTextField;
    private ElementStatefulButton sortOptionButton;
    private ElementStatefulButton sortOrderButton;
    private ElementSlider slider;

    public GuiPortableTablet(EntityPlayer player, InventoryPortableTablet tabletInventory) {
        super(new ContainerPortableTablet(player, tabletInventory), TRANSMUTATION_TABLET);
        this.tabletInventory = tabletInventory;
        xSize = 256;
        ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.drawTitle = false;
        this.drawInventory = false;

        searchTextField = new ElementSearchField(this, 173, 18, "searchField", 78, 10);
        searchTextField.backgroundColor = new GuiColor(0, 0, 0, 0).getColor();
        searchTextField.borderColor = new GuiColor(0, 0, 0, 0).getColor();

        sortOptionButton = new ElementStatefulButton(this, 151, 36, "sortOption", 0, 0, 18, 0, 36, 0, 18, 18, 54, 18, ResourceUtil.getGuiResource("ee3", "elements/buttonSortOption.png")) {
            @Override
            public void drawBackground(int mouseX, int mouseY, float gameTicks) {
                RenderHelper.bindTexture(texture);
                if (isEnabled()) {
                    if (getState() == SORT_BY_DISPLAY_NAME)
                        drawTexturedModalRect(posX, posY, 36, 0, sizeX, sizeY);
                    else if (getState() == SORT_BY_ENERGY_VALUE)
                        drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
                    else if (getState() == SORT_BY_ID)
                        drawTexturedModalRect(posX, posY, 18, 0, sizeX, sizeY);
                }
            }
        };

        sortOrderButton = new ElementStatefulButton(this, 151, 58, "sortOrder", 0, 0, 0, 0, 18, 0, 18, 18, 36, 18, ResourceUtil.getGuiResource("ee3", "elements/buttonSortOrder.png")) {
            @Override
            public void drawBackground(int mouseX, int mouseY, float gameTicks) {
                RenderHelper.bindTexture(texture);
                if (isEnabled()) {
                    if (getState() == SORT_ASCENDING)
                        drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
                    else if (getState() == SORT_DESCENDING)
                        drawTexturedModalRect(posX, posY, 18, 0, sizeX, sizeY);
                }
            }
        };
        setTooltipByState();

        slider = new ElementSlider(this, "scrollBar", 239, 36, 12, 201, 187, 0) {
            @Override
            protected void dragSlider(int x, int y) {
                if (y > _value)
                    setValue(_value + 1);
                else
                    setValue(_value - 1);
            }

            @Override
            public boolean onMouseWheel(int mouseX, int mouseY, int movement) {
                PacketHandler.INSTANCE.sendToServer(new MessageSliderElementUpdated(this));
                return super.onMouseWheel(mouseX, mouseY, movement);
            }

            @Override
            public void onStopDragging() {
                PacketHandler.INSTANCE.sendToServer(new MessageSliderElementUpdated(this));
            }

            @Override
            public int getSliderY() {
                return _value;
            }
        };
        slider.backgroundColor = new GuiColor(0, 0, 0, 0).getColor();
        slider.borderColor = new GuiColor(0, 0, 0, 0).getColor();
        slider.setSliderSize(12, 15);

        addElement(sortOptionButton);
        addElement(sortOrderButton);
        addElement(searchTextField);
        addElement(slider);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
        fontRendererObj.drawString(String.format("%s:", StatCollector.translateToLocal(ENERGY_VALUE)), 10, 142, 0xffffff);
        fontRendererObj.drawString(String.format("%s", energyValueDecimalFormat.format(tabletInventory.getAvailableEnergyValue().getValue())), 10, 152, 0xffffff);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        tickCount++;
    }

    @Override
    protected boolean checkHotbarKeys(int key) {
        return false;
    }

    @Override
    public void handleElementButtonClick(String buttonName, int mouseButton) {
        if (buttonName.equals("sortOption")) {
            PacketHandler.INSTANCE.sendToServer(new MessageGuiElementClicked(buttonName, mouseButton));

            if (mouseButton == LEFT_MOUSE_BUTTON) {
                if (sortOptionButton.getState() == SORT_BY_DISPLAY_NAME)
                    sortOptionButton.setState(SORT_BY_ENERGY_VALUE);
                else if (sortOptionButton.getState() == SORT_BY_ENERGY_VALUE)
                    sortOptionButton.setState(SORT_BY_ID);
                else if (sortOptionButton.getState() == SORT_BY_ID)
                    sortOptionButton.setState(SORT_BY_DISPLAY_NAME);
            } else if (mouseButton == RIGHT_MOUSE_BUTTON) {
                if (sortOptionButton.getState() == SORT_BY_DISPLAY_NAME)
                    sortOptionButton.setState(SORT_BY_ID);
                else if (sortOptionButton.getState() == SORT_BY_ENERGY_VALUE)
                    sortOptionButton.setState(SORT_BY_DISPLAY_NAME);
                else if (sortOptionButton.getState() == SORT_BY_ID)
                    sortOptionButton.setState(SORT_BY_ENERGY_VALUE);
            }

            setTooltipByState();
        } else if (buttonName.equals("sortOrder")) {
            PacketHandler.INSTANCE.sendToServer(new MessageGuiElementClicked(buttonName, mouseButton));

            if (sortOrderButton.getState() == SORT_ASCENDING)
                sortOrderButton.setState(SORT_DESCENDING);
            else if (sortOrderButton.getState() == SORT_DESCENDING)
                sortOrderButton.setState(SORT_ASCENDING);

            setTooltipByState();
        }
    }

    private void setTooltipByState() {
        sortOptionButton.clearToolTip();
        if (sortOptionButton.getState() == SORT_BY_DISPLAY_NAME)
            sortOptionButton.setToolTip(TOOLTIP_SORT_BY_DISPLAY_NAME);
        else if (sortOptionButton.getState() == SORT_BY_ENERGY_VALUE)
            sortOptionButton.setToolTip(TOOLTIP_SORT_BY_ENERGY_VALUE);
        else if (sortOptionButton.getState() == SORT_BY_ID)
            sortOptionButton.setToolTip(TOOLTIP_SORT_BY_ID);

        sortOrderButton.clearToolTip();
        if (sortOrderButton.getState() == SORT_ASCENDING)
            sortOrderButton.setToolTip(TOOLTIP_SORT_ASCENDING);
        else if (sortOrderButton.getState() == SORT_DESCENDING)
            sortOrderButton.setToolTip(TOOLTIP_SORT_DESCENDING);
    }
}
