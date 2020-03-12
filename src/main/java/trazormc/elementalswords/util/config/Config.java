package trazormc.elementalswords.util.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import trazormc.elementalswords.ElementalSwords;

@EventBusSubscriber
public class Config {
	
	private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ForgeConfigSpec SERVER_CONFIG;
	//public static final ForgeConfigSpec CLIENT_CONFIG;
	
	//Configurable Values
	public static ForgeConfigSpec.IntValue AMETHYST_DIMENSION_ID;	
	public static ForgeConfigSpec.IntValue AMETHYST_MINER_ID;
	public static ForgeConfigSpec.IntValue AMETHYST_KING_ID;
	public static ForgeConfigSpec.IntValue AIR_BOSS_ID;
	public static ForgeConfigSpec.IntValue EARTH_BOSS_ID ;
	public static ForgeConfigSpec.IntValue FIRE_BOSS_ID;
	public static ForgeConfigSpec.IntValue LIGHTNING_BOSS_ID;
	public static ForgeConfigSpec.IntValue WATER_BOSS_ID;
	public static ForgeConfigSpec.IntValue CHARGED_FIREBALL_ID;
	public static ForgeConfigSpec.IntValue IMBUEMENT_WINDOW_ID;

	static {
		SERVER_BUILDER.comment("General config");
		//CLIENT_BUILDER.comment("General config");
		
		IMBUEMENT_WINDOW_ID = SERVER_BUILDER.comment("Imbuement Table Window ID").defineInRange("imbuement_window_id", 0, 0, 30);	
		
		SERVER_CONFIG = SERVER_BUILDER.build();
		//CLIENT_CONFIG = CLIENT_BUILDER.build();
	}
	
	public static void loadConfig(ForgeConfigSpec spec, Path path) {
		ElementalSwords.logger.debug("Loading config file {}", path);
		
		final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
		ElementalSwords.logger.debug("Built TOML Config for {}", path.toString());
		configData.load();
		ElementalSwords.logger.debug("Loaded TOML Config for {}", path.toString());
		spec.setConfig(configData);
	}
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading event) {
		
	}
	
	@SubscribeEvent
	public static void onFileChanged(final ModConfig.ConfigReloading event) {
		
	}
}
