package net.endermanofdoom.necromancer.entity.friendly;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityHuskTame extends EntityZombieTame
{
	public EntityHuskTame(World worldIn) 
	{
		super(worldIn);
	}

	public EntityAgeable createChild(EntityAgeable ageable) 
	{
		return new EntityHuskTame(world);
	}

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer player)
    {
        return 0;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_HUSK_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_HUSK_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_HUSK_DEATH;
    }

    protected SoundEvent getStepSound()
    {
        return SoundEvents.ENTITY_HUSK_STEP;
    }
    protected boolean shouldBurnInDay()
    {
        return false;
    }

    protected ItemStack getSkullDrop()
    {
        return null;
    }
}