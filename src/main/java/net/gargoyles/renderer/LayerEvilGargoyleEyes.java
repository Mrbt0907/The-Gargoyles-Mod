package net.gargoyles.renderer;

import net.gargoyles.entity.EntityEvilGargoyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerEvilGargoyleEyes implements LayerRenderer<EntityEvilGargoyle>
{
    private static final ResourceLocation aggressiveEyes = new ResourceLocation("gargoyles", "textures/entities/gargoyle_eyes_aggressive.png");
    private final RenderEvilGargoyle endermanRenderer;

    public LayerEvilGargoyleEyes(RenderEvilGargoyle endermanRendererIn)
    {
        this.endermanRenderer = endermanRendererIn;
    }

    public void doRenderLayer(EntityEvilGargoyle entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if (entitylivingbaseIn.isEntityAlive() && !entitylivingbaseIn.isInvisible())
    	{
        	this.endermanRenderer.bindTexture(aggressiveEyes);
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
            int i = 15728880;
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
            GlStateManager.enableLighting();
            float f = 0.5F + (MathHelper.cos(ageInTicks * 0.05F) * 0.5F);
            GlStateManager.color(f, f, f, 1.0F);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.endermanRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            this.endermanRenderer.setLightmap(entitylivingbaseIn);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
    	}
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}