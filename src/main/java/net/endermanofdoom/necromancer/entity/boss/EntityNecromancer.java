package net.endermanofdoom.necromancer.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;

import net.endermanofdoom.necromancer.NItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityNecromancer extends EntitySpellcasterIllager
{
    private EntityVillager wololoTarget;
    private static final DataParameter<Float> EXTRA_LIVES = EntityDataManager.<Float>createKey(EntityNecromancer.class, DataSerializers.FLOAT);

    public EntityNecromancer(World worldIn)
    {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.enablePersistence();
        this.experienceValue = 50;
		if (!this.world.isRemote)
		for (EntityPlayer entityplayer : world.playerEntities)
		{
			world.playSound(null, entityplayer.getPosition(), SoundEvents.AMBIENT_CAVE, this.getSoundCategory(), getSoundVolume(), getSoundPitch());
			entityplayer.sendStatusMessage(new TextComponentTranslation("You feel an ominous presence infecting the land with it's dark magic."), true);
		}
    }

	protected void initEntityAI()
    {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityNecromancer.AICastingSpell());
        this.tasks.addTask(2, new EntityNecromancer.AIDeathRingSpell());
        this.tasks.addTask(3, new EntityNecromancer.AISummonZombiesSpell());
        this.tasks.addTask(4, new EntityNecromancer.AISummonSkeletonsSpell());
        this.tasks.addTask(5, new EntityNecromancer.AILifeStealSpell());
        this.tasks.addTask(6, new EntityNecromancer.AIConjureRandomUndeadPassiveSpell());
        this.tasks.addTask(7, new EntityNecromancer.AIInfectSpell());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.tasks.addTask(11, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityNecromancer.class}));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget<EntityIronGolem>(this, EntityIronGolem.class, false)).setUnseenMemoryTicks(300));
    }
	
    public void setDead() 
    {
    	if (world.getDifficulty() != EnumDifficulty.PEACEFUL || isDead || world.isRemote)
    	{
    		super.setDead();            
    		for (int i = 0; i < this.width + this.height * 50; ++i)
            {
                double d0 = (double)((float)this.getPosition().getX() + this.world.rand.nextFloat());
                double d1 = (double)((float)this.getPosition().getY() + this.world.rand.nextFloat());
                double d2 = (double)((float)this.getPosition().getZ() + this.world.rand.nextFloat());
                double d3 = d0 - posX;
                double d4 = d1 - posY;
                double d5 = d2 - posZ;
                double d6 = (double)MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                d3 = d3 / d6;
                d4 = d4 / d6;
                d5 = d5 / d6;
                double d7 = 0.5D / (d6 / (double)this.height + 0.1D);
                d7 = d7 * (double)(this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                d3 = d3 * d7;
                d4 = d4 * d7;
                d5 = d5 * d7;
                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (d0 + posX) / 2.0D, (d1 + posY + this.getEyeHeight()) / 2.0D, (d2 + posZ) / 2.0D, d3, d4, d5);
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, d3, d4, d5);
            }
    	}
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60D);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(EXTRA_LIVES, Float.valueOf(0F));
    }

    public float getExtraLives()
    {
        return ((Float)this.dataManager.get(EXTRA_LIVES)).floatValue();
    }

    public void setExtraLives(float count)
    {
        this.dataManager.set(EXTRA_LIVES, Float.valueOf(count));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        compound.setFloat("ExtraLives", getExtraLives());
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        this.setExtraLives(compound.getFloat("ExtraLives"));
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        Item item = Items.EMERALD;

        if (item != null)
        {
            int i = 2 + this.rand.nextInt(5);

            if (lootingModifier > 0)
            {
                i += this.rand.nextInt(lootingModifier + 1);
            }

            for (int j = 0; j < i; ++j)
            {
                this.dropItem(item, 1);
            }
        }
        
        this.dropItem(NItems.NECROMANCER_STAFF, 1);
        
        float f = rand.nextFloat();
        
        if (f >= 0.75F)
            this.dropItem(NItems.NECROMANCER_HOOD, 1);
        else if (f >= 0.5F)
            this.dropItem(NItems.NECROMANCER_ROBE, 1);
        else if (f >= 0.25F)
            this.dropItem(NItems.NECROMANCER_BELT, 1);
        else if (f >= 0F)
            this.dropItem(NItems.NECROMANCER_SHOES, 1);
    }

    protected void updateAITasks()
    {
        super.updateAITasks();

        if (this.getAttackTarget() != null)
        {
            this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), (float)this.getHorizontalFaceSpeed(), (float)this.getVerticalFaceSpeed());
            if (this.getDistance(getAttackTarget()) > 16D || !this.canEntityBeSeen(getAttackTarget()))
                this.getNavigator().tryMoveToEntityLiving(getAttackTarget(), 1D);
            else
                this.getNavigator().clearPath();

            List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(32D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
            {
                public boolean apply(@Nullable Entity p_apply_1_)
                {
                    return p_apply_1_ != null && p_apply_1_ instanceof EntityLiving && !(p_apply_1_ instanceof EntityTameable) && ((EntityLiving)p_apply_1_).isEntityUndead();
                }
            }));

            if (!list.isEmpty())
            {
                for (int l = 0; l < list.size(); ++l)
                {
                	EntityLiving entity = (EntityLiving) list.get(l);
                	
                	if (entity.getAttackTarget() != this.getAttackTarget())
                	{
                		entity.setAttackTarget(getAttackTarget());
                		entity.setRevengeTarget(getAttackTarget());
                	}
                }
            }
        }
        else if (this.getWololoTarget() != null)
        {
            this.getLookHelper().setLookPositionWithEntity(this.getWololoTarget(), (float)this.getHorizontalFaceSpeed(), (float)this.getVerticalFaceSpeed());
        }
        else
        {	
            List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(64D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
            {
                public boolean apply(@Nullable Entity p_apply_1_)
                {
                    return p_apply_1_ != null && p_apply_1_ instanceof EntityLiving && !(p_apply_1_ instanceof EntityTameable) && ((EntityLiving)p_apply_1_).isEntityUndead();
                }
            }));

            if (!list.isEmpty())
            {
                for (int l = 0; l < list.size(); ++l)
                {
                	EntityLiving entity = (EntityLiving) list.get(l);

                	if (entity.getAttackTarget() == null && entity.posY >= this.posY - 8D && entity.isNonBoss() && entity.getDistance(this) > 12D && entity.getNavigator().noPath())
                	{
                		entity.getNavigator().tryMoveToEntityLiving(this, 1D);
                	}
                }
            }
        }
    }

    /**
     * Returns true if this entity is undead.
     */
    public boolean isNonBoss()
    {
        return false;
    }

    /**
     * Returns true if this entity is undead.
     */
    public boolean isEntityUndead()
    {
        return true;
    }
    
    protected void siphon(EntityLivingBase p_82216_2_)
    {
        double d0 = this.posX;
        double d1 = this.posY + this.getEyeHeight();
        double d2 = this.posZ;
        EntityDarkBeam beam = new EntityDarkBeam(world, p_82216_2_, this, d0, d1, d2);
		beam.posX = d0;
		beam.posY = d1;
		beam.posZ = d2;
		beam.accelerationX = p_82216_2_.posX;
		beam.accelerationY = p_82216_2_.posY + (p_82216_2_.height * 0.5D);
		beam.accelerationZ = p_82216_2_.posZ;
		beam.targetEntity = p_82216_2_;
		this.world.spawnEntity(beam);
    }

    protected void onDeathUpdate()
    {
        if (this.getExtraLives() > 0F)
        {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() - 20D);
            this.setHealth(this.getMaxHealth());
            this.clearActivePotions();
            this.addPotionEffect(new PotionEffect(MobEffects.WITHER, 900, 1));
            this.setExtraLives(this.getExtraLives() - 1F);
            this.world.setEntityState(this, (byte)35);
        }
        else
        {
        	this.setDead();
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
            {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
        	
            List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(32D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
            {
                public boolean apply(@Nullable Entity p_apply_1_)
                {
                    return p_apply_1_ != null && p_apply_1_ instanceof EntityLiving && !(p_apply_1_ instanceof EntityTameable) && ((EntityLiving)p_apply_1_).isEntityUndead();
                }
            }));

            if (!list.isEmpty() && !this.isAIDisabled())
            {
                for (int l = 0; l < list.size(); ++l)
                {
                	EntityLiving entity = (EntityLiving) list.get(l);

                	if (entity.isNonBoss())
                	if (entity instanceof EntityCreature)
                	((EntityCreature)entity).targetTasks.addTask(0, (new EntityAINearestAttackableTarget<EntityLiving>((EntityCreature)entity, EntityLiving.class, true)));
                	else
                		entity.targetTasks.addTask(0, new EntityAIFindEntityNearest(entity, EntityLiving.class));
                }
            }
        }
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        
        if (this.getHealth() > 0F)
        {
            if (this.isBeingRidden())
            {
                for (Entity entity : this.getPassengers())
                {
                    for (int i = 0; i < 200; ++i)
                    {
                        float f1 = (float)i * (float)Math.PI * 2.0F / 200 + ((float)Math.PI * 2F / 5F);
                        if (world.isRemote)
        				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, entity.posX +  + (double)MathHelper.cos(f1) * 4D, entity.posY, entity.posZ + (double)MathHelper.sin(f1) * 4D, 0.0D, -0.025D, 0.0D, new int[0]);
                    }
                	
    				List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, entity.getEntityBoundingBox().grow(10D));
    				for(Entity effected : entities)
    				{
    					if(effected instanceof EntityLivingBase && !isOnSameTeam(effected) && entity.getEntityBoundingBox().intersects(effected.getEntityBoundingBox()))
    					{
    						if (effected.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entity, this), 6F - this.getDistance(effected)))
    						{
        						((EntityLivingBase) effected).addPotionEffect(new PotionEffect(MobEffects.WITHER, 120));
        						this.heal(2);
    						}
    					}
    				}
                }
            }
            
            if (!this.world.isRemote)
            {
            	if (this.getRevengeTarget() != null && this.getAttackTarget() != this.getRevengeTarget() && !this.isOnSameTeam(getRevengeTarget()))
            		this.setAttackTarget(getRevengeTarget());
            	
                if (world.getDifficulty() == EnumDifficulty.PEACEFUL)
                {
                	world.getMinecraftServer().setDifficultyForAllWorlds(EnumDifficulty.EASY);

            		for (EntityPlayer entityplayer : world.playerEntities)
            		{
            			world.playSound(null, entityplayer.getPosition(), SoundEvents.AMBIENT_CAVE, this.getSoundCategory(), getSoundVolume(), getSoundPitch());
            			entityplayer.sendStatusMessage(new TextComponentTranslation("An ominous presence darkens this peaceful world"), true);
            		}
                }

                List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(32D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
                {
                    public boolean apply(@Nullable Entity p_apply_1_)
                    {
                        return p_apply_1_ != null && p_apply_1_ instanceof EntityLiving && !(p_apply_1_ instanceof EntityTameable) && ((EntityLiving)p_apply_1_).isEntityUndead();
                    }
                }));

                if (!list.isEmpty())
                {
                    for (int l = 0; l < list.size(); ++l)
                    {
                    	EntityLiving entity = (EntityLiving) list.get(l);
                    	
                    	if (entity.isEntityAlive())
                    	{
                    		this.heal(0.001F);
                    	}
                    }
                }
            }
        }
    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    public boolean isOnSameTeam(Entity entityIn)
    {
        if (entityIn == null)
        {
            return false;
        }
        else if (entityIn == this)
        {
            return true;
        }
        else if (super.isOnSameTeam(entityIn))
        {
            return true;
        }
        else if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER)
        {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        else if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).isEntityUndead() && !(entityIn instanceof EntityTameable))
        {
            return this.getTeam() == null && entityIn.getTeam() == null;
        }
        else
        {
            return false;
        }
    }
    
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (source == DamageSource.WITHER)
        {
          this.heal(amount);
          return false;
        }
        else if (this.isOnSameTeam(source.getTrueSource()))
        {
            return false;
        }
        else
        {
        	return super.attackEntityFrom(source, amount);
        }
    }

    protected SoundEvent getAmbientSound()
    {
        return world.getClosestPlayerToEntity(this, 16D) == null ? (rand.nextFloat() <= 0.01F ? SoundEvents.AMBIENT_CAVE : null) : SoundEvents.VINDICATION_ILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
    	this.playSound(SoundEvents.ENTITY_VILLAGER_DEATH, this.getSoundVolume(), 0.65F);
    	this.playSound(SoundEvents.ENTITY_VILLAGER_DEATH, this.getSoundVolume(), 0.6F);
    	this.playSound(SoundEvents.ENTITY_VILLAGER_DEATH, this.getSoundVolume(), 0.55F);
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return world.getClosestPlayerToEntity(this, 16D) == null ? (rand.nextFloat() <= 0.01F ? SoundEvents.AMBIENT_CAVE : null) : SoundEvents.ENTITY_ILLUSION_ILLAGER_HURT;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 20F;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch()
    {
        return 0.5F;
    }

    private void setWololoTarget(@Nullable EntityVillager wololoTargetIn)
    {
        this.wololoTarget = wololoTargetIn;
    }

    @Nullable
    private EntityVillager getWololoTarget()
    {
        return this.wololoTarget;
    }

    protected SoundEvent getSpellSound()
    {
        return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return 1D;
    }    
    
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        this.setExtraLives(3F);
        return livingdata;
    }

    class AIDeathRingSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        private AIDeathRingSpell()
        {
            super();
        }
        
        public boolean shouldExecute()
        {
            if (EntityNecromancer.this.getAttackTarget() != null && EntityNecromancer.this.getDistance(EntityNecromancer.this.getAttackTarget()) > 5D)
            {
                return false;
            }
            else
            {
            	return super.shouldExecute();
            }
        }

        protected int getCastingTime()
        {
            return 40;
        }

        protected int getCastingInterval()
        {
            return 200;
        }

        protected void castSpell()
        {
        	EntityAreaEffectCloud ring = new EntityAreaEffectCloud(EntityNecromancer.this.world, EntityNecromancer.this.posX, EntityNecromancer.this.posY + (EntityNecromancer.this.height * 0.5D), EntityNecromancer.this.posZ);
        	ring.setOwner(EntityNecromancer.this);
        	ring.setRadius(4.0F);
        	ring.setDuration(100);
        	ring.setParticle(EnumParticleTypes.SUSPENDED_DEPTH);
        	ring.addEffect(new PotionEffect(MobEffects.WITHER));
        	ring.startRiding(EntityNecromancer.this);
        	EntityNecromancer.this.world.spawnEntity(ring);
        }

        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
        }

        protected EntitySpellcasterIllager.SpellType getSpellType()
        {
            return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
        }
    }

    class AILifeStealSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        private AILifeStealSpell()
        {
            super();
        }
        
        public boolean shouldExecute()
        {
            if (EntityNecromancer.this.getAttackTarget() != null && !EntityNecromancer.this.canEntityBeSeen(EntityNecromancer.this.getAttackTarget()))
            {
                return false;
            }
            else if (EntityNecromancer.this.getAttackTarget() != null && EntityNecromancer.this.getDistance(EntityNecromancer.this.getAttackTarget()) <= 5D)
            {
                return false;
            }
            else
            {
            	return super.shouldExecute();
            }
        }

        protected int getCastingTime()
        {
            return 10;
        }

        protected int getCastingInterval()
        {
            return EntityNecromancer.this.getHealth() >= EntityNecromancer.this.getMaxHealth() ? 240 : 80;
        }

        protected void castSpell()
        {
            EntityLivingBase entitylivingbase = EntityNecromancer.this.getAttackTarget();
            if (EntityNecromancer.this.getDistance(EntityNecromancer.this.getAttackTarget()) > 14D)
            {
                List<Entity> list = EntityNecromancer.this.world.getEntitiesInAABBexcluding(EntityNecromancer.this, EntityNecromancer.this.getEntityBoundingBox().grow(32D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
                {
                    public boolean apply(@Nullable Entity p_apply_1_)
                    {
                        return p_apply_1_ != null && p_apply_1_ instanceof EntityLiving && ((EntityLiving)p_apply_1_).isEntityUndead();
                    }
                }));

                if (!list.isEmpty())
                {
                    for (int l = 0; l < list.size(); ++l)
                    {
                    	EntityLiving entity = (EntityLiving) list.get(l);
                    	
                    	if (entity.isEntityAlive())
                    	{
                            EntityNecromancer.this.siphon(entity);
                            break;
                    	}
                    }
                }
            }
            else
            EntityNecromancer.this.siphon(entitylivingbase);
        }

        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.ENTITY_ZOMBIE_INFECT;
        }

        protected EntitySpellcasterIllager.SpellType getSpellType()
        {
            return EntitySpellcasterIllager.SpellType.FANGS;
        }
    }

    class AISummonZombiesSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        private AISummonZombiesSpell()
        {
            super();
        }

        protected int getCastingTime()
        {
            return 60;
        }

        protected int getCastingInterval()
        {
            return 600;
        }

        protected void castSpell()
        {
            EntityLivingBase entitylivingbase = EntityNecromancer.this.getAttackTarget();
            double d0 = Math.min(entitylivingbase.posY, EntityNecromancer.this.posY);
            double d1 = Math.max(entitylivingbase.posY, EntityNecromancer.this.posY) + 1.0D;
            float f = (float)MathHelper.atan2(entitylivingbase.posZ - EntityNecromancer.this.posZ, entitylivingbase.posX - EntityNecromancer.this.posX);

            int num = 5;
            for (int i = 0; i < num; ++i)
            {
                float f1 = f + (float)i * (float)Math.PI * 2.0F / num + ((float)Math.PI * 2F / 5F);
                this.spawnFangs(entitylivingbase.posX + (double)MathHelper.cos(f1) * 4D, entitylivingbase.posZ + (double)MathHelper.sin(f1) * 4D, d0, d1, f1, 0);
            }
        }

        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_)
        {
            BlockPos blockpos = new BlockPos(p_190876_1_, EntityNecromancer.this.posY, p_190876_3_);
            double d0 = 0.0D;
            EntityZombie entityevokerfangs;

        	if (world.provider.isNether())
        	{
    			entityevokerfangs = new EntityPigZombie(EntityNecromancer.this.world);
        	}
        	else
        	{
        		if (rand.nextFloat() > 0.5F)
        		{
            		if (world.getDifficulty().getDifficultyId() > 2 || world.isDaytime())
                		entityevokerfangs = new EntityHusk(EntityNecromancer.this.world);
            		else
            			entityevokerfangs = new EntityZombieVillager(EntityNecromancer.this.world);
        		}
        		else
        		{
            		if (world.getDifficulty().getDifficultyId() > 2 || world.isDaytime())
                		entityevokerfangs = new EntityHusk(EntityNecromancer.this.world);
            		else
            			entityevokerfangs = new EntityZombie(EntityNecromancer.this.world);
        		}
        	}
        	
            entityevokerfangs.copyLocationAndAnglesFrom(EntityNecromancer.this);
            entityevokerfangs.setLocationAndAngles(p_190876_1_, (double)blockpos.getY() + d0 - 1.5D, p_190876_3_, p_190876_9_, p_190876_10_);
            if (EntityNecromancer.this.getAttackTarget() != null)
            	entityevokerfangs.setAttackTarget(EntityNecromancer.this.getAttackTarget());
            entityevokerfangs.onInitialSpawn(EntityNecromancer.this.world.getDifficultyForLocation(blockpos), null);
            entityevokerfangs.motionY += 0.6D;
            entityevokerfangs.hurtResistantTime = 100;
            entityevokerfangs.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
            for (int i = 0; i < 10; ++i)
            entityevokerfangs.spawnExplosionParticle();
            EntityNecromancer.this.world.spawnEntity(entityevokerfangs);
        }

        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
        }

        protected EntitySpellcasterIllager.SpellType getSpellType()
        {
            return EntitySpellcasterIllager.SpellType.BLINDNESS;
        }
    }

    class AICastingSpell extends EntitySpellcasterIllager.AICastingApell
    {
        private AICastingSpell()
        {
            super();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            if (EntityNecromancer.this.getAttackTarget() != null)
            {
                EntityNecromancer.this.getLookHelper().setLookPositionWithEntity(EntityNecromancer.this.getAttackTarget(), (float)EntityNecromancer.this.getHorizontalFaceSpeed(), (float)EntityNecromancer.this.getVerticalFaceSpeed());
            }
            else if (EntityNecromancer.this.getWololoTarget() != null)
            {
                EntityNecromancer.this.getLookHelper().setLookPositionWithEntity(EntityNecromancer.this.getWololoTarget(), (float)EntityNecromancer.this.getHorizontalFaceSpeed(), (float)EntityNecromancer.this.getVerticalFaceSpeed());
            }
        }
    }

    class AISummonSkeletonsSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        private AISummonSkeletonsSpell()
        {
            super();
        }

        protected int getCastingTime()
        {
            return 60;
        }

        protected int getCastingInterval()
        {
            return 600;
        }

        protected void castSpell()
        {
            EntityLivingBase entitylivingbase = EntityNecromancer.this.getAttackTarget();
            double d0 = Math.min(entitylivingbase.posY, EntityNecromancer.this.posY);
            double d1 = Math.max(entitylivingbase.posY, EntityNecromancer.this.posY) + 1.0D;
            float f = (float)MathHelper.atan2(entitylivingbase.posZ - EntityNecromancer.this.posZ, entitylivingbase.posX - EntityNecromancer.this.posX);

            int num = 5;
            for (int i = 0; i < num; ++i)
            {
                float f1 = f + (float)i * (float)Math.PI * 2.0F / num + ((float)Math.PI * 2F / 5F);
                this.spawnFangs(EntityNecromancer.this.posX + (double)MathHelper.cos(f1) * 4D, EntityNecromancer.this.posZ + (double)MathHelper.sin(f1) * 4D, d0, d1, f1, 0);
            }
        }
        
        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_)
        {
            BlockPos blockpos = new BlockPos(p_190876_1_, EntityNecromancer.this.posY, p_190876_3_);
            double d0 = 0.0D;
            AbstractSkeleton entityevokerfangs;

            	if (world.provider.isNether())
            	{
            		if (rand.nextFloat() > 0.2F)
                		entityevokerfangs = new EntityWitherSkeleton(EntityNecromancer.this.world);
            		else
            			entityevokerfangs = new EntitySkeleton(EntityNecromancer.this.world);
            	}
            	else
            	{
            		if (world.getDifficulty().getDifficultyId() > 2)
                		entityevokerfangs = new EntityStray(EntityNecromancer.this.world);
            		else
            			entityevokerfangs = new EntitySkeleton(EntityNecromancer.this.world);
            	}
            	
                entityevokerfangs.copyLocationAndAnglesFrom(EntityNecromancer.this);
                entityevokerfangs.setLocationAndAngles(p_190876_1_, (double)blockpos.getY() + d0 - 1.5D, p_190876_3_, p_190876_9_, p_190876_10_);
                if (EntityNecromancer.this.getAttackTarget() != null)
                	entityevokerfangs.setAttackTarget(EntityNecromancer.this.getAttackTarget());
                entityevokerfangs.onInitialSpawn(EntityNecromancer.this.world.getDifficultyForLocation(blockpos), null);
                if (entityevokerfangs instanceof EntityWitherSkeleton)
                    entityevokerfangs.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                entityevokerfangs.motionY += 0.6D;
                entityevokerfangs.hurtResistantTime = 100;
                entityevokerfangs.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                for (int i = 0; i < 10; ++i)
                entityevokerfangs.spawnExplosionParticle();
                EntityNecromancer.this.world.spawnEntity(entityevokerfangs);
        }

        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
        }

        protected EntitySpellcasterIllager.SpellType getSpellType()
        {
            return EntitySpellcasterIllager.SpellType.BLINDNESS;
        }
    }

    public class AIConjureRandomUndeadPassiveSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        public AIConjureRandomUndeadPassiveSpell()
        {
            super();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (EntityNecromancer.this.isSpellcasting())
            {
                return false;
            }
            else if (EntityNecromancer.this.ticksExisted < this.spellCooldown)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return this.spellWarmup > 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            super.resetTask();
        }

		protected void castSpell()
        {
            this.spawnFangs(EntityNecromancer.this.posX + (rand.nextDouble() * 5D - 2.5D), EntityNecromancer.this.posZ + (rand.nextDouble() * 5D - 2.5D));
        }

        protected int getCastingTime()
        {
            return 20;
        }

        protected int getCastingInterval()
        {
            return EntityNecromancer.this.getAttackTarget() != null ? 100 : 2000;
        }

        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
        }

        protected EntitySpellcasterIllager.SpellType getSpellType()
        {
            return EntitySpellcasterIllager.SpellType.BLINDNESS;
        }

        private void spawnFangs(double p_190876_1_, double p_190876_3_)
        {
            BlockPos blockpos = new BlockPos(p_190876_1_, EntityNecromancer.this.posY, p_190876_3_);
            double d0 = 0.0D;

            	if (world.provider.isNether())
            	{
            		if (rand.nextBoolean())
            		{
                        EntityWitherSkeleton entityevokerfangs = new EntityWitherSkeleton(EntityNecromancer.this.world);
                        entityevokerfangs.copyLocationAndAnglesFrom(EntityNecromancer.this);
                        entityevokerfangs.setLocationAndAngles(p_190876_1_, (double)blockpos.getY() + d0 - 1.5D, p_190876_3_, 0, 0);
                        if (EntityNecromancer.this.getAttackTarget() != null)
                        {
                        	entityevokerfangs.setAttackTarget(EntityNecromancer.this.getAttackTarget());
                        	entityevokerfangs.setRevengeTarget(EntityNecromancer.this.getAttackTarget());
                        }
                        entityevokerfangs.onInitialSpawn(EntityNecromancer.this.world.getDifficultyForLocation(blockpos), null);
                        entityevokerfangs.motionY += 0.6D;
                        entityevokerfangs.hurtResistantTime = 100;
                        entityevokerfangs.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                        for (int i = 0; i < 10; ++i)
                        entityevokerfangs.spawnExplosionParticle();
                        EntityNecromancer.this.world.spawnEntity(entityevokerfangs);
            		}
            		else
            		{
                        EntityPigZombie entityevokerfangs = new EntityPigZombie(EntityNecromancer.this.world);
                        entityevokerfangs.copyLocationAndAnglesFrom(EntityNecromancer.this);
                        entityevokerfangs.setLocationAndAngles(p_190876_1_, (double)blockpos.getY() + d0 - 1.5D, p_190876_3_, 0, 0);
                        if (EntityNecromancer.this.getAttackTarget() != null)
                        {
                        	entityevokerfangs.setAttackTarget(EntityNecromancer.this.getAttackTarget());
                        	entityevokerfangs.setRevengeTarget(EntityNecromancer.this.getAttackTarget());
                        }
                        entityevokerfangs.onInitialSpawn(EntityNecromancer.this.world.getDifficultyForLocation(blockpos), null);
                        entityevokerfangs.setChild(false);
                        entityevokerfangs.motionY += 0.6D;
                        entityevokerfangs.hurtResistantTime = 100;
                        entityevokerfangs.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                        for (int i = 0; i < 10; ++i)
                        entityevokerfangs.spawnExplosionParticle();
                        EntityNecromancer.this.world.spawnEntity(entityevokerfangs);
            		}
            	}
            	else
            	{
            		switch (rand.nextInt(4))
            		{
            		case 1:
            		{
                        EntityHusk entityevokerfangs = new EntityHusk(EntityNecromancer.this.world);
                        entityevokerfangs.copyLocationAndAnglesFrom(EntityNecromancer.this);
                        entityevokerfangs.setLocationAndAngles(p_190876_1_, (double)blockpos.getY() + d0 - 1.5D, p_190876_3_, 0, 0);
                        if (EntityNecromancer.this.getAttackTarget() != null)
                        {
                        	entityevokerfangs.setAttackTarget(EntityNecromancer.this.getAttackTarget());
                        	entityevokerfangs.setRevengeTarget(EntityNecromancer.this.getAttackTarget());
                        }
                        entityevokerfangs.onInitialSpawn(EntityNecromancer.this.world.getDifficultyForLocation(blockpos), null);
                        entityevokerfangs.setChild(false);
                        entityevokerfangs.motionY += 0.6D;
                        entityevokerfangs.hurtResistantTime = 100;
                        entityevokerfangs.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                        for (int i = 0; i < 10; ++i)
                        entityevokerfangs.spawnExplosionParticle();
                        EntityNecromancer.this.world.spawnEntity(entityevokerfangs);
                        break;
            		}
            		case 2:
            		{
                        EntitySkeleton entityevokerfangs = new EntitySkeleton(EntityNecromancer.this.world);
                        entityevokerfangs.copyLocationAndAnglesFrom(EntityNecromancer.this);
                        entityevokerfangs.setLocationAndAngles(p_190876_1_, (double)blockpos.getY() + d0 - 1.5D, p_190876_3_, 0, 0);
                        if (EntityNecromancer.this.getAttackTarget() != null)
                        {
                        	entityevokerfangs.setAttackTarget(EntityNecromancer.this.getAttackTarget());
                        	entityevokerfangs.setRevengeTarget(EntityNecromancer.this.getAttackTarget());
                        }
                        entityevokerfangs.onInitialSpawn(EntityNecromancer.this.world.getDifficultyForLocation(blockpos), null);
                        entityevokerfangs.motionY += 0.6D;
                        entityevokerfangs.hurtResistantTime = 100;
                        entityevokerfangs.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                        for (int i = 0; i < 10; ++i)
                        entityevokerfangs.spawnExplosionParticle();
                        EntityNecromancer.this.world.spawnEntity(entityevokerfangs);
                        break;
            		}
            		case 3:
            		{
                        EntityStray entityevokerfangs = new EntityStray(EntityNecromancer.this.world);
                        entityevokerfangs.copyLocationAndAnglesFrom(EntityNecromancer.this);
                        entityevokerfangs.setLocationAndAngles(p_190876_1_, (double)blockpos.getY() + d0 - 1.5D, p_190876_3_, 0, 0);
                        if (EntityNecromancer.this.getAttackTarget() != null)
                        {
                        	entityevokerfangs.setAttackTarget(EntityNecromancer.this.getAttackTarget());
                        	entityevokerfangs.setRevengeTarget(EntityNecromancer.this.getAttackTarget());
                        }
                        entityevokerfangs.onInitialSpawn(EntityNecromancer.this.world.getDifficultyForLocation(blockpos), null);
                        entityevokerfangs.motionY += 0.6D;
                        entityevokerfangs.hurtResistantTime = 100;
                        entityevokerfangs.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                        for (int i = 0; i < 10; ++i)
                        entityevokerfangs.spawnExplosionParticle();
                        EntityNecromancer.this.world.spawnEntity(entityevokerfangs);
                        break;
            		}
            		default:
            		{
            			EntityZombieVillager entityevokerfangs = new EntityZombieVillager(EntityNecromancer.this.world);
                        entityevokerfangs.copyLocationAndAnglesFrom(EntityNecromancer.this);
                        entityevokerfangs.setLocationAndAngles(p_190876_1_, (double)blockpos.getY() + d0 - 1.5D, p_190876_3_, 0, 0);
                        if (EntityNecromancer.this.getAttackTarget() != null)
                        {
                        	entityevokerfangs.setAttackTarget(EntityNecromancer.this.getAttackTarget());
                        	entityevokerfangs.setRevengeTarget(EntityNecromancer.this.getAttackTarget());
                        }
                        entityevokerfangs.onInitialSpawn(EntityNecromancer.this.world.getDifficultyForLocation(blockpos), null);
                        entityevokerfangs.setChild(false);
                        entityevokerfangs.motionY += 0.6D;
                        entityevokerfangs.hurtResistantTime = 100;
                        entityevokerfangs.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                        for (int i = 0; i < 10; ++i)
                        entityevokerfangs.spawnExplosionParticle();
                        EntityNecromancer.this.world.spawnEntity(entityevokerfangs);
                        break;
            		}
            	}
            }
        }
    }

    public class AIInfectSpell extends EntitySpellcasterIllager.AIUseSpell
    {
        final Predicate<EntityVillager> wololoSelector = new Predicate<EntityVillager>()
        {
            public boolean apply(EntityVillager p_apply_1_)
            {
                return p_apply_1_.isEntityAlive();
            }
        };

        public AIInfectSpell()
        {
            super();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (EntityNecromancer.this.getAttackTarget() != null)
            {
                return false;
            }
            else if (EntityNecromancer.this.isSpellcasting())
            {
                return false;
            }
            else if (EntityNecromancer.this.ticksExisted < this.spellCooldown)
            {
                return false;
            }
            else if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(EntityNecromancer.this.world, EntityNecromancer.this))
            {
                return false;
            }
            else
            {
                List<EntityVillager> list = EntityNecromancer.this.world.<EntityVillager>getEntitiesWithinAABB(EntityVillager.class, EntityNecromancer.this.getEntityBoundingBox().grow(32D), this.wololoSelector);

                if (list.isEmpty())
                {
                    return false;
                }
                else
                {
                    EntityNecromancer.this.setWololoTarget(list.get(EntityNecromancer.this.rand.nextInt(list.size())));
                    return true;
                }
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            return EntityNecromancer.this.getWololoTarget() != null && this.spellWarmup > 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            super.resetTask();
            EntityNecromancer.this.setWololoTarget((EntityVillager)null);
        }

        @SuppressWarnings("deprecation")
		protected void castSpell()
        {
        	EntityVillager entitysheep = EntityNecromancer.this.getWololoTarget();

            if (entitysheep != null && entitysheep.isEntityAlive())
            {
                EntityZombieVillager entityzombievillager = new EntityZombieVillager(entitysheep.world);
                entityzombievillager.copyLocationAndAnglesFrom(entitysheep);
                world.removeEntity(entitysheep);
                entityzombievillager.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityzombievillager)), null);
                entityzombievillager.setProfession(entitysheep.getProfession());
                entityzombievillager.setChild(entitysheep.isChild());
                entityzombievillager.setNoAI(entitysheep.isAIDisabled());

                if (entitysheep.hasCustomName())
                {
                    entityzombievillager.setCustomNameTag(entitysheep.getCustomNameTag());
                    entityzombievillager.setAlwaysRenderNameTag(entitysheep.getAlwaysRenderNameTag());
                }

                entitysheep.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, true));
                for (int i = 0; i < 10; ++i)
                entitysheep.spawnExplosionParticle();
                entitysheep.world.spawnEntity(entityzombievillager);
                entitysheep.world.playEvent((EntityPlayer)null, 1026, new BlockPos(entitysheep), 0);
            }
        }

        protected int getCastWarmupTime()
        {
            return 40;
        }

        protected int getCastingTime()
        {
            return 40;
        }

        protected int getCastingInterval()
        {
            return 100;
        }

        protected SoundEvent getSpellPrepareSound()
        {
            return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
        }

        protected EntitySpellcasterIllager.SpellType getSpellType()
        {
            return EntitySpellcasterIllager.SpellType.BLINDNESS;
        }
    }
}