package trazormc.elementalswords;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import trazormc.elementalswords.gui.ImbuementTableGui;
import trazormc.elementalswords.holders.ModBlocks;
import trazormc.elementalswords.holders.ModContainerTypes;
import trazormc.elementalswords.holders.ModItems;
import trazormc.elementalswords.proxy.ClientProxy;
import trazormc.elementalswords.proxy.IProxy;
import trazormc.elementalswords.proxy.ServerProxy;
import trazormc.elementalswords.util.handlers.EventHandler;
import trazormc.elementalswords.util.handlers.RenderHandler;

@Mod(ElementalSwords.MOD_ID)
@Mod.EventBusSubscriber(modid = ElementalSwords.MOD_ID, bus = Bus.MOD)
public class ElementalSwords {
	public static final String MOD_ID = "trazormc_elemental_swords";
	public static ElementalSwords instance;
	public static final Logger logger = LogManager.getLogger(ElementalSwords.MOD_ID);
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	public ElementalSwords() {
		instance = this;	
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {			
		for(Biome biome : ForgeRegistries.BIOMES) {
			if(biome != Biomes.THE_END && biome != Biomes.END_BARRENS && biome != Biomes.END_HIGHLANDS && biome != Biomes.END_MIDLANDS
					&& biome != Biomes.SMALL_END_ISLANDS && biome != Biomes.NETHER) {
				biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
						Feature.ORE, 
						new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.AMETHYST_ORE.getDefaultState(), 8), 
						Placement.COUNT_RANGE, 
						new CountRangeConfig(/*veins per chunk*/1, 4, 0, 24) 
				));
			}
		}
		
		logger.info("Setup complete");
	}	

	@SubscribeEvent
	public static void clientRegistries(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ModContainerTypes.IMBUEMENT_TABLE, ImbuementTableGui::new);
		RenderHandler.registerEntityRenders();
		logger.info("Client registered");
	}

	@SubscribeEvent
	public static void serverInit(FMLServerStartingEvent event) {}

	public static ItemGroup tabSwords = new ItemGroup("tab_swords") {		
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.AMETHYST_SWORD);
		}
	};
}
