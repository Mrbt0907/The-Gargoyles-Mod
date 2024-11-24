package net.endermanofdoom.necromancer.item;

import java.util.List;

import javax.annotation.Nullable;
import net.endermanofdoom.necromancer.entity.boss.*;
import net.endermanofdoom.necromancer.entity.friendly.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNecromancerClothes extends ItemArmor
{
	public ItemNecromancerClothes(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlot) 
	{
		super(material, renderIndex, equipmentSlot);
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.setUnlocalizedName("necro_" + equipmentSlot.getName());
	}    
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(TextFormatting.GOLD + "Pulsing with Dark Magic and darkness." + TextFormatting.WHITE);
		switch (this.armorType)
		{
		case HEAD:
		{
	        tooltip.add(TextFormatting.AQUA + "Allows you to instantly tame a wild vanilla undead mob by hitting it with an open hand." + TextFormatting.WHITE);
	        tooltip.add(TextFormatting.RED + "Modded undead and undead bosses require support and can't be converted by default." + TextFormatting.WHITE);
			break;
		}
		case CHEST:
		{
	        tooltip.add(TextFormatting.AQUA + "Causes nearby undead to de-aggro from you completely." + TextFormatting.WHITE);
	        tooltip.add(TextFormatting.RED + "Won't work if a Necromancer is nearby, or if you hit the undead." + TextFormatting.WHITE);
			break;
		}
		case LEGS:
		{
	        tooltip.add(TextFormatting.AQUA + "Makes the wearer immune to poison, hunger, nausea and blindness." + TextFormatting.WHITE);
	        tooltip.add(TextFormatting.RED + "Can't be inflicted with regeneration." + TextFormatting.WHITE);
			break;
		}
		case FEET:
		{
	        tooltip.add(TextFormatting.AQUA + "Nearby undead become immune to sunlight." + TextFormatting.WHITE);
	        tooltip.add(TextFormatting.RED + "Effects all undead." + TextFormatting.WHITE);
			break;
		}
		default:
			break;
		}
    }
	
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemstack) 
	{
		if (world.isRemote)
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, player.posX + (player.getRNG().nextDouble() - 0.5D) * (double)player.width, player.posY + player.getRNG().nextDouble() * (double)player.height, player.posZ + (player.getRNG().nextDouble() - 0.5D) * (double)player.width, 0.0D, 0.0D, 0.0D);

		
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(32D));

		for (Entity entity : list)
		{
			if (entity instanceof EntityLiving && !(entity instanceof EntityNecromancer) && entity.isNonBoss())
			{
				EntityLiving living = (EntityLiving)entity;

				switch (this.armorType)
				{
				case HEAD:
				{
					if (player.getLastAttackedEntity() != null && !nearbyNecromancerDetected(world, player))
					{
						if (!world.isRemote && player.getHeldItemMainhand().isEmpty())
						{
							if (player.getLastAttackedEntity().getClass() == EntityZombie.class)
							{
								EntityZombieTame summon = new EntityZombieTame(world);
								summon.copyLocationAndAnglesFrom(player.getLastAttackedEntity());
								summon.onInitialSpawn(world.getDifficultyForLocation(player.getLastAttackedEntity().getPosition()), null);
								summon.setChild(player.getLastAttackedEntity().isChild());
								summon.setOwnerId(player.getUniqueID());
								world.removeEntity(player.getLastAttackedEntity());
								world.spawnEntity(summon);
							}
							if (player.getLastAttackedEntity().getClass() == EntitySkeleton.class)
							{
								EntitySkeletonTame summon = new EntitySkeletonTame(world);
								summon.copyLocationAndAnglesFrom(player.getLastAttackedEntity());
								summon.onInitialSpawn(world.getDifficultyForLocation(player.getLastAttackedEntity().getPosition()), null);
								summon.setChild(player.getLastAttackedEntity().isChild());
								summon.setOwnerId(player.getUniqueID());
								world.removeEntity(player.getLastAttackedEntity());
								world.spawnEntity(summon);
							}
							if (player.getLastAttackedEntity().getClass() == EntityHusk.class)
							{
								EntityHuskTame summon = new EntityHuskTame(world);
								summon.copyLocationAndAnglesFrom(player.getLastAttackedEntity());
								summon.onInitialSpawn(world.getDifficultyForLocation(player.getLastAttackedEntity().getPosition()), null);
								summon.setChild(player.getLastAttackedEntity().isChild());
								summon.setOwnerId(player.getUniqueID());
								world.removeEntity(player.getLastAttackedEntity());
								world.spawnEntity(summon);
							}
							if (player.getLastAttackedEntity().getClass() == EntityStray.class)
							{
								EntityStrayTame summon = new EntityStrayTame(world);
								summon.copyLocationAndAnglesFrom(player.getLastAttackedEntity());
								summon.onInitialSpawn(world.getDifficultyForLocation(player.getLastAttackedEntity().getPosition()), null);
								summon.setChild(player.getLastAttackedEntity().isChild());
								summon.setOwnerId(player.getUniqueID());
								world.removeEntity(player.getLastAttackedEntity());
								world.spawnEntity(summon);
							}
							if (player.getLastAttackedEntity().getClass() == EntityPigZombie.class)
							{
								EntityPigZombieTame summon = new EntityPigZombieTame(world);
								summon.copyLocationAndAnglesFrom(player.getLastAttackedEntity());
								summon.onInitialSpawn(world.getDifficultyForLocation(player.getLastAttackedEntity().getPosition()), null);
								summon.setChild(player.getLastAttackedEntity().isChild());
								summon.setOwnerId(player.getUniqueID());
								world.removeEntity(player.getLastAttackedEntity());
								world.spawnEntity(summon);
							}
							if (player.getLastAttackedEntity().getClass() == EntityWitherSkeleton.class)
							{
								EntityWitherSkeletonTame summon = new EntityWitherSkeletonTame(world);
								summon.copyLocationAndAnglesFrom(player.getLastAttackedEntity());
								summon.onInitialSpawn(world.getDifficultyForLocation(player.getLastAttackedEntity().getPosition()), null);
								summon.setChild(player.getLastAttackedEntity().isChild());
								summon.setOwnerId(player.getUniqueID());
								world.removeEntity(player.getLastAttackedEntity());
								world.spawnEntity(summon);
							}
						}
						player.setLastAttackedEntity(null);
					}
					break;
				}
				case CHEST:
				{
					if (living.getAttackTarget() != null && !(living instanceof EntityTameable) && living.isEntityUndead() && living.getAttackTarget() == player && !nearbyNecromancerDetected(world, player))
					{
						living.setAttackTarget(null);
						living.getNavigator().clearPath();
					}
					break;
				}
				case LEGS:
				{
					player.removePotionEffect(MobEffects.BLINDNESS);
					player.removePotionEffect(MobEffects.NAUSEA);
					player.removePotionEffect(MobEffects.HUNGER);
					player.removePotionEffect(MobEffects.POISON);
					player.removePotionEffect(MobEffects.REGENERATION);
					player.removePotionEffect(MobEffects.INSTANT_HEALTH);
					break;
				}
				case FEET:
				{
					if (living.isEntityUndead())
					{
						living.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 10, 0, true, true));
					}
					break;
				}
				default:
					break;
				}
			}
		}
	}
	
	public int getMaxDamage()
    {
		return 0;
    }
	
    public void setDamage(ItemStack stack, int damage)
    {
    	super.setDamage(stack, 0);
    }
    
    private boolean nearbyNecromancerDetected(World world, EntityPlayer player)
    {
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(32D));
		boolean flag = false;
		for (Entity entity : list)
		{
			if (entity instanceof EntityNecromancer && entity.isEntityAlive())
			{
				flag = true;
			}
		}
    	return flag;
    }
}
