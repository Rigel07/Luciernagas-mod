package com.eric.luciernagas.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FireflyRenderer extends EntityRenderer<FireflyEntity> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            new ResourceLocation("luciernagas", "textures/entity/firefly_blue.png"),
            new ResourceLocation("luciernagas", "textures/entity/firefly_yellow.png"),
            new ResourceLocation("luciernagas", "textures/entity/firefly_green.png"),
            new ResourceLocation("luciernagas", "textures/entity/firefly_pink.png")
    };

    public FireflyRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(FireflyEntity entity) {
        return TEXTURES[Math.min(3, entity.getColor())];
    }

    @Override
    public void render(FireflyEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float bob = Mth.sin((entity.tickCount + partialTick) * 0.16f) * 0.08f;
        poseStack.translate(0, 0.25 + bob, 0);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(0.32f, 0.32f, 0.32f);

        var vc = buffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(entity)));
        var matrix = poseStack.last().pose();

        // Simple glowing billboard: two crossed quads.
        drawQuad(vc, matrix);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
        drawQuad(vc, poseStack.last().pose());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, 15728880);
    }

    private static void drawQuad(com.mojang.blaze3d.vertex.VertexConsumer vc, org.joml.Matrix4f m) {
        float s = 1.0f;
        int light = 15728880;
        vc.vertex(m, -s, -s, 0).color(255,255,255,255).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0,0,1).endVertex();
        vc.vertex(m,  s, -s, 0).color(255,255,255,255).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0,0,1).endVertex();
        vc.vertex(m,  s,  s, 0).color(255,255,255,255).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0,0,1).endVertex();
        vc.vertex(m, -s,  s, 0).color(255,255,255,255).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0,0,1).endVertex();
    }
}
