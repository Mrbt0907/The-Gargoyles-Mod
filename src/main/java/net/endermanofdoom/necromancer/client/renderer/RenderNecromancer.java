package net.endermanofdoom.necromancer.client.renderer;

import net.endermanofdoom.necromancer.Necromancer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNecromancer extends RenderLiving<EntityMob>
{
    public RenderNecromancer(RenderManager p_i47207_1_)
    {
        super(p_i47207_1_, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
        ((ModelIllager)this.getMainModel()).hat.showModel = true;
        this.addLayer(new LayerNecroEyes(this));
        this.addLayer(new LayerShoulderPads(this));
        this.addLayer(new LayerHeldItem(this)
        {
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
            {
                if (((EntitySpellcasterIllager)entitylivingbaseIn).isSpellcasting())
                {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            protected void translateToHand(EnumHandSide p_191361_1_)
            {
                ((ModelIllager)this.livingEntityRenderer.getMainModel()).getArm(p_191361_1_).postRender(0.0625F);
            }
        });
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityMob entity)
    {
        return ((EntitySpellcasterIllager)entity).isSpellcasting() ? new ResourceLocation(Necromancer.MODID, "textures/boss/necromancer_casting.png") : new ResourceLocation(Necromancer.MODID, "textures/boss/necromancer.png");
    }

    public ModelIllager getMainModel()
    {
        return (ModelIllager)super.getMainModel();
    }
    
    public class LayerNecroEyes implements LayerRenderer<EntitySpellcasterIllager>
    {
    	private final RenderNecromancer endermanRenderer;

        public LayerNecroEyes(RenderNecromancer endermanRendererIn)
        {
            this.endermanRenderer = endermanRendererIn;
        }

        public void doRenderLayer(EntitySpellcasterIllager entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
        {
            this.endermanRenderer.bindTexture(new ResourceLocation(Necromancer.MODID, "textures/boss/necromancer_eyes.png"));
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 15728880.0F, 0.0F);
            GlStateManager.enableLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            this.endermanRenderer.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
            this.endermanRenderer.setLightmap(entitylivingbaseIn);
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
        }

        public boolean shouldCombineTextures()
        {
            return false;
        }
    }
    
    public class LayerShoulderPads implements LayerRenderer<EntitySpellcasterIllager>
    {
        private final RenderNecromancer snowManRenderer;

        public LayerShoulderPads(RenderNecromancer snowManRendererIn)
        {
            this.snowManRenderer = snowManRendererIn;
        }

        public void doRenderLayer(EntitySpellcasterIllager entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
        {
            if (!entitylivingbaseIn.isInvisible())
            {
            	ItemStack shoulderpad = new ItemStack(Items.SKULL, 1, 0);
            	if (entitylivingbaseIn.isSpellcasting())
            	{
                    GlStateManager.pushMatrix();
                    this.snowManRenderer.getMainModel().leftArm.postRender(0.0625F);
                    GlStateManager.translate(0F, 0F, 0.25F);
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scale(0.625F, -0.625F, -0.625F);
                    Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, shoulderpad, ItemCameraTransforms.TransformType.HEAD);
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    this.snowManRenderer.getMainModel().rightArm.postRender(0.0625F);
                    GlStateManager.translate(0F, 0F, 0.25F);
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scale(0.625F, -0.625F, -0.625F);
                    Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, shoulderpad, ItemCameraTransforms.TransformType.HEAD);
                    GlStateManager.popMatrix();
            	}
            	else
            	{
                    GlStateManager.pushMatrix();
                    this.snowManRenderer.getMainModel().arms.postRender(0.0625F);
                    GlStateManager.translate(0.425F, -0.275F, -0.15F);
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-45.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scale(0.625F, -0.625F, -0.625F);
                    Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, shoulderpad, ItemCameraTransforms.TransformType.HEAD);
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    this.snowManRenderer.getMainModel().arms.postRender(0.0625F);
                    GlStateManager.translate(-0.425F, -0.275F, -0.15F);
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scale(0.625F, -0.625F, -0.625F);
                    Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, shoulderpad, ItemCameraTransforms.TransformType.HEAD);
                    GlStateManager.popMatrix();
            	}
            }
        }

        public boolean shouldCombineTextures()
        {
            return true;
        }
    }
}