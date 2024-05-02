 package net.gargoyles;
 
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
 
 @Mod(modid=Gargoyles.MODID, name=Gargoyles.MODNAME, version=Gargoyles.VERSION)
 public class Gargoyles
 {
   public static final String MODNAME = "Gargoyles Mod";
   public static final String MODID = "gargoyles";
   public static final String VERSION = "1.1";
   public static final String CLIENT = "net.gargoyles.ClientProxy";
   public static final String SERVER = "net.gargoyles.CommonProxy";
   
   @SidedProxy(clientSide=CLIENT, serverSide=SERVER)
   public static CommonProxy proxy;
   @Mod.Metadata
   public static ModMetadata meta;
   @Mod.Instance(MODID)
   public static Gargoyles modInstance;
   
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
   }
 }