package com.example.battleloot.block;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/** Rechtsklick auf eine Lootbox mit diesem Item oeffnet das Config-GUI (nur OPs). */
public class ConfigWandItem extends Item {
    public ConfigWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.battleloot.config_wand.tooltip"));
    }
}
