package trazormc.elementalswords.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.server.ServerWorld;
import trazormc.elementalswords.ElementalSwords;

public interface IStructure {
	public static final ServerWorld WORLDSERVER = Minecraft.getInstance().getIntegratedServer().func_71218_a(DimensionType.getById(ElementalSwords.AMETHYST_DIM_TYPE.getId()));
	public static final PlacementSettings settings = (new PlacementSettings()).setChunk(null).setIgnoreEntities(false).setMirror(Mirror.NONE).setRotation(Rotation.NONE);
}
