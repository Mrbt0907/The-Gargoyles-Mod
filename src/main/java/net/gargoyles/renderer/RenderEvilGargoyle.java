 package net.gargoyles.renderer;
 
import net.gargoyles.entity.EntityEvilGargoyle;
import net.gargoyles.model.ModelEvilGargoyle;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
 
 
 
 
 
 @SideOnly(Side.CLIENT)
 public class RenderEvilGargoyle
   extends RenderLiving<EntityEvilGargoyle>
 {
   private static final ResourceLocation stoneGargoyleTextures = new ResourceLocation("gargoyles", "textures/entities/gargoyle8.png");
   
   public RenderEvilGargoyle(RenderManager renderManager)
   {
   		super(renderManager, new ModelEvilGargoyle(), 0.8F);
   		this.addLayer(new LayerEvilGargoyleEyes(this));
   }
   
   protected ResourceLocation getEntityTexture(EntityEvilGargoyle entity)
   {
       return stoneGargoyleTextures;
   }
 }


/* Location:              C:\Users\Gabriel\Desktop\The Titans MC 1.7.10 version 0.42-deobf.jar!\net\minecraft\theTitans\render\RenderUltimaIronGolemTitan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */