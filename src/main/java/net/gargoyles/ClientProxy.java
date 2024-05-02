package net.gargoyles;

import net.gargoyles.entity.EntityEvilGargoyle;
import net.gargoyles.entity.EntityGargoyle;
import net.gargoyles.registry.GBlocks;
import net.gargoyles.renderer.RenderEvilGargoyle;
import net.gargoyles.renderer.RenderGargoyle;
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
		GBlocks.registerRenders();
	}

	public void renderEntities()
	{
		 RenderingRegistry.registerEntityRenderingHandler(EntityGargoyle.class, manager -> new RenderGargoyle(manager));
		 RenderingRegistry.registerEntityRenderingHandler(EntityEvilGargoyle.class, manager -> new RenderEvilGargoyle(manager));
	}
}