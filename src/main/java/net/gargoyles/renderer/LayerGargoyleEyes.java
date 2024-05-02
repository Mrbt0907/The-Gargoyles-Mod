package net.gargoyles.renderer;

import net.gargoyles.entity.EntityGargoyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerGargoyleEyes implements LayerRenderer<EntityGargoyle>
{
    private static final ResourceLocation aggressiveEyes = new ResourceLocation("gargoyles", "textures/entities/gargoyle_eyes_aggressive.png");
    private static final ResourceLocation defensiveEyes = new ResourceLocation("gargoyles", "textures/entities/gargoyle_eyes_defensive.png");
    private static final ResourceLocation passiveEyes = new ResourceLocation("gargoyles", "textures/entities/gargoyle_eyes_passive.png");
    private final RenderGargoyle endermanRenderer;

    public LayerGargoyleEyes(RenderGargoyle endermanRendererIn)
    {
        this.endermanRenderer = endermanRendererIn;
    }

    public void doRenderLayer(EntityGargoyle entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if (entitylivingbaseIn.isEntityAlive() && !entitylivingbaseIn.isInvisible())
    	{
        	if (entitylivingbaseIn.getState() == 1)
        		this.endermanRenderer.bindTexture(aggressiveEyes);
        	else if (entitylivingbaseIn.getState() == 0)
            	this.endermanRenderer.bindTexture(defensiveEyes);
        	else
        		this.endermanRenderer.bindTexture(passiveEyes);
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
            float f = 0.5F + (MathHelper.cos(ageInTicks * (entitylivingbaseIn.getState() == 1 ? 1F : 0.05F)) * 0.5F);
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