package com.example.battleloot.menu;

import com.example.battleloot.ModBlocks;
import com.example.battleloot.ModMenus;
import com.example.battleloot.block.LootBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Daten (ContainerData): 0 = min Items, 1 = max Items, 2 = Modus, 3 = Refill-Sekunden.
 * Button-IDs: 0/1 min -/+, 2/3 max -/+, 4/5 Sekunden -/+, 6 Modus umschalten.
 */
public class LootConfigMenu extends AbstractContainerMenu {
    public static final int POOL_SLOTS = 27;

    private final Container pool;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    // Client
    public LootConfigMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos());
    }

    // Server
    public LootConfigMenu(int id, Inventory inv, LootBoxBlockEntity be) {
        this(id, inv, be.getBlockPos());
    }

    private LootConfigMenu(int id, Inventory inv, BlockPos pos) {
        super(ModMenus.LOOT_CONFIG.get(), id);
        Level level = inv.player.level();
        this.access = ContainerLevelAccess.create(level, pos);

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof LootBoxBlockEntity box) {
            this.pool = box.getPool();
            this.data = box.getData();
        } else {
            this.pool = new SimpleContainer(POOL_SLOTS);
            this.data = new SimpleContainerData(4);
        }

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 9; c++) {
                this.addSlot(new Slot(pool, c + r * 9, 8 + c * 18, 18 + r * 18));
            }
        }
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 9; c++) {
                this.addSlot(new Slot(inv, c + r * 9 + 9, 8 + c * 18, 152 + r * 18));
            }
        }
        for (int c = 0; c < 9; c++) {
            this.addSlot(new Slot(inv, c, 8 + c * 18, 210));
        }
        this.addDataSlots(this.data);
    }

    public int getValue(int index) {
        return this.data.get(index);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (!player.hasPermissions(2)) {
            return false;
        }
        int min = data.get(0);
        int max = data.get(1);
        int mode = data.get(2);
        int secs = data.get(3);

        switch (id) {
            case 0 -> min = Math.max(1, min - 1);
            case 1 -> min = Math.min(POOL_SLOTS, min + 1);
            case 2 -> max = Math.max(1, max - 1);
            case 3 -> max = Math.min(POOL_SLOTS, max + 1);
            case 4 -> secs = Math.max(1, secs - 5);
            case 5 -> secs = Math.min(3600, secs + 5);
            case 6 -> mode = mode == 0 ? 1 : 0;
            default -> {
                return false;
            }
        }
        if (id <= 1 && min > max) {
            max = min;
        }
        if ((id == 2 || id == 3) && max < min) {
            min = max;
        }
        data.set(0, min);
        data.set(1, max);
        data.set(2, mode);
        data.set(3, secs);
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < POOL_SLOTS) {
                if (!this.moveItemStackTo(stack, POOL_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, POOL_SLOTS, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.LOOT_BOX.get());
    }
}
