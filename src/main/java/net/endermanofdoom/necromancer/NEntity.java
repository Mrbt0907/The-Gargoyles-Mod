package net.endermanofdoom.necromancer;

import net.endermanofdoom.necromancer.entity.boss.*;
import net.endermanofdoom.necromancer.entity.friendly.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class NEntity
{
	private static int id = 0;
	
	public static void registerEntities()
	{
		createEntityWithEgg(EntityZombieTame.class, "zombie_tame", 44975, 7969893);
		createEntityWithEgg(EntityHuskTame.class, "husk_tame", 7958625, 15125652);
		createEntityWithEgg(EntityPigZombieTame.class, "pigzombie_tame", 15373203, 5009705);
		createEntityWithEgg(EntitySkeletonTame.class, "skeleton_tame", 12698049, 4802889);
		createEntityWithEgg(EntityStrayTame.class, "stray_tame", 6387319, 14543594);
		createEntityWithEgg(EntityWitherSkeletonTame.class, "wither_skeleton_tame", 1315860, 4672845);
		createEntity(EntityDarkBeam.class, "dark_beam");
		createEntityWithEgg(EntityNecromancer.class, "necromancer", 0x1e1c1a, 0xdbdddd);
	}

	public static void createEntityWithEgg(Class<? extends Entity> entityClass, String entityName, int primary, int secondary)
	{
		createEntity(entityClass, entityName);
		EntityRegistry.registerEgg(new ResourceLocation(Necromancer.MODID, entityName), primary, secondary);
	}
	
	public static void createEntity(Class<? extends Entity> entityClass, String entityName)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Necromancer.MODID, entityName), entityClass, entityName, ++id, Necromancer.modInstance, 1024, 1, true);
	}
}
