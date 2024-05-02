 package net.gargoyles.registry;
 
import net.gargoyles.Gargoyles;
import net.gargoyles.entity.EntityEvilGargoyle;
import net.gargoyles.entity.EntityGargoyle;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
 
public class GEntity
{
	 private static int id = 0;
	 
   public static void registerEntity()
   {
	   createEntityWithEgg(EntityGargoyle.class, "gargoyle", 11316396, 10526880);
	   createEntityWithEgg(EntityEvilGargoyle.class, "gargoyle_evil", 11316396, 10526880);
   }
   
   private static void createEgg(String entityName, int solidColor, int spotColor)
   {
     EntityRegistry.registerEgg(new ResourceLocation(Gargoyles.MODID, entityName), solidColor, spotColor);
   }

	public static void createEntityWithEgg(Class<? extends Entity> entityClass, String entityName, int primary, int secondary)
	{
		createEntity(entityClass, entityName);
		createEgg(entityName, primary, secondary);
	}
	
	public static void createEntity(Class<? extends Entity> entityClass, String entityName)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Gargoyles.MODID, entityName), entityClass, entityName, ++id, Gargoyles.modInstance, 2048, 1, true);
	}
}