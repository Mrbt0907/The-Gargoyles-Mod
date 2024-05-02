package net.gargoyles.worldgen;

import java.util.Random;

import net.gargoyles.registry.GBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class SpawnPerch extends WorldGenerator
{
	@SuppressWarnings("deprecation")
	public boolean LocationIsValidSpawn(World world, int x, int y, int z)
	{

		IBlockState checkBlock = world.getBlockState(new BlockPos(x, y, z));
		IBlockState blockAbove = world.getBlockState(new BlockPos(x, y + 1, z));
		IBlockState blockBelow = world.getBlockState(new BlockPos(x, y - 1, z));

		if (blockAbove.getBlock() != Blocks.AIR)
		{
			return false;
		}
		if (checkBlock.getBlock() == Blocks.COBBLESTONE && world.canSeeSky(new BlockPos(x, y + 1, z)))
		{
			return true;
		}
		else if (checkBlock.getBlock() == Blocks.SNOW_LAYER && blockBelow.getBlock() == Blocks.COBBLESTONE)
		{
			return true;
		}
		else if (checkBlock.getBlock().getMaterial(checkBlock) == Material.PLANTS && blockBelow.getBlock() == Blocks.COBBLESTONE)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean generate(World world, Random rand, BlockPos pos)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		if (LocationIsValidSpawn(world, x, y, z) && rand.nextInt(5) == 0)
		{
			world.setBlockState(pos.up(), GBlocks.stoneperch.getDefaultState());
		       return true;
		}
		else
		{
		       return false;
		}
	}
	
}
