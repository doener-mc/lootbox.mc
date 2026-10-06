package com.example.battleloot;

import com.example.battleloot.block.ConfigWandItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BattleLoot.MODID);

    public static final RegistryObject<Item> LOOT_BOX = ITEMS.register("loot_box",
            () -> new BlockItem(ModBlocks.LOOT_BOX.get(), new Item.Properties()));

    public static final RegistryObject<Item> CONFIG_WAND = ITEMS.register("config_wand",
            () -> new ConfigWandItem(new Item.Properties().stacksTo(1)));
}
