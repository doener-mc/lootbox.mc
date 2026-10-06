package com.example.battleloot;

import com.example.battleloot.block.LootBoxBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BattleLoot.MODID);

    public static final RegistryObject<BlockEntityType<LootBoxBlockEntity>> LOOT_BOX = BLOCK_ENTITIES.register("loot_box",
            () -> BlockEntityType.Builder.of(LootBoxBlockEntity::new, ModBlocks.LOOT_BOX.get()).build(null));
}
