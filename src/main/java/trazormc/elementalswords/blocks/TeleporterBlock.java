package trazormc.elementalswords.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.network.play.server.SPlayerAbilitiesPacket;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.network.play.server.SServerDifficultyPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.util.ModUtils;

public class TeleporterBlock extends Block {

	public TeleporterBlock(Properties properties) {
		super(properties);	
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
            if(worldIn.getDimension().getType() == ElementalSwords.AMETHYST_DIM_TYPE) {
            	if(player.getBedLocation(DimensionType.OVERWORLD) != null)
            		changeDimension(((ServerPlayerEntity) player), player.getBedLocation(DimensionType.OVERWORLD), DimensionType.OVERWORLD);
            	else 
            		changeDimension(((ServerPlayerEntity) player), new BlockPos(0, ModUtils.calculateGenerationHeight(worldIn, 0, 0) + 1, 0), DimensionType.OVERWORLD);
            } else {
            	changeDimension(((ServerPlayerEntity) player), new BlockPos(0, ModUtils.calculateGenerationHeight(worldIn, 0, 0) + 1, 0), ElementalSwords.AMETHYST_DIM_TYPE);
            }
			return true;
		}
		return false;
	}
	
	//Move to ModUtils? Maybe needed elsewhere?
	public static void changeDimension(ServerPlayerEntity player, BlockPos pos, DimensionType type) {		
		if(!ForgeHooks.onTravelToDimension(player, type)) return;
		
		DimensionType dimensionType = player.dimension;

        ServerWorld serverworld = player.server.func_71218_a(dimensionType);
        player.dimension = type;
        ServerWorld serverworld1 = player.server.func_71218_a(type);
        WorldInfo worldinfo = player.world.getWorldInfo();
        player.connection.sendPacket(new SRespawnPacket(type, worldinfo.getGenerator(), player.interactionManager.getGameType()));
        player.connection.sendPacket(new SServerDifficultyPacket(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        PlayerList playerlist = player.server.getPlayerList();
        playerlist.updatePermissionLevel(player);
        serverworld.removeEntity(player, true);
        player.revive();
        float f = player.rotationPitch;
        float f1 = player.rotationYaw;

        player.setLocationAndAngles(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, f1, f);
        serverworld.getProfiler().endSection();
        serverworld.getProfiler().startSection("placing");
        player.setLocationAndAngles(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, f1, f);

        serverworld.getProfiler().endSection();
        player.setWorld(serverworld1);
        serverworld1.func_217447_b(player);
        player.connection.setPlayerLocation(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, f1, f);
        player.interactionManager.func_73080_a(serverworld1);
        player.connection.sendPacket(new SPlayerAbilitiesPacket(player.abilities));
        playerlist.func_72354_b(player, serverworld1);
        playerlist.sendInventory(player);
        
        for(EffectInstance effectinstance : player.getActivePotionEffects()) {
            player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectinstance));
        }

        player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
        BasicEventHooks.firePlayerChangedDimensionEvent(player, dimensionType, type);		
	}
}
