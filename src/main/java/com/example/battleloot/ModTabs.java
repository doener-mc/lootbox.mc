package com.example.battleloot;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BattleLoot.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.battleloot"))
                    .icon(() -> new ItemStack(ModItems.LOOT_BOX.get()))
                    .displayItems((params, out) -> {
                        out.accept(ModItems.LOOT_BOX.get());
                        out.accept(ModItems.CONFIG_WAND.get());
                    })
                    .build());
}
