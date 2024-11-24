 package net.endermanofdoom.necromancer;
 
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

 public class CommonProxy
 {
	  public void preInit(FMLPreInitializationEvent e) 
	  {
		  NItems.init();
		  //NSounds.registerSounds();
	  }
	  
	  public void init(FMLInitializationEvent e) 
	  {
		  NEntity.registerEntities();
		  //GameRegistry.registerWorldGenerator(new WorldGen(), 10000);
	  }
	  
	  public void postInit(FMLPostInitializationEvent e) {}

}