package net.gargoyles.entity;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.endermanofdoom.mac.enums.EnumGender;
import net.endermanofdoom.mac.enums.EnumLevel;
import net.endermanofdoom.mac.interfaces.IGendered;
import net.endermanofdoom.mac.interfaces.IMobTier;
import net.endermanofdoom.mac.interfaces.IVariedMob;
import net.endermanofdoom.mca.EnumSoundType;
import net.endermanofdoom.mca.ISoundSupport;
import net.gargoyles.blocks.BlockPerch;
import net.gargoyles.registry.GBlocks;
import net.gargoyles.registry.GSounds;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class EntityGargoyle extends EntityIronGolem implements IMobTier, IVariedMob, IGendered, ISoundSupport
{
    private static final Set<Block> BLACKLISTEDBLOCKS = Sets.<Block>newIdentityHashSet();
	   public double waypointX;
	   public double waypointY;
	   public double waypointZ;
	    private int clientSideAttackTime;
	    private EntityLivingBase targetedEntity;
	    private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityGargoyle.class, DataSerializers.VARINT);
	    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.<Integer>createKey(EntityGargoyle.class, DataSerializers.VARINT);
	    private static final DataParameter<Integer> TYPE = EntityDataManager.<Integer>createKey(EntityGargoyle.class, DataSerializers.VARINT);
	    private static final Predicate<EntityLiving> ATTACKABLE = new Predicate<EntityLiving>()
	    {
	        public boolean apply(@Nullable EntityLiving p_apply_1_)
	        {
	            return p_apply_1_ != null && p_apply_1_ instanceof IMob && p_apply_1_.getCustomNameTag().isEmpty() && ((EntityLivingBase)p_apply_1_).attackable();
	        }
	    };
	    
    @SuppressWarnings({ })
	public EntityGargoyle(World p_i1694_1_)
    {
        super(p_i1694_1_);
        this.enablePersistence();
        this.setSize(0.9F, 2.4F);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityGargoyle.AIPerch());
        this.tasks.addTask(0, new EntityGargoyle.AIBeamAttack());
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(5, new EntityAILookAtVillager(this));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIDefendVillage(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, false, true, new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
                return p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper);
            }
        }));
    }
    
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
    	if (entitylivingbaseIn == null)
    		super.setAttackTarget(null);
    	else if ((entitylivingbaseIn instanceof EntityLiving && this.getRevengeTarget() == null && !((EntityLiving)entitylivingbaseIn).getCustomNameTag().isEmpty()))
    		super.setAttackTarget(null);
        else
        	super.setAttackTarget(entitylivingbaseIn);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        
        if (this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() != this.getMobHealth())
        {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMobHealth());
            this.setHealth((float) getMobHealth());
        }
        if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() != this.getMobAttack())
        	this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(getMobAttack());

        if (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() != this.getMobSpeed())
        	this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getMobSpeed());
        
        if (this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue() != 48D)
        	this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48D);
    }
    
    protected ResourceLocation getLootTable()
    {
        return null;
    }
    
    public float getEyeHeight()
    {
        return !this.onGround ? 1.4F : ((this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - 0.5D), MathHelper.floor(this.posZ))).getBlock() == this.getFavoriteBlockToPerch()) ? 0.875F + (this.rotationPitch / 40) : 2.1F);
    }
    
    public float getAttackAnimationScale(float p_175477_1_)
    {
        return ((float)this.clientSideAttackTime + p_175477_1_) / 80F;
    }
    
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(STATE, Integer.valueOf(-1));
        this.dataManager.register(TYPE, Integer.valueOf(0));
        this.dataManager.register(TARGET_ENTITY, Integer.valueOf(0));
    }
    
    public int getState()
    {
        return ((Integer)this.dataManager.get(STATE)).intValue();
    }

    public void setState(int state)
    {
        this.dataManager.set(STATE, Integer.valueOf(state));
    }
    
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
    	super.readEntityFromNBT(tagCompund);
    	if (tagCompund.hasKey("Variant", 99))
    	{
    		setVariant(tagCompund.getByte("Variant"));
    	}
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);

        if (TARGET_ENTITY.equals(key))
        {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }
    
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("Variant", (byte)getVariant());
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }
    
    public boolean canAttackClass(@SuppressWarnings("rawtypes") Class cls)
    {
        return this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(cls) ? false : cls != EntityIronGolem.class && cls != EntityGargoyle.class;
    }

    protected void collideWithEntity(Entity p_82167_1_)
    {
        if (p_82167_1_ instanceof EntityGargoyle && this.getAttackTarget() == null && ((EntityGargoyle)p_82167_1_).getAttackTarget() == null)
        {
        	if (this.onGround && ((EntityGargoyle)p_82167_1_).onGround && this.getDistanceSq(((EntityGargoyle)p_82167_1_).waypointX, ((EntityGargoyle)p_82167_1_).waypointY, ((EntityGargoyle)p_82167_1_).waypointZ) < 4D)
        	{
        		++this.waypointY;
        		this.noClip = false;
        	}
        }

        super.collideWithEntity(p_82167_1_);
    }
    
    public int getMaxFallHeight()
    {
        return 256;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        
        if (this.collidedHorizontally)
        	this.motionY = 0.4D;
        
        if (this.isEntityAlive())
        {
            if (this.getTargetedEntity() != null)
            {
                this.setState(1);
            }
            else
            {
            	if (this.getVillage() != null || this.world.getBlockState(new BlockPos((int)waypointX, (int)waypointY, (int)waypointZ)).getBlock() == this.getFavoriteBlockToPerch())
            	{
                    this.setState(0);
            	}
            	else
            	{
                    this.setState(-1);
            	}
            }
        }		
        
        if (getMaxHealth() != this.getMobHealth() && this.getHealth() > 0)
		{
        	this.setHealth((float) getMobHealth());
        	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMobHealth());
		}

        if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() != this.getMobAttack())
        	this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getMobAttack());
        
        BlockPos pos = new BlockPos((int)waypointX, (int)waypointY, (int)waypointZ);
        Block block = this.world.getBlockState(pos).getBlock();
        
        if (this.getVariant() == 6)
        {
            this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
        }
        
        if (this.getVillage() != null && !this.world.isRemote && this.getAttackTarget() == null && this.getDistanceSq(this.getVillage().getCenter().getX(), this.getVillage().getCenter().getY(), this.getVillage().getCenter().getZ()) > 32D * 32D)
        {
        	   /* 210 */         double d0 = this.getVillage().getCenter().getX() - this.posX;
        	   /* 211 */         double d1 = this.getVillage().getCenter().getZ() - this.posZ;
        	   /* 212 */         double d3 = d0 * d0 + d1 * d1;
        	            
        		   /* 200 */         if (this.posY <= this.getVillage().getCenter().getY() + 1D)
        	/* 207 */           this.motionY += 0.6D - this.motionY;
        	   /* 216 */           double d5 = MathHelper.sqrt(d3);
        	   /* 217 */           this.motionX += d0 / d5 * 0.6D - this.motionX;
        	   /* 218 */           this.motionZ += d1 / d5 * 0.6D - this.motionZ;
        }
        
        if (this.getVillage() != null && !this.world.isRemote && this.getAttackTarget() == null)
        {
        	List<EntityLiving> list = this.world.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double)(this.getVillage().getCenter().getX() - this.getVillage().getVillageRadius()), (double)(this.getVillage().getCenter().getY() - 4D), (double)(this.getVillage().getCenter().getZ() - this.getVillage().getVillageRadius()), (double)(this.getVillage().getCenter().getX() + this.getVillage().getVillageRadius()), (double)(this.getVillage().getCenter().getY() + 4D), (double)(this.getVillage().getCenter().getZ() + this.getVillage().getVillageRadius())), Predicates.and(ATTACKABLE, EntitySelectors.NOT_SPECTATING));

            if (list != null && !list.isEmpty() && this.rand.nextInt(5) == 0)
            {
                for (int i = 0; i < list.size(); ++i)
                {
                	EntityLiving entity = (EntityLiving)list.get(this.rand.nextInt(list.size()));

                    if (this.isEntityAlive() && entity.isEntityAlive() && this.canAttackClass(entity.getClass()) && entity instanceof IMob)
                        this.setAttackTarget((EntityLivingBase)entity);
                }
            }
        }
        
        if (!this.world.isRemote && this.getAttackTarget() == null)
        {
        	List<EntityLiving> list = this.world.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), Predicates.and(ATTACKABLE, EntitySelectors.NOT_SPECTATING));

            if (list != null && !list.isEmpty() && this.rand.nextInt(10) == 0)
            {
                for (int i = 0; i < list.size(); ++i)
                {
                	EntityLiving entity = (EntityLiving)list.get(this.rand.nextInt(list.size()));

                    if (this.isEntityAlive() && entity.isEntityAlive() && this.canAttackClass(entity.getClass()) && entity instanceof IMob && this.canEntityBeSeen(entity))
                        this.setAttackTarget((EntityLivingBase)entity);
                }
            }
        }
        
        if (this.getLeashHolder() != null)
        	this.getLookHelper().setLookPositionWithEntity(getLeashHolder(), 180F, 180F);
        
        if ((this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive()))
            this.setAttackTarget(null);

        if (this.getAttackTarget() == null && this.getNatureBlock(this.world.getBlockState(pos)))
        {
        	   /* 210 */         double d0 = this.waypointX - this.posX;
        	   /* 210 */         double d1 = this.waypointY + 1 - this.posY;
        	   /* 211 */         double d2 = this.waypointZ - this.posZ;
        	   /* 212 */         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        	            
        	   if (d3 == 4D)
            	   this.renderYawOffset = this.rotationYaw = this.rotationYawHead += 180F;
        	   
	            if (d3 > 3D)
	            {
	         	   double d5 = MathHelper.sqrt(d3);
	        	   this.motionX += d0 / d5 * 0.75D - this.motionX;
	        	   this.motionY += d1 / d5 * 0.75D - this.motionY;
	        	   this.motionZ += d2 / d5 * 0.75D - this.motionZ;
	        	   this.getLookHelper().setLookPosition(waypointX, waypointY, waypointZ, 180F, 0F);
	        	   this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
	        	   this.noClip = true;
	           }
        	   else
        	   {
        		   this.getNavigator().clearPath();
        		   this.setLocationAndAngles(waypointX + 0.5D, waypointY + (block instanceof BlockPerch ? 0.8D : 1D), waypointZ + 0.5D, this.rotationYawHead, 40F);
            	   this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
            	   this.noClip = false;
            	   this.extinguish();
            	   if ((this.ticksExisted + this.getEntityId()) % (this.world.getBlockState(pos).getBlock() == this.getFavoriteBlockToPerch() ? 20 : 40) == 0)
            		   this.heal(this.world.getBlockState(pos).getBlock() == this.getFavoriteBlockToPerch() ? 2F : 1F);
        	   }
        }
        else
        	this.noClip = false;
        
        Entity entity = this.getAttackTarget();
        double move = entity != null ? 0.6D + (this.rand.nextDouble() * 0.2D) : 0.8D;
        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }
        /* 194 */     if (!this.world.isRemote && entity != null)
        {
   /* 210 */         double d0 = entity.posX - this.posX;
   /* 211 */         double d1 = entity.posZ - this.posZ;
   /* 212 */         double d3 = d0 * d0 + d1 * d1;
            
   /* 214 */         if (!this.world.isRemote && this.isEntityAlive() && this.getDistanceSq(entity) <= ((entity.width * entity.width) + (this.width * this.width) + 16D) && (this.ticksExisted + this.getEntityId()) % 20 == 0 && this.canEntityBeSeen(entity))
   {
/* 200 */         this.attackEntityAsMob(entity);
/* 219 */           this.getLookHelper().setLookPositionWithEntity(entity, 180F, 40F);
   }
   
   /* 214 */         if ((d3 > (this.getVariant() == 3 ? 512D : 1D)) && (this.canEntityBeSeen(entity) || this.isEntityInsideOpaqueBlock() || this.posY <= 0D || this.posY <= entity.posY || this.rand.nextInt(10) == 0))
            {
	   /* 200 */         if (this.posY <= entity.posY + 1D)
/* 207 */           this.motionY += 0.8D - this.motionY;
   /* 216 */           double d5 = MathHelper.sqrt(d3);
   /* 217 */           this.motionX += d0 / d5 * move - this.motionX;
   /* 218 */           this.motionZ += d1 / d5 * move - this.motionZ;
   /* 219 */           this.getLookHelper().setLookPositionWithEntity(entity, 180F, 40F);
   this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
            }
        }
    	if (this.getNatureBlock(this.world.getBlockState(pos.up())))
    		++this.waypointY;
    	
    	if (!this.world.isRemote && (this.getInvalidBlock(this.world.getBlockState(pos)) || this.world.getBlockState(pos.up(1)).getBlock() != Blocks.AIR || this.world.getBlockState(pos.up(2)).getBlock() != Blocks.AIR) || this.world.getBlockState(pos.up(3)).getBlock() != Blocks.AIR)
    	{
/* 408 */           int i = MathHelper.floor(this.posY);
/* 409 */           int i1 = MathHelper.floor(this.posX);
/* 410 */           int j1 = MathHelper.floor(this.posZ);
/* 413 */           for (int l1 = -8; l1 <= 8; l1++)
          {
/* 415 */             for (int i2 = -8; i2 <= 8; i2++)
            {
/* 417 */               for (int j = -8; j <= 8; j++)
              {
/* 419 */                 int j2 = i1 + l1;
/* 420 */                 int k = i + j;
/* 421 */                 int l = j1 + i2;
IBlockState blockmain = this.world.getBlockState(new BlockPos(j2, k, l));
Block block1 = this.world.getBlockState(new BlockPos(j2, k + 1, l)).getBlock();
Block block2 = this.world.getBlockState(new BlockPos(j2, k + 2, l)).getBlock();
Block block3 = this.world.getBlockState(new BlockPos(j2, k + 3, l)).getBlock();
                
                if (getNatureBlock(blockmain) && ((blockmain.getBlock() == this.getFavoriteBlockToPerch() && this.rand.nextInt(5) == 0) || this.rand.nextInt(600) == 0) && block1 == Blocks.AIR && block2 == Blocks.AIR && block3 == Blocks.AIR)
                {
                	this.waypointX = j2;
                	this.waypointY = k;
                	this.waypointZ = l;
                }
              }
            }
          }
    	}
    }

    @SuppressWarnings("deprecation")
	public boolean getNatureBlock(IBlockState state) 
    {
    	return (state.getMaterial().isOpaque() || state.getBlock() == getFavoriteBlockToPerch()) && state.getBlock().isTopSolid(state) && !(state.getBlock() instanceof IPlantable) && !(state.getBlock() instanceof IGrowable) && !BLACKLISTEDBLOCKS.contains(state.getBlock());
	}
    
    @SuppressWarnings("deprecation")
	public boolean getInvalidBlock(IBlockState state) 
    {
    	return (!state.getMaterial().isOpaque() || state.getBlock() != getFavoriteBlockToPerch() || !state.getBlock().isTopSolid(state) || state.getBlock() instanceof IPlantable || state.getBlock() instanceof IGrowable || BLACKLISTEDBLOCKS.contains(state.getBlock()));
	}
    
    public Block getFavoriteBlockToPerch() 
    {
        switch (this.getVariant())
        {
            case 1:
                return GBlocks.sandstoneperch;
            case 2:
                return GBlocks.obsidianperch;
            case 3:
                return GBlocks.goldperch;
            case 4:
                return GBlocks.ironperch;
            case 5:
                return GBlocks.endstoneperch;
            case 6:
                return GBlocks.netherbrickperch;
            default:
                return GBlocks.stoneperch;
        }
	}
    
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
 /* 434 */     if (isEntityInvulnerable(source))
      {
 /* 436 */       return false;
      }
 /* 438 */     else if (source.isExplosion() || source == DamageSource.IN_WALL || source == DamageSource.DROWN || source == DamageSource.ON_FIRE || source == DamageSource.IN_FIRE)
 {
 /* 440 */       return false;
 }
 else
 {
	 if (source.getTrueSource() instanceof EntityLivingBase && this.canAttackClass(source.getTrueSource().getClass()))
		 this.setRevengeTarget((EntityLivingBase) source.getTrueSource());
	 
 /* 444 */     return super.attackEntityFrom(source, amount);
 }
    }

	public boolean attackEntityAsMob(Entity p_70652_1_)
    {
        this.world.setEntityState(this, (byte)4);
        boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
        if (flag)
        {
            switch (this.getVariant())
            {
                case 0:
                default:
                	p_70652_1_.motionY += 0.3D;
                	break;
                case 1:
                	p_70652_1_.motionY += 0.15D;
                	break;
                case 2:
                	p_70652_1_.motionY += 0.5D;
                	break;
                case 3:
                	p_70652_1_.motionY += 0.6D;
                	break;
                case 4:
                	p_70652_1_.motionY += 0.4D;
                	break;
                case 5:
                	p_70652_1_.motionY += 0.5D;
                	break;
                case 6:
                {
                	p_70652_1_.motionY += 0.3D;
                	p_70652_1_.setFire(10);
                }
            }
        }
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

	protected SoundEvent getAmbientSound()
	{
		return this.getNatureBlock(this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - 0.5D), MathHelper.floor(this.posZ)))) ? null : GSounds.gargoyleLiving;
	}

	protected SoundEvent getHurtSound(DamageSource source)
	{
		return GSounds.gargoyleGrunt;
	}

	protected SoundEvent getDeathSound()
	{
		return GSounds.gargoyleDeath;
	}

    private void setTargetedEntity(int entityId)
    {
        this.dataManager.set(TARGET_ENTITY, Integer.valueOf(entityId));
    }

    public boolean hasTargetedEntity()
    {
        return ((Integer)this.dataManager.get(TARGET_ENTITY)).intValue() != 0;
    }
    
    @Nullable
    public EntityLivingBase getTargetedEntity()
    {
        if (!this.hasTargetedEntity())
        {
            return null;
        }
        else if (this.world.isRemote)
        {
            if (this.targetedEntity != null)
            {
                return this.targetedEntity;
            }
            else
            {
                Entity entity = this.world.getEntityByID(((Integer)this.dataManager.get(TARGET_ENTITY)).intValue());

                if (entity instanceof EntityLivingBase)
                {
                    this.targetedEntity = (EntityLivingBase)entity;
                    return this.targetedEntity;
                }
                else
                {
                    return null;
                }
            }
        }
        else
        {
            return this.getAttackTarget();
        }
    }
    
    static
    {
    	BLACKLISTEDBLOCKS.add(Blocks.AIR);
    	BLACKLISTEDBLOCKS.add(Blocks.GRASS_PATH);
    	BLACKLISTEDBLOCKS.add(Blocks.BEDROCK);
    	BLACKLISTEDBLOCKS.add(Blocks.BARRIER);
    	BLACKLISTEDBLOCKS.add(Blocks.COMMAND_BLOCK);
    	BLACKLISTEDBLOCKS.add(Blocks.CHAIN_COMMAND_BLOCK);
    	BLACKLISTEDBLOCKS.add(Blocks.REPEATING_COMMAND_BLOCK);
    	BLACKLISTEDBLOCKS.add(Blocks.STRUCTURE_BLOCK);
    	BLACKLISTEDBLOCKS.add(Blocks.GRASS);
    	BLACKLISTEDBLOCKS.add(Blocks.DIRT);
    	BLACKLISTEDBLOCKS.add(Blocks.MYCELIUM);
    	BLACKLISTEDBLOCKS.add(Blocks.STONE);
    	BLACKLISTEDBLOCKS.add(Blocks.END_STONE);
    	BLACKLISTEDBLOCKS.add(Blocks.SANDSTONE);
    	BLACKLISTEDBLOCKS.add(Blocks.SAND);
    	BLACKLISTEDBLOCKS.add(Blocks.GRAVEL);
        BLACKLISTEDBLOCKS.add(Blocks.TNT);
        BLACKLISTEDBLOCKS.add(Blocks.CACTUS);
        BLACKLISTEDBLOCKS.add(Blocks.CLAY);
        BLACKLISTEDBLOCKS.add(Blocks.PUMPKIN);
        BLACKLISTEDBLOCKS.add(Blocks.MELON_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.NETHERRACK);
        BLACKLISTEDBLOCKS.add(Blocks.SOUL_SAND);
        BLACKLISTEDBLOCKS.add(Blocks.LOG);
        BLACKLISTEDBLOCKS.add(Blocks.LOG2);
        BLACKLISTEDBLOCKS.add(Blocks.LEAVES);
        BLACKLISTEDBLOCKS.add(Blocks.LEAVES2);
        BLACKLISTEDBLOCKS.add(Blocks.COAL_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.COAL_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.IRON_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.IRON_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.LAPIS_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.LAPIS_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.REDSTONE_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.LIT_REDSTONE_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.REDSTONE_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.GOLD_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.GOLD_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.DIAMOND_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.DIAMOND_BLOCK);
        BLACKLISTEDBLOCKS.add(Blocks.EMERALD_ORE);
        BLACKLISTEDBLOCKS.add(Blocks.EMERALD_BLOCK);
    }
    
    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        int j = 1 + this.rand.nextInt(2);
        int k;

        switch (this.getVariant())
        {
            case 0:
            default:
                for (k = 0; k < j; ++k)
                {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.STONE), 1, 0.0F);
                }
            	break;
            case 1:
                for (k = 0; k < j; ++k)
                {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.SANDSTONE), 1, 0.0F);
                }
            	break;
            case 2:
                for (k = 0; k < j; ++k)
                {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.OBSIDIAN), 1, 0.0F);
                }
            	break;
            case 3:
                for (k = 0; k < j; ++k)
                {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.GOLD_BLOCK), 1, 0.0F);
                }
            	break;
            case 4:
                for (k = 0; k < j; ++k)
                {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.IRON_BLOCK), 1, 0.0F);
                }
            	break;
            case 5:
                for (k = 0; k < j; ++k)
                {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.END_STONE), 1, 0.0F);
                }
            	break;
            case 6:
                for (k = 0; k < j; ++k)
                {
                    this.dropItemWithOffset(Item.getItemFromBlock(Blocks.NETHER_BRICK), 1, 0.0F);
                }
        }
    }
    
    class AIBeamAttack extends EntityAIBase
    {
        private EntityGargoyle field_179456_a = EntityGargoyle.this;
        private int field_179455_b;

        public AIBeamAttack()
        {
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = this.field_179456_a.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && (entitylivingbase.attackable() || (entitylivingbase instanceof EntityLiving && ((EntityLiving)entitylivingbase).getCustomNameTag().isEmpty() && !((EntityLiving)entitylivingbase).isNoDespawnRequired()));
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting()
        {
            EntityLivingBase entitylivingbase = this.field_179456_a.getAttackTarget();
            return super.shouldContinueExecuting() && entitylivingbase != null && (entitylivingbase.attackable() || (entitylivingbase instanceof EntityLiving && ((EntityLiving)entitylivingbase).getCustomNameTag().isEmpty() && !((EntityLiving)entitylivingbase).isNoDespawnRequired()));
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.field_179455_b = -10;
            if (this.field_179456_a.getVariant() == 3)
            {
            this.field_179456_a.getNavigator().clearPath();
            this.field_179456_a.getLookHelper().setLookPositionWithEntity(this.field_179456_a.getAttackTarget(), 90.0F, 90.0F);
            }
        }

        /**
         * Resets the task
         */
        public void resetTask()
        {
            this.field_179456_a.setAttackTarget((EntityLivingBase)null);
            this.field_179456_a.setTargetedEntity(0);
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            EntityLivingBase entitylivingbase = this.field_179456_a.getAttackTarget();
        	
            if (this.field_179456_a.canEntityBeSeen(entitylivingbase))
            	this.field_179456_a.setTargetedEntity(this.field_179456_a.getAttackTarget().getEntityId());
            
            if (this.field_179456_a.getVariant() == 3)
            {
            this.field_179456_a.getNavigator().clearPath();
            this.field_179456_a.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0F, 180.0F);

            if (!this.field_179456_a.canEntityBeSeen(entitylivingbase))
            {
                this.field_179456_a.setAttackTarget((EntityLivingBase)null);
            }
            else
            {
                ++this.field_179455_b;
                this.field_179456_a.setTargetedEntity(this.field_179456_a.getAttackTarget().getEntityId());
                if (this.field_179455_b > 0)
                {
                	entitylivingbase.attackEntityFrom(DamageSource.MAGIC, (float)(field_179456_a.clientSideAttackTime / 80));
                	entitylivingbase.setFire(1 + field_179455_b);
                	entitylivingbase.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.5F, 1.0F + (field_179456_a.clientSideAttackTime / 80));
                }
                
                if (this.field_179455_b % 20 == 0)
                	entitylivingbase.hurtResistantTime = 0;
                
                if (this.field_179455_b >= 80)
                {
                    float f = 8.0F;

                    if (this.field_179456_a.world.getDifficulty() == EnumDifficulty.HARD)
                    {
                        f += 4.0F;
                    }
                    field_179456_a.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + field_179456_a.getRNG().nextFloat(), field_179456_a.getRNG().nextFloat() * 0.7F + 0.3F);
                    entitylivingbase.world.newExplosion(null, entitylivingbase.posX, entitylivingbase.posY + 1D, entitylivingbase.posZ, 1F, true, false);
                    entitylivingbase.world.newExplosion(field_179456_a, entitylivingbase.posX, entitylivingbase.posY + entitylivingbase.getEyeHeight(), entitylivingbase.posZ, 1F, true, false);
                    entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.field_179456_a, this.field_179456_a), f);
                    field_179456_a.attackEntityAsMob(entitylivingbase);
                    this.field_179456_a.setAttackTarget((EntityLivingBase)null);
                    field_179455_b = 0;
                    resetTask();
                }
            }

                super.updateTask();
            }
        }
    }
    
    class AIPerch extends EntityAIBase
    {
        public AIPerch()
        {
        	setMutexBits(7);
        }

        public boolean shouldExecute()
        {
        	IBlockState blockmain = EntityGargoyle.this.world.getBlockState(new BlockPos((int)EntityGargoyle.this.waypointX, (int)EntityGargoyle.this.waypointY, (int)EntityGargoyle.this.waypointZ));
        	return EntityGargoyle.this.getNatureBlock(blockmain) && EntityGargoyle.this.getAttackTarget() == null;
        }
    }

	public EnumGender getGender() 
	{
		return EnumGender.NONE;
	}

	public void setVariant(int type) 
	{
    	this.dataManager.set(TYPE, Integer.valueOf(type));
	}

	public int getVariant() 
	{
    	return ((Integer)this.dataManager.get(TYPE)).intValue();
	}

	public double getMobHealth() {
        switch (getVariant() )
        {
            case 1:
            	return 30D;
            case 2:
            	return 200D;
            case 3:
            	return 80D;
            case 4:
            	return 100D;
            case 5:
            	return 120D;
            case 6:
            	return 60D;
            default:
            	return 50D;
        }
	}

	public double getMobAttack() 
	{
	        switch (getVariant())
	        {
	            case 1:
	            	return (this.world.getBiome(this.getPosition()) == Biomes.DESERT || this.world.getBiome(this.getPosition()) == Biomes.DESERT_HILLS ? 12D : 6D);
	            case 2:
	            	return 18D;
	            case 3:
	            	return 20D;
	            case 4:
	            	return 14D;
	            case 5:
	            	return 26D;
	            case 6:
	            	return 14D;
	            default:
	            	return 10D;
	        }
	}

	public double getMobSpeed() 
	{
		return 0.25F;
	}

	public EnumLevel getTier() 
	{
		return EnumLevel.NORMAL;
	}

	public EnumSoundType getSoundType() 
	{
		return EnumSoundType.WOOD;
	}
}