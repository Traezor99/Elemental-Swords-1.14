package trazormc.elementalswords;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import trazormc.elementalswords.gui.ImbuementTableGui;
import trazormc.elementalswords.init.ModContainerTypes;
import trazormc.elementalswords.init.ModDimensions;
import trazormc.elementalswords.init.ModItems;
import trazormc.elementalswords.proxy.ClientProxy;
import trazormc.elementalswords.proxy.IProxy;
import trazormc.elementalswords.proxy.ServerProxy;
import trazormc.elementalswords.util.config.Config;
import trazormc.elementalswords.util.handlers.EventHandler;
import trazormc.elementalswords.util.handlers.RenderHandler;

@Mod(ElementalSwords.MOD_ID)
@Mod.EventBusSubscriber(modid = ElementalSwords.MOD_ID, bus = Bus.MOD)
public class ElementalSwords {
	public static final String MOD_ID = "trazormc_elemental_swords";
	public static ElementalSwords instance;
	public static final Logger logger = LogManager.getLogger(ElementalSwords.MOD_ID);
	
	public static DimensionType AMETHYST_DIM_TYPE;
	public static DimensionType FIRE_DIM_TYPE;
	
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
	
	public ElementalSwords() {
		instance = this;
		
		//ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
		
		MinecraftForge.EVENT_BUS.register(new EventHandler());
						
		//Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Reference.MOD_ID + "-client.toml"));
		Config.loadConfig(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve(ElementalSwords.MOD_ID + "-server.toml"));
	}
	
	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {	
		AMETHYST_DIM_TYPE = DimensionManager.registerDimension(new ResourceLocation(ElementalSwords.MOD_ID, "amethyst_dimension"), ModDimensions.AMETHYST_DIMENSION, new PacketBuffer(Unpooled.buffer(16)), true);
		FIRE_DIM_TYPE = DimensionManager.registerDimension(new ResourceLocation(ElementalSwords.MOD_ID, "fire_dimension"), ModDimensions.FIRE_DIMENSION, new PacketBuffer(Unpooled.buffer(16)), true);
		logger.info("Setup complete");
	}	
	
	@SubscribeEvent
	public static void clientRegistries(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ModContainerTypes.IMBUEMENT_TABLE, ImbuementTableGui::new);
		RenderHandler.registerEntityRenders();
		logger.info("Client registered");
	}
	
	@SubscribeEvent
	public static void serverInit(FMLServerStartingEvent event) {
	}
	
	public static ItemGroup tabSwords = new ItemGroup("tab_swords") {		
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.AMETHYST_SWORD);
		}
	};
}
