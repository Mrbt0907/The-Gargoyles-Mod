package net.endermanofdoom.necromancer;

import net.endermanofdoom.necromancer.entity.friendly.*;
import net.endermanofdoom.necromancer.entity.boss.*;
import net.endermanofdoom.necromancer.client.renderer.*;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		renderEntities();
	}

	public void init(FMLInitializationEvent e)
	{
		super.init(e);
	}

	public void postInit(FMLPostInitializationEvent e)
	{
		super.postInit(e);
		NItems.registerRenders();
	}

	public void renderEntities()
	{
		 RenderingRegistry.registerEntityRenderingHandler(EntityZombieTame.class, manager -> new RenderZombieTame(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityHuskTame.class, manager -> new RenderHuskTame(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityPigZombieTame.class, manager -> new RenderPigZombieTame(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonTame.class, manager -> new RenderSkeletonTame(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityStrayTame.class, manager -> new RenderStrayTame(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityWitherSkeletonTame.class, manager -> new RenderWitherSkeletonTame(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityDarkBeam.class, manager -> new RenderNullEntity(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityNecromancer.class, manager -> new RenderNecromancer(manager));
	}
}