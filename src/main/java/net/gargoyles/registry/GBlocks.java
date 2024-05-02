/*    */ package net.gargoyles.registry;
import net.gargoyles.blocks.BlockMagicPumpkin;
import net.gargoyles.blocks.BlockPerch;
/*    */ 
/*    */ import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GBlocks
/*    */ {
/*    */   public static Block magic_pumpkin;
/*    */   public static Block stoneperch;
/*    */   public static Block sandstoneperch;
/*    */   public static Block obsidianperch;
/*    */   public static Block goldperch;
/*    */   public static Block ironperch;
/*    */   public static Block endstoneperch;
/*    */   public static Block netherbrickperch;
/*    */   
/*    */   public static void init()
/*    */   {
	magic_pumpkin = new BlockMagicPumpkin();
	stoneperch = new BlockPerch("stoneperch", 1.5F, 10F);
	sandstoneperch = new BlockPerch("sandstoneperch", 0.8F, 5F);
	obsidianperch = new BlockPerch("obsidianperch", 50F, 2000F);
	goldperch = new BlockPerch("goldperch", 3F, 10F);
	ironperch = new BlockPerch("ironperch", 5F, 10F);
	endstoneperch = new BlockPerch("endstoneperch", 3F, 15F);
	netherbrickperch = new BlockPerch("netherbrickperch", 2F, 10F);
/*    */     
/* 45 */     registerBlock(magic_pumpkin);
/* 45 */     registerBlock(stoneperch);
/* 45 */     registerBlock(sandstoneperch);
/* 45 */     registerBlock(obsidianperch);
/* 45 */     registerBlock(goldperch);
/* 45 */     registerBlock(ironperch);
/* 45 */     registerBlock(endstoneperch);
/* 45 */     registerBlock(netherbrickperch);
/*    */   }
/*    */   
	private static void registerBlock(Block block)
	{
		GameData.register_impl(block.setRegistryName(block.getUnlocalizedName().substring(5)));
		GameData.register_impl(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		registerRender(magic_pumpkin);
		registerRender(stoneperch);
		registerRender(sandstoneperch);
		registerRender(obsidianperch);
		registerRender(goldperch);
		registerRender(ironperch);
		registerRender(endstoneperch);
		registerRender(netherbrickperch);
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(Block block)
	{
		Item item = Item.getItemFromBlock(block);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("gargoyles:" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}