package trazormc.elementalswords.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.blocks.AmethystOreBlock;
import trazormc.elementalswords.blocks.ImbuementTableBlock;
import trazormc.elementalswords.blocks.TeleporterBlock;
import trazormc.elementalswords.containers.ImbuementTableContainer;
import trazormc.elementalswords.crafting.ImbuementShapelessRecipes;
import trazormc.elementalswords.entities.AmethystMinerEntity;
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.entities.ChargedFireballEntity;
import trazormc.elementalswords.entities.EarthBossEntity;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.entities.LightningBossEntity;
import trazormc.elementalswords.entities.WaterBossEntity;
import trazormc.elementalswords.init.ModBiomes;
import trazormc.elementalswords.init.ModBlocks;
import trazormc.elementalswords.init.ModContainerTypes;
import trazormc.elementalswords.init.ModRecipeSerializers;
import trazormc.elementalswords.init.ModDimensions;
import trazormc.elementalswords.init.ModEntityTypes;
import trazormc.elementalswords.init.ModItems;
import trazormc.elementalswords.items.armor.EarthArmor;
import trazormc.elementalswords.items.armor.FireArmor;
import trazormc.elementalswords.items.armor.WaterArmor;
import trazormc.elementalswords.items.shards.AirShardItem;
import trazormc.elementalswords.items.shards.EarthShardItem;
import trazormc.elementalswords.items.shards.FireShardItem;
import trazormc.elementalswords.items.shards.LightningShardItem;
import trazormc.elementalswords.items.shards.WaterShardItem;
import trazormc.elementalswords.items.swords.AirSword;
import trazormc.elementalswords.items.swords.EarthSword;
import trazormc.elementalswords.items.swords.FireSword;
import trazormc.elementalswords.items.swords.LightningSword;
import trazormc.elementalswords.items.swords.WaterSword;
import trazormc.elementalswords.util.ArmorMaterialTypes;
import trazormc.elementalswords.util.ItemTiers;
import trazormc.elementalswords.world.biomes.DimensionBiome;
import trazormc.elementalswords.world.dimensions.amethyst.AmethystModDimension;
import trazormc.elementalswords.world.dimensions.fire.FireModDimension;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
	
	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
		createEntityTypes();		
		event.getRegistry().registerAll(
				ModItems.AMETHYST = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "amethyst"),
				ModItems.SUMMONING_SHARD = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "summoning_shard"),
				
				ModItems.AIR_SHARD = setup(new AirShardItem(new Item.Properties().group(ElementalSwords.tabSwords)), "air_shard"),
				ModItems.EARTH_SHARD = setup(new EarthShardItem(new Item.Properties().group(ElementalSwords.tabSwords)), "earth_shard"),
				ModItems.FIRE_SHARD = setup(new FireShardItem(new Item.Properties().group(ElementalSwords.tabSwords)), "fire_shard"),
				ModItems.LIGHTNING_SHARD = setup(new LightningShardItem(new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_shard"),
				ModItems.WATER_SHARD = setup(new WaterShardItem(new Item.Properties().group(ElementalSwords.tabSwords)), "water_shard"),
				
				ModItems.GUST_OF_WIND = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "gust_of_wind"),
				ModItems.EARTH_STONE = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "earth_stone"),
				ModItems.FLAMING_EMBER = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "flaming_ember"),
				ModItems.SPARK_OF_ENERGY = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "spark_of_energy"),
				ModItems.SHIMMERING_WATER = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "shimmering_water"),
				
				ModItems.AMETHYST_SWORD = setup(new SwordItem(ItemTiers.AMETHYST, (int)ItemTiers.AMETHYST.getAttackDamage(), 0, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_sword"),
				ModItems.AIR_SWORD = setup(new AirSword(ItemTiers.STANDARD, (int)ItemTiers.STANDARD.getAttackDamage(), 0, new Item.Properties().group(ElementalSwords.tabSwords)), "air_sword"),
				ModItems.EARTH_SWORD = setup(new EarthSword(ItemTiers.STANDARD, (int)ItemTiers.STANDARD.getAttackDamage(), 0, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_sword"),
				ModItems.FIRE_SWORD = setup(new FireSword(ItemTiers.STANDARD, (int)ItemTiers.STANDARD.getAttackDamage(), 0, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_sword"),
				ModItems.LIGHTNING_SWORD = setup(new LightningSword(ItemTiers.LIGHTNING, (int)ItemTiers.LIGHTNING.getAttackDamage(), 0, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_sword"),
				ModItems.WATER_SWORD = setup(new WaterSword(ItemTiers.STANDARD, (int)ItemTiers.STANDARD.getAttackDamage(), 0, new Item.Properties().group(ElementalSwords.tabSwords)), "water_sword"),
				
				ModItems.AMETHSYT_HELMET = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_helmet"),
				ModItems.AMETHYST_CHESTPLATE = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_chestplate"),
				ModItems.AMETHYST_LEGGINGS = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_leggings"),
				ModItems.AMETHYST_BOOTS = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_boots"),
						
				ModItems.AIR_HELMET = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "air_helmet"),
				ModItems.AIR_CHESTPLATE = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "air_chestplate"),
				ModItems.AIR_LEGGINGS = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "air_leggings"),
				ModItems.AIR_BOOTS = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "air_boots"),
				
				ModItems.EARTH_HELMET = setup(new EarthArmor(ArmorMaterialTypes.EARTH, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_helmet"),
				ModItems.EARTH_CHESTPLATE = setup(new EarthArmor(ArmorMaterialTypes.EARTH, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_chestplate"),
				ModItems.EARTH_LEGGINGS = setup(new EarthArmor(ArmorMaterialTypes.EARTH, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_leggings"),
				ModItems.EARTH_BOOTS = setup(new EarthArmor(ArmorMaterialTypes.EARTH, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_boots"),
				
				ModItems.FIRE_HELMET = setup(new FireArmor(ArmorMaterialTypes.FIRE, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_helmet"),
				ModItems.FIRE_CHESTPLATE = setup(new FireArmor(ArmorMaterialTypes.FIRE, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_chestplate"),
				ModItems.FIRE_LEGGINGS = setup(new FireArmor(ArmorMaterialTypes.FIRE, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_leggings"),
				ModItems.FIRE_BOOTS = setup(new FireArmor(ArmorMaterialTypes.FIRE, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_boots"),
				
				ModItems.LIGHTNING_HELMET = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_helmet"),
				ModItems.LIGHTNING_CHESTPLATE = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_chestplate"),
				ModItems.LIGHTNING_LEGGINGS = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_leggings"),
				ModItems.LIGHTNING_BOOTS = setup(new ArmorItem(ArmorMaterialTypes.STANDARD, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_boots"),
				
				ModItems.WATER_HELMET = setup(new WaterArmor(ArmorMaterialTypes.STANDARD, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "water_helmet"),
				ModItems.WATER_CHESTPLATE = setup(new WaterArmor(ArmorMaterialTypes.STANDARD, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "water_chestplate"),
				ModItems.WATER_LEGGINGS = setup(new WaterArmor(ArmorMaterialTypes.STANDARD, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "water_leggings"),
				ModItems.WATER_BOOTS = setup(new WaterArmor(ArmorMaterialTypes.STANDARD, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "water_boots"),
				
				ModItems.AIR_BOSS_SPAWN_EGG = setupEggs("air_boss_spawn_egg", ModEntityTypes.ENTITY_AIR_BOSS, 9987080, 3381306),
				ModItems.AMETHYST_MINER_SPAWN_EGG = setupEggs("amethyst_miner_spawn_egg", ModEntityTypes.ENTITY_AMETHYST_MINER, 12071583, 000000),
				ModItems.EARTH_BOSS_SPAWN_EGG = setupEggs("earth_boss_spawn_egg", ModEntityTypes.ENTITY_EARTH_BOSS, 9987080, 3381306),
				ModItems.FIRE_BOSS_SPAWN_EGG = setupEggs("fire_boss_spawn_egg", ModEntityTypes.ENTITY_FIRE_BOSS, 13313792, 13345280),
				ModItems.LIGHTNING_BOSS_SPAWN_EGG = setupEggs("lightning_boss_spawn_egg", ModEntityTypes.ENTITY_LIGHTNING_BOSS, 15398463, 16777215),
				ModItems.WATER_BOSS_SPAWN_EGG = setupEggs("water_boss_spawn_egg", ModEntityTypes.ENTITY_WATER_BOSS, 3093218, 3131903),
				
				setupBlockItems(new BlockItem(ModBlocks.AMETHYST_BLOCK, new Item.Properties().group(ElementalSwords.tabSwords))),
				setupBlockItems(new BlockItem(ModBlocks.AMETHYST_ORE, new Item.Properties().group(ElementalSwords.tabSwords))),
				setupBlockItems(new BlockItem(ModBlocks.AMETHYST_STONE, new Item.Properties().group(ElementalSwords.tabSwords))),
				setupBlockItems(new BlockItem(ModBlocks.IMBUEMENT_TABLE, new Item.Properties().group(ElementalSwords.tabSwords))),
				setupBlockItems(new BlockItem(ModBlocks.TELEPORTER_BLOCK, new Item.Properties().group(ElementalSwords.tabSwords)))			
				);
		ElementalSwords.logger.info("Items registered");
	}
	
	@SubscribeEvent
	public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
				ModBlocks.AMETHYST_BLOCK = new Block(Properties.create(Material.IRON).hardnessAndResistance(4.0f, 10.0f).lightValue(0).sound(SoundType.METAL)).setRegistryName(ElementalSwords.MOD_ID, "amethyst_block"),
				ModBlocks.AMETHYST_ORE = new AmethystOreBlock(Properties.create(Material.ROCK).hardnessAndResistance(3.0f, 10.0f).lightValue(0).sound(SoundType.STONE)).setRegistryName(ElementalSwords.MOD_ID, "amethyst_ore"),
				ModBlocks.AMETHYST_STONE = new Block(Properties.create(Material.ROCK).hardnessAndResistance(3.0f, 10.0f).lightValue(0).sound(SoundType.STONE)).setRegistryName(ElementalSwords.MOD_ID, "amethyst_stone"),
				ModBlocks.IMBUEMENT_TABLE = new ImbuementTableBlock(Properties.create(Material.IRON).hardnessAndResistance(4.0f, 15.0f).lightValue(0).sound(SoundType.METAL)).setRegistryName(ElementalSwords.MOD_ID, "imbuement_table"),
				ModBlocks.TELEPORTER_BLOCK = new TeleporterBlock(Properties.create(Material.IRON).hardnessAndResistance(5.0f,  15.0f).lightValue(0).sound(SoundType.METAL)).setRegistryName(ElementalSwords.MOD_ID, "teleporter_block")			
				);
		ElementalSwords.logger.info("Blocks registered");
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onRecipeSerializerRegistry(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().register(ModRecipeSerializers.IMBUEMENT_SHAPELESS = (IRecipeSerializer<ImbuementShapelessRecipes>) new ImbuementShapelessRecipes.Serializer().setRegistryName(new ResourceLocation(ElementalSwords.MOD_ID, "imbuement")));
	}
	
	@SubscribeEvent
	public static void onBiomeRegistry(RegistryEvent.Register<Biome> event) {	
		event.getRegistry().registerAll(
				ModBiomes.AMETHYST_DIMENSION_BIOME = new DimensionBiome(new SurfaceBuilderConfig(ModBlocks.AMETHYST_STONE.getDefaultState(), ModBlocks.AMETHYST_STONE.getDefaultState(), ModBlocks.AMETHYST_STONE.getDefaultState())).setRegistryName(ElementalSwords.MOD_ID, "amethyst_dimension_biome"),
				ModBiomes.FIRE_DIMENSION_BIOME = new DimensionBiome(new SurfaceBuilderConfig(Blocks.NETHER_BRICKS.getDefaultState(), Blocks.NETHERRACK.getDefaultState(), Blocks.QUARTZ_BLOCK.getDefaultState())).setRegistryName(ElementalSwords.MOD_ID, "fire_dimension_biome")
				);
	}
	
	@SubscribeEvent
	public static void onDimensionRegistry(RegistryEvent.Register<ModDimension> event) {
		event.getRegistry().registerAll(
				ModDimensions.AMETHYST_DIMENSION = new AmethystModDimension("amethyst_dimension"),
				ModDimensions.FIRE_DIMENSION = new FireModDimension("fire_dimension")
				);
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent 
	public static void onContainerTypeRegistry(RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(ModContainerTypes.IMBUEMENT_TABLE = (ContainerType<ImbuementTableContainer>) IForgeContainerType.create(ImbuementTableContainer::new).setRegistryName(ElementalSwords.MOD_ID, "imbuement_table"));
	}

	@SubscribeEvent
	public static void onEntityRegistry(RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().registerAll(
				ModEntityTypes.ENTITY_AMETHYST_MINER,
				ModEntityTypes.ENTITY_AIR_BOSS,
				ModEntityTypes.ENTITY_EARTH_BOSS,
				ModEntityTypes.ENTITY_FIRE_BOSS,
				ModEntityTypes.ENTITY_LIGHTNING_BOSS,
				ModEntityTypes.ENTITY_WATER_BOSS,
				ModEntityTypes.ENTITY_CHARGED_FIREBALL
				);
	}
	
	private static Item setupEggs(String name, EntityType<?> entityType, int primary, int secondary) {
		return new SpawnEggItem(entityType, primary, secondary, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(ElementalSwords.MOD_ID, name);
	}
	
	private static BlockItem setupBlockItems(BlockItem item) {
		item.setRegistryName(item.getBlock().getRegistryName());
		return item;
	}
	
	private static <T extends IForgeRegistryEntry<?>> T setup(final T entry, final String name) {
		entry.setRegistryName(new ResourceLocation(ElementalSwords.MOD_ID, name));
		return entry;
	}
	
	private static void createEntityTypes() {
		setup(ModEntityTypes.ENTITY_AIR_BOSS = EntityType.Builder.create(AirBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":air_boss"), "air_boss");
		setup(ModEntityTypes.ENTITY_AMETHYST_MINER = EntityType.Builder.create(AmethystMinerEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":amethyst_miner"), "amethyst_miner");
		setup(ModEntityTypes.ENTITY_EARTH_BOSS = EntityType.Builder.create(EarthBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":earth_boss"), "earth_boss");
		setup(ModEntityTypes.ENTITY_FIRE_BOSS = EntityType.Builder.create(FireBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).immuneToFire().build(ElementalSwords.MOD_ID + ":fire_boss"), "fire_boss");
		setup(ModEntityTypes.ENTITY_LIGHTNING_BOSS = EntityType.Builder.create(LightningBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":lightning_boss"), "lightning_boss");
		setup(ModEntityTypes.ENTITY_WATER_BOSS = EntityType.Builder.create(WaterBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":water_boss"), "water_boss");
		setup(ModEntityTypes.ENTITY_CHARGED_FIREBALL = EntityType.Builder.<ChargedFireballEntity>create(ChargedFireballEntity::new, EntityClassification.MISC).setTrackingRange(85).setCustomClientFactory((spawnEntity, world) -> new ChargedFireballEntity(world)).build(ElementalSwords.MOD_ID + ":charged_fireball"), "charged_fireball");
	}
}