package trazormc.elementalswords.holders;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.entities.ChargedFireballEntity;
import trazormc.elementalswords.entities.EarthBossEntity;
import trazormc.elementalswords.entities.LightningBossEntity;
import trazormc.elementalswords.entities.WaterBossEntity;
import trazormc.elementalswords.entities.WindSeekerEntity;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.entities.HailEntity;
import trazormc.elementalswords.ElementalSwords;

@ObjectHolder(ElementalSwords.MOD_ID)
public class ModEntityTypes {	
	public static EntityType<AirBossEntity> AIR_BOSS = null;
	public static EntityType<EarthBossEntity> EARTH_BOSS = null;
	public static EntityType<FireBossEntity> FIRE_BOSS = null;
	public static EntityType<LightningBossEntity> LIGHTNING_BOSS = null;
	public static EntityType<WaterBossEntity> WATER_BOSS = null;
	
	public static EntityType<ChargedFireballEntity> CHARGED_FIREBALL = null;
	public static EntityType<HailEntity> HAIL = null;
	public static EntityType<WindSeekerEntity> WIND_SEEKER = null;
}
