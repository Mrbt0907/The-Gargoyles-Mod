package net.endermanofdoom.necromancer.item;

import java.util.List;
import javax.annotation.Nullable;

import net.endermanofdoom.necromancer.entity.friendly.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNecromancerStaff extends Item
{
	public ItemNecromancerStaff(String name) 
	{
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.COMBAT);
	}
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced)
    {
        tooltip.add(TextFormatting.GOLD + "Pulsing with Dark Magic and darkness." + TextFormatting.WHITE);
        tooltip.add(TextFormatting.AQUA + "Right click to summon a random undead mob near you." + TextFormatting.WHITE);
        tooltip.add(TextFormatting.RED + "Has a very small chance of summoning a Wither." + TextFormatting.WHITE);
    }
	
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
    	if (worldIn.isRemote && itemstack.getItemDamage() < itemstack.getMaxDamage() && playerIn.getCooldownTracker().getCooldown(itemstack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks()) <= 0)
        playerIn.swingArm(handIn);
        
    	if (worldIn.isRemote || itemstack.getItemDamage() >= itemstack.getMaxDamage())
    	{
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    	}
    	else
    	{
    		float chance = playerIn.getRNG().nextFloat() * 1000F;
    		double x = playerIn.posX + (playerIn.getRNG().nextDouble() * 5D - 2.5D);
    		double z = playerIn.posZ + (playerIn.getRNG().nextDouble() * 5D - 2.5D);
    		EntityTameable summon = null;
    		
    		if (chance <= 1F)
    		{
    			EntityWither backfire = new EntityWither(worldIn);
    			backfire.copyLocationAndAnglesFrom(playerIn);
    			backfire.setLocationAndAngles(x, playerIn.posY - 1.5D, z, 0, 0);
    			backfire.onInitialSpawn(playerIn.world.getDifficultyForLocation(playerIn.getPosition()), null);
    			backfire.motionY += 0.6D;
    			backfire.hurtResistantTime = 40;
                backfire.setAttackTarget(playerIn);
                backfire.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 1F, 1F);
                playerIn.world.spawnEntity(backfire);
    		}
    		else if (chance <= 50F && chance > 1F)
    		{
                summon = new EntityWitherSkeletonTame(playerIn.world);
    		}
    		else if (chance <= 100F && chance > 50F)
    		{
                summon = new EntityPigZombieTame(playerIn.world);
    		}
    		else if (chance <= 200F && chance > 100F)
    		{
                summon = new EntityStrayTame(playerIn.world);
    		}
    		else if (chance <= 300F && chance > 200F)
    		{
                summon = new EntityHuskTame(playerIn.world);
    		}
    		else if (chance <= 600F && chance > 300F)
    		{
                summon = new EntitySkeletonTame(playerIn.world);
    		}
    		else if (chance > 600F)
    		{
                summon = new EntityZombieTame(playerIn.world);
    		}
    		
    		if (summon != null)
    		{
                summon.copyLocationAndAnglesFrom(playerIn);
                summon.setLocationAndAngles(x, playerIn.posY - 1.5D, z, 0, 0);
                summon.onInitialSpawn(playerIn.world.getDifficultyForLocation(playerIn.getPosition()), null);
                summon.motionY += 0.6D;
                summon.hurtResistantTime = 40;
                for (int i = 0; i < 10; ++i)
                summon.spawnExplosionParticle();
                playerIn.world.spawnEntity(summon);
                if (playerIn.getLastAttackedEntity() != null)
                	summon.setAttackTarget(playerIn.getLastAttackedEntity());
                if (playerIn.getRevengeTarget() != null)
                	summon.setAttackTarget(playerIn.getRevengeTarget());
                summon.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 1F, 1F);
                summon.setOwnerId(playerIn.getUniqueID());
                if (!playerIn.capabilities.isCreativeMode)
                {
                    itemstack.damageItem(1, playerIn);
                	playerIn.getCooldownTracker().setCooldown(this, 80);
                }
    		}

            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    	}
    }
}
