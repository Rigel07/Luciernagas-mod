package com.eric.luciernagas.registry;

import com.eric.luciernagas.LuciernagasMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, LuciernagasMod.MOD_ID);

    public static final RegistryObject<Item> LUMINOUS_DUST =
            ITEMS.register("luminous_dust", () -> new Item(new Item.Properties()));
}
