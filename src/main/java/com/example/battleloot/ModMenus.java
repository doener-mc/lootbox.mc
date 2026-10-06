package com.example.battleloot;

import com.example.battleloot.menu.LootConfigMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BattleLoot.MODID);

    public static final RegistryObject<MenuType<LootConfigMenu>> LOOT_CONFIG = MENUS.register("loot_config",
            () -> IForgeMenuType.create(LootConfigMenu::new));
}
