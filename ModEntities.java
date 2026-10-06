package com.eric.luciernagas.registry;

import com.eric.luciernagas.LuciernagasMod;
import com.eric.luciernagas.entity.FireflyEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LuciernagasMod.MOD_ID);

    public static final RegistryObject<EntityType<FireflyEntity>> FIREFLY =
            ENTITIES.register("firefly", () -> EntityType.Builder.of(FireflyEntity::new, MobCategory.AMBIENT)
                    .sized(0.35f, 0.35f)
                    .clientTrackingRange(8)
                    .updateInterval(3)
                    .build("firefly"));
}
