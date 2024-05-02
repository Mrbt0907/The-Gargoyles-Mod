 package net.gargoyles.renderer;
 
import net.gargoyles.entity.EntityGargoyle;
import net.gargoyles.model.ModelGargoyle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;
 
 
 
 
 
 @SideOnly(Side.CLIENT)
 public class RenderGargoyle
   extends RenderLiving<EntityGargoyle>
 {
   private static final ResourceLocation stoneGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle1.png");
   private static final ResourceLocation sandstoneGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle2.png");
   private static final ResourceLocation obsidianGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle3.png");
   private static final ResourceLocation goldenGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle4.png");
   private static final ResourceLocation ironGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle5.png");
   private static final ResourceLocation endstoneGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle6.png");
   private static final ResourceLocation nethraticGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle7.png");
   private static final ResourceLocation gargoyleBeamTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle_beam.png");
   
   public RenderGargoyle(RenderManager renderManager)
   {
   		super(renderManager, new ModelGargoyle(), 0.8F);
   		this.addLayer(new LayerGargoyleEyes(this));
   }
   
 
 
 
   protected ResourceLocation getEntityTexture(EntityGargoyle entity)
   {
       switch (entity.getVariant())
       {
           case 0:
           default:
               return stoneGargoyleTextures;
           case 1:
               return sandstoneGargoyleTextures;
           case 2:
               return obsidianGargoyleTextures;
           case 3:
               return goldenGargoyleTextures;
           case 4:
               return ironGargoyleTextures;
           case 5:
               return endstoneGargoyleTextures;
           case 6:
               return nethraticGargoyleTextures;
       }
   }
   
   protected void applyRotations(EntityGargoyle p_180588_1_, float p_180588_2_, float p_180588_3_, float p_180588_4_)
   {
	   super.applyRotations(p_180588_1_, p_180588_2_, p_180588_3_, p_180588_4_);
	   if (p_180588_1_.getVariant() == 1)
	   {
		   GL11.glScalef(0.75F, 1F, 0.75F);
	   }
	   if (p_180588_1_.getVariant() == 2)
	   {
		   GL11.glScalef(1.25F, 1F, 1.25F);
	   }
	   if (p_180588_1_.getVariant() == 3)
	   {
		   GL11.glScalef(1.1F, 1F, 1.1F);
	   }
	   if (p_180588_1_.getVariant() == 4)
	   {
		   GL11.glScalef(1.2F, 1F, 1.2F);
	   }
	   if (p_180588_1_.getVariant() == 5)
	   {
		   GL11.glScalef(1.1F, 1.1F, 1.1F);
	   }
	   if (p_180588_1_.getVariant() == 6)
	   {
		   GL11.glScalef(1.05F, 0.95F, 1.05F);
	   }
   }
   
   private Vec3d getPosition(EntityLivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_)
   {
       double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * (double)p_177110_4_;
       double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * (double)p_177110_4_;
       double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * (double)p_177110_4_;
       return new Vec3d(d0, d1, d2);
   }
   
   public void doRender(EntityGargoyle entity, double x, double y, double z, float entityYaw, float partialTicks)
   {
       super.doRender(entity, x, y, z, entityYaw, partialTicks);
       EntityLivingBase entitylivingbase = entity.getTargetedEntity();

       if (entity.getVariant() == 3 && entitylivingbase != null)
       {
           float f = entity.getAttackAnimationScale(partialTicks);
           Tessellator tessellator = Tessellator.getInstance();
           BufferBuilder bufferbuilder = tessellator.getBuffer();
           this.bindTexture(gargoyleBeamTextures);
           GlStateManager.glTexParameteri(3553, 10242, 10497);
           GlStateManager.glTexParameteri(3553, 10243, 10497);
           GlStateManager.disableLighting();
           GlStateManager.disableCull();
           GlStateManager.disableBlend();
           GlStateManager.depthMask(true);
           int i1 = 15728880;
           int j1 = i1 % 65536;
           int k1 = i1 / 65536;
           OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j1, (float)k1);
           GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
           float f2 = (float)entity.world.getTotalWorldTime() + partialTicks;
           float f3 = f2 * 0.5F % 1.0F;
           float f4 = entity.getEyeHeight();
           GlStateManager.pushMatrix();
           GlStateManager.translate((float)x, (float)y + f4, (float)z);
           Vec3d vec3d = this.getPosition(entitylivingbase, entitylivingbase instanceof EntityDragon ? 2.5D : (double)entitylivingbase.getEyeHeight(), partialTicks);
           Vec3d vec3d1 = this.getPosition(entity, (double)f4, partialTicks);
           Vec3d vec3d2 = vec3d.subtract(vec3d1);
           double d0 = vec3d2.lengthVector() + 1.0D;
           vec3d2 = vec3d2.normalize();
           float f5 = (float)Math.acos(vec3d2.y);
           float f6 = (float)Math.atan2(vec3d2.z, vec3d2.x);
           GlStateManager.rotate((((float)Math.PI / 2F) + -f6) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
           GlStateManager.rotate(f5 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
           double d1 = (double)f2 * 0.05D * -1.5D;
           bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
           float f7 = f * f;
           int j = 255 + (int)(f7 * 255.0F);
           int k = 255 + (int)(f7 * 255.0F);
           int l = 255 - (int)(f7 * 255.0F);
           double d4 = 0.0D + Math.cos(d1 + 2.25D) * 0.25D;
           double d5 = 0.0D + Math.sin(d1 + 2.25D) * 0.25D;
           double d6 = 0.0D + Math.cos(d1 + (Math.PI / 4D)) * 0.25D;
           double d7 = 0.0D + Math.sin(d1 + (Math.PI / 4D)) * 0.25D;
           double d8 = 0.0D + Math.cos(d1 + 3.9D) * 0.25D;
           double d9 = 0.0D + Math.sin(d1 + 3.9D) * 0.25D;
           double d10 = 0.0D + Math.cos(d1 + 5.5D) * 0.25D;
           double d11 = 0.0D + Math.sin(d1 + 5.5D) * 0.25D;
           double d12 = 0.0D + Math.cos(d1 + Math.PI) * 0.2D;
           double d13 = 0.0D + Math.sin(d1 + Math.PI) * 0.2D;
           double d14 = 0.0D + Math.cos(d1 + 0.0D) * 0.2D;
           double d15 = 0.0D + Math.sin(d1 + 0.0D) * 0.2D;
           double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * 0.2D;
           double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * 0.2D;
           double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * 0.2D;
           double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * 0.2D;
           double d22 = (double)(-1.0F + f3);
           double d23 = d0 * 2.5D + d22;
           bufferbuilder.pos(d12, d0, d13).tex(0.5D, d23).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d12, 0.0D, d13).tex(0.5D, d22).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d16, d0, d17).tex(0.5D, d23).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d16, 0.0D, d17).tex(0.5D, d22).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
           double d24 = 0.0D;

           if (entity.ticksExisted % 2 == 0)
           {
               d24 = 0.5D;
           }

           bufferbuilder.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
           bufferbuilder.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
           tessellator.draw();
           GlStateManager.popMatrix();
       }
   }
 }


/* Location:              C:\Users\Gabriel\Desktop\The Titans MC 1.7.10 version 0.42-deobf.jar!\net\minecraft\theTitans\render\RenderUltimaIronGolemTitan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */