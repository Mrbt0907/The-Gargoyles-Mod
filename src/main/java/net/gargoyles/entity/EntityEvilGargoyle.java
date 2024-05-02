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
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEvilGargoyle extends EntityMob implements IMobTier, IVariedMob, IGendered, ISoundSupport
{
    private static final Set<Block> BLACKLISTEDBLOCKS = Sets.<Block>newIdentityHashSet();
	   public double waypointX;
	   public double waypointY;
	   public double waypointZ;
	    private int attackTimer;
	    private static final Predicate<EntityLivingBase> ATTACKABLE = new Predicate<EntityLivingBase>()
	    {
	        public boolean apply(@Nullable EntityLivingBase p_apply_1_)
	        {
	            return p_apply_1_ != null && !(p_apply_1_ instanceof IMob) && ((EntityLivingBase)p_apply_1_).attackable();
	        }
	    };
	    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public EntityEvilGargoyle(World p_i1694_1_)
    {
        super(p_i1694_1_);
        this.enablePersistence();
        this.setSize(0.9F, 2.4F);
        this.experienceValue = 10;
        this.tasks.addTask(0, new EntityEvilGargoyle.AIPerch());
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, false, ATTACKABLE));
    }
    
    public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
    {
    	if (entitylivingbaseIn == null)
    		super.setAttackTarget(null);
        else
        	super.setAttackTarget(entitylivingbaseIn);
    }
    
    protected ResourceLocation getLootTable()
    {
        return null;
    }
    
    public float getEyeHeight()
    {
        return !this.onGround ? 1.4F : ((this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - 0.5D), MathHelper.floor(this.posZ))).getBlock() == this.getFavoriteBlockToPerch()) ? 0.875F + (this.rotationPitch / 40) : 2.1F);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }
    
    public boolean canAttackClass(@SuppressWarnings("rawtypes") Class  cls)
    {
        return cls != EntityEvilGargoyle.class;
    }

    protected void collideWithEntity(Entity p_82167_1_)
    {
        if (p_82167_1_ instanceof EntityEvilGargoyle && this.getAttackTarget() == null && ((EntityEvilGargoyle)p_82167_1_).getAttackTarget() == null)
        {
        	if (this.onGround && ((EntityEvilGargoyle)p_82167_1_).onGround && this.getDistanceSq(((EntityEvilGargoyle)p_82167_1_).waypointX, ((EntityEvilGargoyle)p_82167_1_).waypointY, ((EntityEvilGargoyle)p_82167_1_).waypointZ) < 4D)
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
        
        if (this.ticksExisted < 20)
        	this.getNavigator().clearPath();
        
        if (this.attackTimer > 0)
        {
            --this.attackTimer;
        }
        
        if (this.collidedHorizontally)
        	this.motionY = 0.4D;
        
        BlockPos pos = new BlockPos((int)waypointX, (int)waypointY, (int)waypointZ);
        Block block = this.world.getBlockState(pos).getBlock();
        
        this.world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
        
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
        
        if (getMaxHealth() != this.getMobHealth() && this.getHealth() > 0)
		{
        	this.setHealth((float) getMobHealth());
        	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMobHealth());
		}

        if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() != this.getMobAttack())
        	this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(this.getMobAttack());
        
        Entity entity = this.getAttackTarget();
        double move = entity != null ? 0.4D + (this.rand.nextDouble() * 0.2D) : 0.6D;
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
   
   /* 214 */         if ((d3 > 1D) && (this.canEntityBeSeen(entity) || this.isEntityInsideOpaqueBlock() || this.posY <= 0D || this.posY <= entity.posY || this.rand.nextInt(10) == 0))
            {
	   /* 200 */         if (this.posY <= entity.posY + 0.5D)
/* 207 */           this.motionY += 0.6D - this.motionY;
   /* 216 */           double d5 = MathHelper.sqrt(d3);
   /* 217 */           this.motionX += d0 / d5 * move - this.motionX * move;
   /* 218 */           this.motionZ += d1 / d5 * move - this.motionZ * move;
   /* 219 */           this.getLookHelper().setLookPositionWithEntity(entity, 180F, 40F);
   this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
            }
        }
        
        if (!this.world.isRemote && this.getAttackTarget() == null)
        {
        	List<EntityLiving> list = this.world.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), Predicates.and(ATTACKABLE, EntitySelectors.NOT_SPECTATING));

            if (list != null && !list.isEmpty() && this.rand.nextInt(10) == 0)
            {
                for (int i = 0; i < list.size(); ++i)
                {
                	EntityLiving entity1 = (EntityLiving)list.get(this.rand.nextInt(list.size()));

                    if (this.isEntityAlive() && entity1.isEntityAlive() && this.canAttackClass(entity1.getClass()) && !(entity1 instanceof IMob))
                        this.setAttackTarget((EntityLivingBase)entity1);
                }
            }
        }
        
    	if (this.getNatureBlock(this.world.getBlockState(pos.up())))
    		++this.waypointY;
    	
    	if (this.ticksExisted > 100 && !this.world.isRemote && (this.getInvalidBlock(this.world.getBlockState(pos)) || this.world.getBlockState(pos.up(1)).getBlock() != Blocks.AIR || this.world.getBlockState(pos.up(2)).getBlock() != Blocks.AIR) || this.world.getBlockState(pos.up(3)).getBlock() != Blocks.AIR)
    	{
/* 408 */           int i = MathHelper.floor(this.posY);
/* 409 */           int i1 = MathHelper.floor(this.posX);
/* 410 */           int j1 = MathHelper.floor(this.posZ);
/* 413 */           for (int l1 = -2; l1 <= 2; l1++)
          {
/* 415 */             for (int i2 = -2; i2 <= 2; i2++)
            {
/* 417 */               for (int j = -2; j <= 2; j++)
              {
/* 419 */                 int j2 = i1 + l1;
/* 420 */                 int k = i + j;
/* 421 */                 int l = j1 + i2;
IBlockState blockmain = this.world.getBlockState(new BlockPos(j2, k, l));
Block block1 = this.world.getBlockState(new BlockPos(j2, k + 1, l)).getBlock();
Block block2 = this.world.getBlockState(new BlockPos(j2, k + 2, l)).getBlock();
Block block3 = this.world.getBlockState(new BlockPos(j2, k + 3, l)).getBlock();
                
                if (getNatureBlock(blockmain) && (blockmain.getBlock() == this.getFavoriteBlockToPerch() && this.rand.nextInt(5) == 0) && block1 == Blocks.AIR && block2 == Blocks.AIR && block3 == Blocks.AIR)
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
    	return GBlocks.stoneperch;
	}
    
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
 /* 434 */     if (isEntityInvulnerable(source))
      {
 /* 436 */       return false;
      }
 /* 438 */     else if (source == DamageSource.IN_WALL || source == DamageSource.DROWN || source == DamageSource.ON_FIRE || source == DamageSource.IN_FIRE)
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
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 6F + (rand.nextFloat() * 6F));
        if (flag)
        {
        	p_70652_1_.motionY += 0.3D;
        }
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }
	
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 4)
        {
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getAttackTimer()
    {
        return this.attackTimer;
    }
    
    /*      */   protected SoundEvent getAmbientSound()
    /*      */   {
    /*  892 */     return this.getNatureBlock(this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY - 0.5D), MathHelper.floor(this.posZ)))) ? null : GSounds.gargoyleLiving;
    /*      */   }
    /*      */   
    /*      */ 
    /*      */ 
    /*      */ 
    /*      */   protected SoundEvent getHurtSound(DamageSource source)
    /*      */   {
    /*  900 */     return GSounds.gargoyleGrunt;
    /*      */   }
    /*      */   
    /*      */ 
    /*      */ 
    /*      */ 
    /*      */   protected SoundEvent getDeathSound()
    /*      */   {
    /*  908 */     return GSounds.gargoyleDeath;
    /*      */   }
    
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
    }
    
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }
    
    public void fall(float distance, float damageMultiplier)
    {
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 120;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return false;
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
        int j = 2 + this.rand.nextInt(3);
        int k;

        for (k = 0; k < j; ++k)
        {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.STONE), 1, 0.0F);
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
        	IBlockState blockmain = EntityEvilGargoyle.this.world.getBlockState(new BlockPos((int)EntityEvilGargoyle.this.waypointX, (int)EntityEvilGargoyle.this.waypointY, (int)EntityEvilGargoyle.this.waypointZ));
        	return EntityEvilGargoyle.this.getNatureBlock(blockmain) && EntityEvilGargoyle.this.getAttackTarget() == null;
        }
    }

	public EnumGender getGender() 
	{
		return EnumGender.NONE;
	}

	public void setVariant(int type) 
	{

	}

	public int getVariant() 
	{
    	return 0;
	}

	public double getMobHealth() 
	{
    	return 50D;
	}

	public double getMobAttack() 
	{
    	return 10D;
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