package com.example.battleloot;

import com.example.battleloot.block.LootBoxBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BattleLoot.MODID);

    // Unzerstoerbar im Survival (wie Bedrock), im Creative sofort abbaubar
    public static final RegistryObject<Block> LOOT_BOX = BLOCKS.register("loot_box",
            () -> new LootBoxBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(-1.0F, 3600000.0F)
                    .sound(SoundType.WOOD)));
}
