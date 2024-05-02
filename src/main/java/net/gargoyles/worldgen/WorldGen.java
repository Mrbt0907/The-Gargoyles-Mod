package net.gargoyles.worldgen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		int dimensionId = world.provider.getDimension();
		
		if (dimensionId != 1 && dimensionId != -1 && random.nextInt(30) == 0) // Every Dimension that isn't the Nether or the End (Overworld if no other mods are installed)
		{
			generateStructure(chunkProvider, random, world, chunkX, chunkZ, new WorldGenCathedrial(), 256, 1);
		}
	}

	private void generateStructure(IChunkProvider chunkGenerator, Random rand, World world, int chunkX, int chunkZ, WorldGenerator worldgen, int maxY, int chance)
	{
		for (int k = 0; k < chance; ++k)
		{
	    	int x = chunkX * 16 + rand.nextInt(16);
	    	int z = chunkZ * 16 + rand.nextInt(16);
	    	int randPosY = rand.nextInt(maxY);
	    	worldgen.generate(world, rand, new BlockPos(x, randPosY, z));
		}
	}
}