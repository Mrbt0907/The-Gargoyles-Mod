package net.endermanofdoom.necromancer;

import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator  
{
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
    {
		if (world.getBiome(new BlockPos(chunkX, 0, chunkZ)) != Biomes.VOID)
		{
	        if (world.provider.getDimension() == 0)
	        {
	            generateStructure(chunkProvider, random, world, chunkX * 16, chunkZ * 16, new WorldGenNecromancerTower(), 256);
	        }
		}
    }

	private void generateStructure(IChunkProvider chunkGenerator, Random rand, World world, int chunkX, int chunkZ, WorldGenerator worldgen, int maxY)
	{
		if (rand.nextFloat() <= 0.00025F)
		{
	    	int x = chunkX + 8;
	    	int z = chunkZ + 8;
	    	worldgen.generate(world, rand, new BlockPos(x, maxY, z));
		}
	}
}
