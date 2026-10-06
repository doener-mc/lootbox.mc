package com.example.battleloot.block;

import com.example.battleloot.ModBlockEntities;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LootBoxBlockEntity extends BlockEntity {
    public static final int POOL_SIZE = 27;

    private int minRolls = 1;
    private int maxRolls = 3;
    /** 0 = nach dem Oeffnen zerstoeren, 1 = nach Zeit wieder auffuellen */
    private int mode = 0;
    private int refillSeconds = 30;

    private final SimpleContainer pool = new SimpleContainer(POOL_SIZE) {
        @Override
        public void setChanged() {
            super.setChanged();
            LootBoxBlockEntity.this.setChanged();
        }
    };

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> minRolls;
                case 1 -> maxRolls;
                case 2 -> mode;
                case 3 -> refillSeconds;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> minRolls = value;
                case 1 -> maxRolls = value;
                case 2 -> mode = value;
                case 3 -> refillSeconds = value;
                default -> { }
            }
            LootBoxBlockEntity.this.setChanged();
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public LootBoxBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LOOT_BOX.get(), pos, state);
    }

    public SimpleContainer getPool() {
        return pool;
    }

    public ContainerData getData() {
        return data;
    }

    public int getMode() {
        return mode;
    }

    public int getRefillSeconds() {
        return refillSeconds;
    }

    /** Gibt dem Spieler zufaellige Items aus dem Pool. false = Pool leer. */
    public boolean giveLoot(ServerPlayer player) {
        List<ItemStack> candidates = new ArrayList<>();
        for (int i = 0; i < pool.getContainerSize(); i++) {
            ItemStack s = pool.getItem(i);
            if (!s.isEmpty()) {
                candidates.add(s);
            }
        }
        if (candidates.isEmpty()) {
            return false;
        }

        RandomSource rnd = player.getRandom();
        int rolls = minRolls + (maxRolls > minRolls ? rnd.nextInt(maxRolls - minRolls + 1) : 0);
        List<ItemStack> bag = new ArrayList<>(candidates);

        for (int r = 0; r < rolls; r++) {
            if (bag.isEmpty()) {
                bag.addAll(candidates); // erst wenn alle Items gezogen wurden, darf sich etwas wiederholen
            }
            ItemStack source = bag.remove(rnd.nextInt(bag.size()));
            ItemStack drop = source.copy();
            drop.setCount(1 + rnd.nextInt(Math.max(1, source.getCount())));
            player.getInventory().add(drop);
            if (!drop.isEmpty()) {
                player.drop(drop, false);
            }
        }
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("MinRolls", minRolls);
        tag.putInt("MaxRolls", maxRolls);
        tag.putInt("Mode", mode);
        tag.putInt("RefillSeconds", refillSeconds);
        ListTag list = new ListTag();
        for (int i = 0; i < pool.getContainerSize(); i++) {
            ItemStack s = pool.getItem(i);
            if (!s.isEmpty()) {
                CompoundTag t = new CompoundTag();
                t.putByte("Slot", (byte) i);
                s.save(t);
                list.add(t);
            }
        }
        tag.put("Pool", list);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        minRolls = tag.contains("MinRolls") ? Math.max(1, tag.getInt("MinRolls")) : 1;
        maxRolls = tag.contains("MaxRolls") ? Math.max(minRolls, tag.getInt("MaxRolls")) : 3;
        mode = tag.getInt("Mode") == 1 ? 1 : 0;
        refillSeconds = tag.contains("RefillSeconds") ? Math.max(1, tag.getInt("RefillSeconds")) : 30;
        pool.clearContent();
        ListTag list = tag.getList("Pool", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag t = list.getCompound(i);
            int slot = t.getByte("Slot") & 255;
            if (slot < POOL_SIZE) {
                pool.setItem(slot, ItemStack.of(t));
            }
        }
    }
}
