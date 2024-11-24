package net.endermanofdoom.necromancer.client.renderer;

import net.endermanofdoom.necromancer.client.model.ModelSkeletonTame;
import net.endermanofdoom.necromancer.entity.friendly.EntitySkeletonTame;
import net.endermanofdoom.necromancer.entity.friendly.EntityStrayTame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderStrayTame extends RenderSkeletonTame
{
    private static final ResourceLocation STRAY_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray.png");

    public RenderStrayTame(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.addLayer(new LayerStrayClothing(this));
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySkeletonTame entity)
    {
        return STRAY_SKELETON_TEXTURES;
    }
    
    public class LayerStrayClothing implements LayerRenderer<EntityStrayTame>
    {
        private final RenderLivingBase<?> renderer;
        private final ModelSkeletonTame layerModel = new ModelSkeletonTame(0.25F, true);

        public LayerStrayClothing(RenderLivingBase<?> p_i47183_1_)
        {
            this.renderer = p_i47183_1_;
        }

        public void doRenderLayer(EntityStrayTame entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
        {
            this.layerModel.setModelAttributes(this.renderer.getMainModel());
            this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.renderer.bindTexture(new ResourceLocation("textures/entity/skeleton/stray_overlay.png"));
            this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        public boolean shouldCombineTextures()
        {
            return true;
        }
    }
}