package trazormc.elementalswords.world.gen.generators;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import trazormc.elementalswords.ElementalSwords;

//Create IFeatureConfig for Structure<>
public class WorldGenStructure extends Feature<NoFeatureConfig> {

	public WorldGenStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		ServerWorld worldServer = worldIn.getWorld().getServer().func_71218_a(ElementalSwords.AMETHYST_DIM_TYPE);
        Template structureTemplate = worldServer.getStructureTemplateManager().getTemplateDefaulted(new ResourceLocation(ElementalSwords.MOD_ID,"amethyst_city_house"));
                
        structureTemplate.addBlocksToWorld(worldIn, new BlockPos(pos.getX(), calculateGenerationHeight(worldIn.getWorld(), pos.getX(), pos.getZ()), pos.getZ()), new PlacementSettings().setRotation(Rotation.NONE).setMirror(Mirror.NONE));
		return true;            
	}
	
	private static int calculateGenerationHeight(World world, double x, double z) {
		int y = world.getHeight();
		boolean foundGround = false;
		
		while(!foundGround && y-- > 0) {
			Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			foundGround = block != Blocks.AIR;
		}
		
		return y;
	}
}


