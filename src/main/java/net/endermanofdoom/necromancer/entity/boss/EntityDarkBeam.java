package net.endermanofdoom.necromancer.entity.boss;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDarkBeam extends EntityFireball
{
	public Entity targetEntity;
	
	public EntityDarkBeam(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setNoGravity(true);
	}

	public EntityDarkBeam(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		this(worldIn);
		this.shootingEntity = shooter;
		this.setLocationAndAngles(accelX, accelY, accelZ, shooter.rotationYaw, shooter.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.accelerationX = accelX;
		this.accelerationY = accelY;
		this.accelerationZ = accelZ;
	}
	public EntityDarkBeam(World worldIn, Entity target, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		this(worldIn);
		this.shootingEntity = shooter;
		this.setLocationAndAngles(accelX, accelY, accelZ, shooter.rotationYaw, shooter.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.targetEntity = target;
		this.accelerationX = accelX;
		this.accelerationY = accelY;
		this.accelerationZ = accelZ;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
	}

	public EntityDarkBeam(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		this(worldIn);
		this.setLocationAndAngles(accelX, accelY, accelZ, 0F, 0F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.accelerationX = accelX;
		this.accelerationY = accelY;
		this.accelerationZ = accelZ;
	}
	protected EnumParticleTypes getParticleType()
	{
		return EnumParticleTypes.SMOKE_NORMAL;
	}

	/**
	* Called when this EntityFireball hits a block or entity.
	*/
	protected void onImpact(RayTraceResult result)
	{
	}
	public boolean isBurning()
	{
		return false;
	}

	/**
	* Returns true if other Entities should be prevented from moving through this Entity.
	*/
	public boolean canBeCollidedWith()
	{
		return false;
	}

	/**
	* Called when the entity is attacked.
	*/
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}
	protected float getMotionFactor()
	{
		return 0.99F;
	}
	public void onUpdate()
	{
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setNoGravity(true);
		this.onEntityUpdate();
		if (this.targetEntity != null && !this.targetEntity.isEntityAlive())
		this.targetEntity = null;
		this.motionX *= 0D;
		this.motionY *= 0D;
		this.motionZ *= 0D;
		if (this.ticksExisted == 1)
		{
			this.accelerationX = this.posX;
			this.accelerationY = this.posY;
			this.accelerationZ = this.posZ;
		}
		if (this.ticksExisted > 1)
		{
			if (targetEntity != null)
				this.setPositionAndUpdate(targetEntity.posX, targetEntity.posY + (targetEntity.height * 0.5D), targetEntity.posZ);

			short short1 = (short)(int)getDistanceSq(this.accelerationX, this.accelerationY, this.accelerationZ);
			for (int i = 0; i < short1; i++)
			{
				double d9 = i / (short1 - 1.0D);
				double d6 = this.posX + (this.posX - this.accelerationX) * -d9;
				double d7 = this.posY + (this.posY - this.accelerationY) * -d9;
				double d8 = this.posZ + (this.posZ - this.accelerationZ) * -d9;
				if (getParticleType() != null && world.isRemote)
				this.world.spawnParticle(this.getParticleType(), true, d6, d7, d8, 0.0D, -0.025D, 0.0D, new int[0]);
			}
		}

		if (this.ticksExisted > 5)
		{
			if (this.shootingEntity != null && targetEntity != null && this.targetEntity instanceof EntityLivingBase && this.shootingEntity instanceof EntityLivingBase && this.getDistance(targetEntity.posX, posY, targetEntity.posZ) <= 1D && shootingEntity.canEntityBeSeen(targetEntity))
			{
				if(targetEntity != shootingEntity)
				{
					((EntityLivingBase) targetEntity).setHealth(((EntityLivingBase) targetEntity).getHealth() - 4F);
					if (shootingEntity.getHealth() >= shootingEntity.getMaxHealth())
						shootingEntity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shootingEntity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + 4D);
					else
						shootingEntity.heal(4F);
					if (((EntityLivingBase) targetEntity).getHealth() <= 0)
						((EntityLivingBase) targetEntity).onDeath(DamageSource.MAGIC);
                    this.world.setEntityState(targetEntity, (byte) 2);
				}
				List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(targetEntity, new AxisAlignedBB(this.getPosition()).grow(3D));
				for(Entity entity : entities)
				{
					if(entity instanceof EntityLivingBase && entity != shootingEntity)
					{
						((EntityLivingBase) entity).setHealth(((EntityLivingBase) entity).getHealth() - 2F);
						if (shootingEntity.getHealth() >= shootingEntity.getMaxHealth())
							shootingEntity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(shootingEntity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + 2D);
						else
							shootingEntity.heal(2F);
						if (((EntityLivingBase) entity).getHealth() <= 0)
							((EntityLivingBase) entity).onDeath(DamageSource.MAGIC);
					}
				}
			}
			this.setDead();
		}

		if (this.ticksExisted > 20 || (!this.world.isRemote && this.targetEntity == null))
		this.setDead();
	}
}