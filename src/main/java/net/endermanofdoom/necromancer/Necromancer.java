 package net.endermanofdoom.necromancer;
 
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
 
 @Mod(modid=Necromancer.MODID, name=Necromancer.MODNAME, version=Necromancer.VERSION)
 public class Necromancer
 {
   public static final String MODNAME = "Necromancers Mod";
   public static final String MODID = "necromancer";
   public static final String VERSION = "1.0";
   public static final String CLIENT = "net.endermanofdoom.necromancer.ClientProxy";
   public static final String SERVER = "net.endermanofdoom.necromancer.CommonProxy";
   
   @SidedProxy(clientSide=CLIENT, serverSide=SERVER)
   public static CommonProxy proxy;
   @Mod.Metadata
   public static ModMetadata meta;
   @Mod.Instance(MODID)
   public static Necromancer modInstance;
   
   @Mod.EventHandler
   public void preInit(FMLPreInitializationEvent e)
   {
	proxy.preInit(e);
   }
   
   @Mod.EventHandler
   public void init(FMLInitializationEvent e)
   {
     proxy.init(e);
   }
   
   @Mod.EventHandler
   public void postInit(FMLPostInitializationEvent e)
   {
     proxy.postInit(e);
		GameRegistry.registerWorldGenerator(new WorldGen(), 100);
   }
 }