 package net.gargoyles;
 
import net.gargoyles.entity.EntityEvilGargoyle;
import net.gargoyles.entity.EntityGargoyle;
import net.gargoyles.registry.GBlocks;
import net.gargoyles.registry.GEntity;
import net.gargoyles.registry.GSounds;
import net.gargoyles.worldgen.WorldGen;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

 public class CommonProxy
 {
	  public void preInit(FMLPreInitializationEvent e) 
	  {
		  GBlocks.init();
			GSounds.registerSounds();
			EntityParrot.registerMimicSound(EntityEvilGargoyle.class, GSounds.gargoyleLiving);
			EntityParrot.registerMimicSound(EntityGargoyle.class, GSounds.gargoyleLiving);
			EntityParrot.registerMimicSound(EntityIronGolem.class, SoundEvents.ENTITY_IRONGOLEM_HURT);
			EntityParrot.registerMimicSound(EntitySnowman.class, SoundEvents.ENTITY_SNOWMAN_HURT);
	  }
	  
	  public void init(FMLInitializationEvent e) 
	  {
			GEntity.registerEntity();
		     GameRegistry.registerWorldGenerator(new WorldGen(), 10000);
	  }
	  
	  public void postInit(FMLPostInitializationEvent e) {}

}