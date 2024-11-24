package net.endermanofdoom.necromancer;

import net.endermanofdoom.necromancer.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NItems 
{
	public static final ArmorMaterial NECROMANCER_CLOTHES = EnumHelper.addArmorMaterial("Necromancer", Necromancer.MODID + ":necro", 1, new int[]{1, 2, 1, 1}, 30, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F);
	
	public static final Item NECROMANCER_STAFF = new ItemNecromancerStaff("necro_staff");
	public static final Item NECROMANCER_HOOD = new ItemNecromancerClothes(NECROMANCER_CLOTHES, 1, EntityEquipmentSlot.HEAD);
	public static final Item NECROMANCER_ROBE = new ItemNecromancerClothes(NECROMANCER_CLOTHES, 1, EntityEquipmentSlot.CHEST);
	public static final Item NECROMANCER_BELT = new ItemNecromancerClothes(NECROMANCER_CLOTHES, 2, EntityEquipmentSlot.LEGS);
	public static final Item NECROMANCER_SHOES = new ItemNecromancerClothes(NECROMANCER_CLOTHES, 1, EntityEquipmentSlot.FEET);
	
	public static void init()
	{
		GameData.register_impl(NECROMANCER_STAFF.setRegistryName(NECROMANCER_STAFF.getUnlocalizedName().substring(5)));
		GameData.register_impl(NECROMANCER_HOOD.setRegistryName(NECROMANCER_HOOD.getUnlocalizedName().substring(5)));
		GameData.register_impl(NECROMANCER_ROBE.setRegistryName(NECROMANCER_ROBE.getUnlocalizedName().substring(5)));
		GameData.register_impl(NECROMANCER_BELT.setRegistryName(NECROMANCER_BELT.getUnlocalizedName().substring(5)));
		GameData.register_impl(NECROMANCER_SHOES.setRegistryName(NECROMANCER_SHOES.getUnlocalizedName().substring(5)));
		NECROMANCER_CLOTHES.setRepairItem(new ItemStack(Blocks.WOOL, 1, 15));
	}

	public static void registerRenders() 
	{
		registerRender(NECROMANCER_STAFF);
		registerRender(NECROMANCER_HOOD);
		registerRender(NECROMANCER_ROBE);
		registerRender(NECROMANCER_BELT);
		registerRender(NECROMANCER_SHOES);
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Necromancer.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
