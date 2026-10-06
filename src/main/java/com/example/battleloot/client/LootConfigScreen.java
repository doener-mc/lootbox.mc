package com.example.battleloot.client;

import com.example.battleloot.menu.LootConfigMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class LootConfigScreen extends AbstractContainerScreen<LootConfigMenu> {
    private Button modeButton;

    public LootConfigScreen(LootConfigMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 234;
    }

    @Override
    protected void init() {
        super.init();
        addStep(0, "-", 100, 76);
        addStep(1, "+", 152, 76);
        addStep(2, "-", 100, 93);
        addStep(3, "+", 152, 93);
        addStep(4, "-", 100, 110);
        addStep(5, "+", 152, 110);
        modeButton = this.addRenderableWidget(
                Button.builder(Component.empty(), b -> press(6))
                        .bounds(leftPos + 8, topPos + 127, 160, 18).build());
        updateModeLabel();
    }

    private void addStep(int id, String label, int x, int y) {
        this.addRenderableWidget(Button.builder(Component.literal(label), b -> press(id))
                .bounds(leftPos + x, topPos + y, 16, 14).build());
    }

    private void press(int id) {
        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
        }
    }

    private void updateModeLabel() {
        if (modeButton != null) {
            modeButton.setMessage(Component.translatable(
                    menu.getValue(2) == 1 ? "gui.battleloot.mode.refill" : "gui.battleloot.mode.destroy"));
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateModeLabel();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);
        super.render(g, mouseX, mouseY, partialTick);
        this.renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        g.fill(x, y, x + imageWidth, y + imageHeight, 0xFFC6C6C6);
        g.fill(x, y, x + imageWidth, y + 1, 0xFFFFFFFF);
        g.fill(x, y, x + 1, y + imageHeight, 0xFFFFFFFF);
        g.fill(x, y + imageHeight - 1, x + imageWidth, y + imageHeight, 0xFF555555);
        g.fill(x + imageWidth - 1, y, x + imageWidth, y + imageHeight, 0xFF555555);
        for (Slot s : this.menu.slots) {
            g.fill(x + s.x - 1, y + s.y - 1, x + s.x + 17, y + s.y + 17, 0xFF373737);
            g.fill(x + s.x, y + s.y, x + s.x + 16, y + s.y + 16, 0xFF8B8B8B);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(this.font, this.title, 8, 6, 0x404040, false);
        g.drawString(this.font, Component.translatable("gui.battleloot.min"), 8, 79, 0x404040, false);
        g.drawString(this.font, Component.translatable("gui.battleloot.max"), 8, 96, 0x404040, false);
        boolean refill = menu.getValue(2) == 1;
        g.drawString(this.font, Component.translatable("gui.battleloot.refill"), 8, 113,
                refill ? 0x404040 : 0x8B8B8B, false);
        drawValue(g, String.valueOf(menu.getValue(0)), 79, 0x404040);
        drawValue(g, String.valueOf(menu.getValue(1)), 96, 0x404040);
        drawValue(g, menu.getValue(3) + "s", 113, refill ? 0x404040 : 0x8B8B8B);
    }

    private void drawValue(GuiGraphics g, String text, int y, int color) {
        g.drawString(this.font, text, 134 - this.font.width(text) / 2, y, color, false);
    }
}
