package trazormc.elementalswords.init;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;
import trazormc.elementalswords.entities.AmethystMinerEntity;
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.entities.AmethystKingEntity;
import trazormc.elementalswords.entities.ChargedFireballEntity;
import trazormc.elementalswords.entities.EarthBossEntity;
import trazormc.elementalswords.entities.LightningBossEntity;
import trazormc.elementalswords.entities.WaterBossEntity;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.ElementalSwords;

@ObjectHolder(ElementalSwords.MOD_ID)
public class ModEntityTypes {	
	public static EntityType<AmethystKingEntity> ENTITY_AMETHYST_KING = null;
	public static EntityType<AmethystMinerEntity> ENTITY_AMETHYST_MINER = null;
	public static EntityType<AirBossEntity> ENTITY_AIR_BOSS = null;
	public static EntityType<EarthBossEntity> ENTITY_EARTH_BOSS = null;
	public static EntityType<FireBossEntity> ENTITY_FIRE_BOSS = null;
	public static EntityType<LightningBossEntity> ENTITY_LIGHTNING_BOSS = null;
	public static EntityType<WaterBossEntity> ENTITY_WATER_BOSS = null;
	public static EntityType<ChargedFireballEntity> ENTITY_CHARGED_FIREBALL = null;	
}
