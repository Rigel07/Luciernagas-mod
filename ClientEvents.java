package com.eric.luciernagas.client;

import com.eric.luciernagas.registry.ModEntities;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "luciernagas", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.FIREFLY.get(), net.minecraft.world.entity.ai.attributes.AttributeSupplier.builder()
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH, 4.0)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, 0.5)
                .add(net.minecraft.world.entity.ai.attributes.Attributes.FLYING_SPEED, 0.5)
                .build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.FIREFLY.get(), SpawnPlacements.Type.ON_GROUND,
                net.minecraft.world.entity.monster.Monster::checkMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
