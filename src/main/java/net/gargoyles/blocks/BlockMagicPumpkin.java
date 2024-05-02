package net.gargoyles.blocks;

import net.gargoyles.entity.EntityGargoyle;
import net.gargoyles.registry.GBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMagicPumpkin extends BlockHorizontal
{
    private BlockPattern stoneGargoyleBasePattern;
    private BlockPattern stoneGargoylePattern;
    private BlockPattern sandstoneGargoyleBasePattern;
    private BlockPattern sandstoneGargoylePattern;
    private BlockPattern obsidianGargoyleBasePattern;
    private BlockPattern obsidianGargoylePattern;
    private BlockPattern goldenGargoyleBasePattern;
    private BlockPattern goldenGargoylePattern;
    private BlockPattern ironGargoyleBasePattern;
    private BlockPattern ironGargoylePattern;
    private BlockPattern endstoneGargoyleBasePattern;
    private BlockPattern endstoneGargoylePattern;
    private BlockPattern netheraticGargoyleBasePattern;
    private BlockPattern netheraticGargoylePattern;

    public BlockMagicPumpkin()
    {
        super(Material.GOURD, MapColor.ADOBE);
        this.setUnlocalizedName("magic_pumpkin");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.WOOD);
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
        this.trySpawnGolem(worldIn, pos);
    }

    public boolean canDispenserPlace(World worldIn, BlockPos pos)
    {
        return this.getStoneGargoyleBasePattern().match(worldIn, pos) != null || this.getSandstoneGargoyleBasePattern().match(worldIn, pos) != null || this.getObsidianGargoyleBasePattern().match(worldIn, pos) != null || this.getGoldenGargoyleBasePattern().match(worldIn, pos) != null || this.getIronGargoyleBasePattern().match(worldIn, pos) != null || this.getEndstoneGargoyleBasePattern().match(worldIn, pos) != null || this.getNetheraticGargoyleBasePattern().match(worldIn, pos) != null;
    }

    private void trySpawnGolem(World worldIn, BlockPos pos)
    {
        BlockPattern.PatternHelper blockpattern$patternhelper1 = this.getStoneGargoylePattern().match(worldIn, pos);
        BlockPattern.PatternHelper blockpattern$patternhelper2 = this.getSandstoneGargoyleBasePattern().match(worldIn, pos);
        BlockPattern.PatternHelper blockpattern$patternhelper3 = this.getObsidianGargoyleBasePattern().match(worldIn, pos);
        BlockPattern.PatternHelper blockpattern$patternhelper4 = this.getGoldenGargoyleBasePattern().match(worldIn, pos);
        BlockPattern.PatternHelper blockpattern$patternhelper5 = this.getIronGargoyleBasePattern().match(worldIn, pos);
        BlockPattern.PatternHelper blockpattern$patternhelper6 = this.getEndstoneGargoyleBasePattern().match(worldIn, pos);
        BlockPattern.PatternHelper blockpattern$patternhelper7 = this.getNetheraticGargoyleBasePattern().match(worldIn, pos);

        if (blockpattern$patternhelper1 != null)
        {
            for (int j = 0; j < this.getStoneGargoylePattern().getPalmLength(); ++j)
            {
                for (int k = 0; k < this.getStoneGargoylePattern().getThumbLength(); ++k)
                {
                    worldIn.setBlockState(blockpattern$patternhelper1.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }

            EntityGargoyle entitygolem = new EntityGargoyle(worldIn);
            entitygolem.setVariant(0);
            entitygolem.setPlayerCreated(true);
            BlockPos blockpos1 = blockpattern$patternhelper1.translateOffset(1, 2, 0).getPos();
            entitygolem.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntity(entitygolem);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitygolem.getEntityBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitygolem);
            }

            for (int l = 0; l < 120; ++l)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int k1 = 0; k1 < this.getStoneGargoylePattern().getPalmLength(); ++k1)
            {
                for (int l1 = 0; l1 < this.getStoneGargoylePattern().getThumbLength(); ++l1)
                {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper1.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                }
            }
        }

        if (blockpattern$patternhelper2 != null)
        {
            for (int j = 0; j < this.getSandstoneGargoyleBasePattern().getPalmLength(); ++j)
            {
                for (int k = 0; k < this.getSandstoneGargoyleBasePattern().getThumbLength(); ++k)
                {
                    worldIn.setBlockState(blockpattern$patternhelper2.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }

            EntityGargoyle entitygolem = new EntityGargoyle(worldIn);
            entitygolem.setVariant(1);
            entitygolem.setPlayerCreated(true);
            BlockPos blockpos1 = blockpattern$patternhelper2.translateOffset(1, 2, 0).getPos();
            entitygolem.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntity(entitygolem);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitygolem.getEntityBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitygolem);
            }

            for (int l = 0; l < 120; ++l)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int k1 = 0; k1 < this.getSandstoneGargoyleBasePattern().getPalmLength(); ++k1)
            {
                for (int l1 = 0; l1 < this.getSandstoneGargoyleBasePattern().getThumbLength(); ++l1)
                {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper2.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                }
            }
        }
        
        if (blockpattern$patternhelper3 != null)
        {
            for (int j = 0; j < this.getObsidianGargoyleBasePattern().getPalmLength(); ++j)
            {
                for (int k = 0; k < this.getObsidianGargoyleBasePattern().getThumbLength(); ++k)
                {
                    worldIn.setBlockState(blockpattern$patternhelper3.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }

            EntityGargoyle entitygolem = new EntityGargoyle(worldIn);
            entitygolem.setVariant(2);
            entitygolem.setPlayerCreated(true);
            BlockPos blockpos1 = blockpattern$patternhelper3.translateOffset(1, 2, 0).getPos();
            entitygolem.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntity(entitygolem);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitygolem.getEntityBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitygolem);
            }

            for (int l = 0; l < 120; ++l)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int k1 = 0; k1 < this.getObsidianGargoyleBasePattern().getPalmLength(); ++k1)
            {
                for (int l1 = 0; l1 < this.getObsidianGargoyleBasePattern().getThumbLength(); ++l1)
                {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper3.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                }
            }
        }
        
        if (blockpattern$patternhelper4 != null)
        {
            for (int j = 0; j < this.getGoldenGargoyleBasePattern().getPalmLength(); ++j)
            {
                for (int k = 0; k < this.getGoldenGargoyleBasePattern().getThumbLength(); ++k)
                {
                    worldIn.setBlockState(blockpattern$patternhelper4.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }

            EntityGargoyle entitygolem = new EntityGargoyle(worldIn);
            entitygolem.setVariant(3);
            entitygolem.setPlayerCreated(true);
            BlockPos blockpos1 = blockpattern$patternhelper4.translateOffset(1, 2, 0).getPos();
            entitygolem.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntity(entitygolem);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitygolem.getEntityBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitygolem);
            }

            for (int l = 0; l < 120; ++l)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int k1 = 0; k1 < this.getGoldenGargoyleBasePattern().getPalmLength(); ++k1)
            {
                for (int l1 = 0; l1 < this.getGoldenGargoyleBasePattern().getThumbLength(); ++l1)
                {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper4.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                }
            }
        }
        
        if (blockpattern$patternhelper5 != null)
        {
            for (int j = 0; j < this.getIronGargoyleBasePattern().getPalmLength(); ++j)
            {
                for (int k = 0; k < this.getIronGargoyleBasePattern().getThumbLength(); ++k)
                {
                    worldIn.setBlockState(blockpattern$patternhelper5.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }

            EntityGargoyle entitygolem = new EntityGargoyle(worldIn);
            entitygolem.setVariant(4);
            entitygolem.setPlayerCreated(true);
            BlockPos blockpos1 = blockpattern$patternhelper5.translateOffset(1, 2, 0).getPos();
            entitygolem.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntity(entitygolem);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitygolem.getEntityBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitygolem);
            }

            for (int l = 0; l < 120; ++l)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int k1 = 0; k1 < this.getIronGargoyleBasePattern().getPalmLength(); ++k1)
            {
                for (int l1 = 0; l1 < this.getIronGargoyleBasePattern().getThumbLength(); ++l1)
                {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper5.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                }
            }
        }
        
        if (blockpattern$patternhelper6 != null)
        {
            for (int j = 0; j < this.getEndstoneGargoyleBasePattern().getPalmLength(); ++j)
            {
                for (int k = 0; k < this.getEndstoneGargoyleBasePattern().getThumbLength(); ++k)
                {
                    worldIn.setBlockState(blockpattern$patternhelper6.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }

            EntityGargoyle entitygolem = new EntityGargoyle(worldIn);
            entitygolem.setVariant(5);
            entitygolem.setPlayerCreated(true);
            BlockPos blockpos1 = blockpattern$patternhelper6.translateOffset(1, 2, 0).getPos();
            entitygolem.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntity(entitygolem);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitygolem.getEntityBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitygolem);
            }

            for (int l = 0; l < 120; ++l)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int k1 = 0; k1 < this.getEndstoneGargoyleBasePattern().getPalmLength(); ++k1)
            {
                for (int l1 = 0; l1 < this.getEndstoneGargoyleBasePattern().getThumbLength(); ++l1)
                {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper6.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                }
            }
        }
        
        if (blockpattern$patternhelper7 != null)
        {
            for (int j = 0; j < this.getNetheraticGargoyleBasePattern().getPalmLength(); ++j)
            {
                for (int k = 0; k < this.getNetheraticGargoyleBasePattern().getThumbLength(); ++k)
                {
                    worldIn.setBlockState(blockpattern$patternhelper7.translateOffset(j, k, 0).getPos(), Blocks.AIR.getDefaultState(), 2);
                }
            }

            EntityGargoyle entitygolem = new EntityGargoyle(worldIn);
            entitygolem.setVariant(6);
            entitygolem.setPlayerCreated(true);
            BlockPos blockpos1 = blockpattern$patternhelper7.translateOffset(1, 2, 0).getPos();
            entitygolem.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntity(entitygolem);

            for (EntityPlayerMP entityplayermp : worldIn.getEntitiesWithinAABB(EntityPlayerMP.class, entitygolem.getEntityBoundingBox().grow(5.0D)))
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayermp, entitygolem);
            }

            for (int l = 0; l < 120; ++l)
            {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)blockpos1.getX() + worldIn.rand.nextDouble(), (double)blockpos1.getY() + worldIn.rand.nextDouble() * 2.5D, (double)blockpos1.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            for (int k1 = 0; k1 < this.getNetheraticGargoyleBasePattern().getPalmLength(); ++k1)
            {
                for (int l1 = 0; l1 < this.getNetheraticGargoyleBasePattern().getThumbLength(); ++l1)
                {
                    BlockWorldState blockworldstate1 = blockpattern$patternhelper7.translateOffset(k1, l1, 0);
                    worldIn.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.AIR, false);
                }
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    protected BlockPattern getStoneGargoyleBasePattern()
    {
        if (this.stoneGargoyleBasePattern == null)
        {
            this.stoneGargoyleBasePattern = FactoryBlockPattern.start().aisle("   ", "SSS", " S ").where('S', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.STONE))).build();
        }

        return this.stoneGargoyleBasePattern;
    }

    protected BlockPattern getStoneGargoylePattern()
    {
        if (this.stoneGargoylePattern == null)
        {
            this.stoneGargoylePattern = FactoryBlockPattern.start().aisle(" ^ ", "SSS", " S ").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(GBlocks.magic_pumpkin))).where('S', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.STONE))).build();
        }

        return this.stoneGargoylePattern;
    }
    
    protected BlockPattern getSandstoneGargoyleBasePattern()
    {
        if (this.sandstoneGargoyleBasePattern == null)
        {
            this.sandstoneGargoyleBasePattern = FactoryBlockPattern.start().aisle("   ", "SSS", " S ").where('S', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SANDSTONE))).build();
        }

        return this.sandstoneGargoyleBasePattern;
    }

    protected BlockPattern getSandstoneGargoylePattern()
    {
        if (this.sandstoneGargoylePattern == null)
        {
            this.sandstoneGargoylePattern = FactoryBlockPattern.start().aisle(" ^ ", "SSS", " S ").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(GBlocks.magic_pumpkin))).where('S', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.SANDSTONE))).build();
        }

        return this.sandstoneGargoylePattern;
    }
    
    protected BlockPattern getObsidianGargoyleBasePattern()
    {
        if (this.obsidianGargoyleBasePattern == null)
        {
            this.obsidianGargoyleBasePattern = FactoryBlockPattern.start().aisle("   ", "OOO", " O ").where('O', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.OBSIDIAN))).build();
        }

        return this.obsidianGargoyleBasePattern;
    }

    protected BlockPattern getObsidianGargoylePattern()
    {
        if (this.obsidianGargoylePattern == null)
        {
            this.obsidianGargoylePattern = FactoryBlockPattern.start().aisle(" ^ ", "OOO", " O ").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(GBlocks.magic_pumpkin))).where('O', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.OBSIDIAN))).build();
        }

        return this.obsidianGargoylePattern;
    }
    
    protected BlockPattern getGoldenGargoyleBasePattern()
    {
        if (this.goldenGargoyleBasePattern == null)
        {
            this.goldenGargoyleBasePattern = FactoryBlockPattern.start().aisle("   ", "GGG", " G ").where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK))).build();
        }

        return this.goldenGargoyleBasePattern;
    }

    protected BlockPattern getGoldenGargoylePattern()
    {
        if (this.goldenGargoylePattern == null)
        {
            this.goldenGargoylePattern = FactoryBlockPattern.start().aisle(" ^ ", "GGG", " G ").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(GBlocks.magic_pumpkin))).where('G', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.GOLD_BLOCK))).build();
        }

        return this.goldenGargoylePattern;
    }
    
    protected BlockPattern getIronGargoyleBasePattern()
    {
        if (this.ironGargoyleBasePattern == null)
        {
            this.ironGargoyleBasePattern = FactoryBlockPattern.start().aisle("   ", "III", " I ").where('I', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).build();
        }

        return this.ironGargoyleBasePattern;
    }

    protected BlockPattern getIronGargoylePattern()
    {
        if (this.ironGargoylePattern == null)
        {
            this.ironGargoylePattern = FactoryBlockPattern.start().aisle(" ^ ", "III", " I ").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(GBlocks.magic_pumpkin))).where('I', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.IRON_BLOCK))).build();
        }

        return this.ironGargoylePattern;
    }
    
    protected BlockPattern getEndstoneGargoyleBasePattern()
    {
        if (this.endstoneGargoyleBasePattern == null)
        {
            this.endstoneGargoyleBasePattern = FactoryBlockPattern.start().aisle("   ", "EEE", " E ").where('E', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_STONE))).build();
        }

        return this.endstoneGargoyleBasePattern;
    }

    protected BlockPattern getEndstoneGargoylePattern()
    {
        if (this.endstoneGargoylePattern == null)
        {
            this.endstoneGargoylePattern = FactoryBlockPattern.start().aisle(" ^ ", "EEE", " E ").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(GBlocks.magic_pumpkin))).where('E', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.END_STONE))).build();
        }

        return this.endstoneGargoylePattern;
    }
    
    protected BlockPattern getNetheraticGargoyleBasePattern()
    {
        if (this.netheraticGargoyleBasePattern == null)
        {
            this.netheraticGargoyleBasePattern = FactoryBlockPattern.start().aisle("   ", "NNN", " N ").where('N', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.NETHER_BRICK))).build();
        }

        return this.netheraticGargoyleBasePattern;
    }

    protected BlockPattern getNetheraticGargoylePattern()
    {
        if (this.netheraticGargoylePattern == null)
        {
            this.netheraticGargoylePattern = FactoryBlockPattern.start().aisle(" ^ ", "NNN", " N ").where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(GBlocks.magic_pumpkin))).where('N', BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.NETHER_BRICK))).build();
        }

        return this.netheraticGargoylePattern;
    }
}