package com.eric.luciernagas;

import com.eric.luciernagas.entity.FireflyEntity;
import com.eric.luciernagas.entity.FireflyRenderer;
import com.eric.luciernagas.registry.ModEntities;
import com.eric.luciernagas.registry.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LuciernagasMod.MOD_ID)
public class LuciernagasMod {
    public static final String MOD_ID = "luciernagas";

    public LuciernagasMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(bus);
        ModItems.ITEMS.register(bus);
        bus.addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() ->
            EntityRenderers.register(ModEntities.FIREFLY.get(), FireflyRenderer::new)
        );
    }
}
