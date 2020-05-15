package trazormc.elementalswords.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
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
import trazormc.elementalswords.entities.AirBossEntity;
import trazormc.elementalswords.entities.ChargedFireballEntity;
import trazormc.elementalswords.entities.EarthBossEntity;
import trazormc.elementalswords.entities.FireBossEntity;
import trazormc.elementalswords.entities.HailEntity;
import trazormc.elementalswords.entities.LightningBossEntity;
import trazormc.elementalswords.entities.WaterBossEntity;
import trazormc.elementalswords.entities.WindSeekerEntity;
import trazormc.elementalswords.holders.ModBlocks;
import trazormc.elementalswords.holders.ModContainerTypes;
import trazormc.elementalswords.holders.ModEffects;
import trazormc.elementalswords.holders.ModEntityTypes;
import trazormc.elementalswords.holders.ModItems;
import trazormc.elementalswords.holders.ModRecipeSerializers;
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
import trazormc.elementalswords.potions.ForcedDrownEffect;
import trazormc.elementalswords.util.ArmorMaterials;
import trazormc.elementalswords.util.ItemTiers;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {

	@SubscribeEvent
	public static void onItemRegistry(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				ModItems.AMETHYST = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "amethyst"),
				ModItems.SUMMONING_SHARD = new Item(new Item.Properties().maxStackSize(1).group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "summoning_shard"),

				ModItems.AIR_SHARD = setup(new AirShardItem(new Item.Properties().maxStackSize(1).group(ElementalSwords.tabSwords)), "air_shard"),
				ModItems.EARTH_SHARD = setup(new EarthShardItem(new Item.Properties().maxStackSize(1).group(ElementalSwords.tabSwords)), "earth_shard"),
				ModItems.FIRE_SHARD = setup(new FireShardItem(new Item.Properties().maxStackSize(1).group(ElementalSwords.tabSwords)), "fire_shard"),
				ModItems.LIGHTNING_SHARD = setup(new LightningShardItem(new Item.Properties().maxStackSize(1).group(ElementalSwords.tabSwords)), "lightning_shard"),
				ModItems.WATER_SHARD = setup(new WaterShardItem(new Item.Properties().maxStackSize(1).group(ElementalSwords.tabSwords)), "water_shard"),

				ModItems.GUST_OF_WIND = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "gust_of_wind"),
				ModItems.EARTH_STONE = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "earth_stone"),
				ModItems.FLAMING_EMBER = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "flaming_ember"),
				ModItems.SPARK_OF_ENERGY = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "spark_of_energy"),
				ModItems.SHIMMERING_WATER = new Item(new Item.Properties().group(ElementalSwords.tabSwords)).setRegistryName(ElementalSwords.MOD_ID, "shimmering_water"),
				
				//For attack damage, it adds whatever value is passed to the constructor with the IItemTier value
				ModItems.AMETHYST_SWORD = setup(new SwordItem(ItemTiers.AMETHYST, (int)ItemTiers.AMETHYST.getAttackDamage(), -2.2f, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_sword"),
				ModItems.AIR_SWORD = setup(new AirSword(ItemTiers.STANDARD, (int)ItemTiers.STANDARD.getAttackDamage(), -2.2f, new Item.Properties().group(ElementalSwords.tabSwords)), "air_sword"),
				ModItems.EARTH_SWORD = setup(new EarthSword(ItemTiers.STANDARD, (int)ItemTiers.STANDARD.getAttackDamage(), -2.2f, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_sword"),
				ModItems.FIRE_SWORD = setup(new FireSword(ItemTiers.STANDARD, (int)ItemTiers.STANDARD.getAttackDamage(), -2.2f, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_sword"),
				ModItems.LIGHTNING_SWORD = setup(new LightningSword(ItemTiers.LIGHTNING, (int)ItemTiers.LIGHTNING.getAttackDamage(), -2.2f, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_sword"),
				ModItems.WATER_SWORD = setup(new WaterSword(ItemTiers.STANDARD, (int)ItemTiers.STANDARD.getAttackDamage(), -2.2f, new Item.Properties().group(ElementalSwords.tabSwords)), "water_sword"),

				ModItems.AMETHSYT_HELMET = setup(new ArmorItem(ArmorMaterials.AMETHYST, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_helmet"),
				ModItems.AMETHYST_CHESTPLATE = setup(new ArmorItem(ArmorMaterials.AMETHYST, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_chestplate"),
				ModItems.AMETHYST_LEGGINGS = setup(new ArmorItem(ArmorMaterials.AMETHYST, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_leggings"),
				ModItems.AMETHYST_BOOTS = setup(new ArmorItem(ArmorMaterials.AMETHYST, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "amethyst_boots"),

				ModItems.AIR_HELMET = setup(new ArmorItem(ArmorMaterials.AIR, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "air_helmet"),
				ModItems.AIR_CHESTPLATE = setup(new ArmorItem(ArmorMaterials.AIR, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "air_chestplate"),
				ModItems.AIR_LEGGINGS = setup(new ArmorItem(ArmorMaterials.AIR, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "air_leggings"),
				ModItems.AIR_BOOTS = setup(new ArmorItem(ArmorMaterials.AIR, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "air_boots"),

				ModItems.EARTH_HELMET = setup(new EarthArmor(ArmorMaterials.EARTH, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_helmet"),
				ModItems.EARTH_CHESTPLATE = setup(new EarthArmor(ArmorMaterials.EARTH, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_chestplate"),
				ModItems.EARTH_LEGGINGS = setup(new EarthArmor(ArmorMaterials.EARTH, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_leggings"),
				ModItems.EARTH_BOOTS = setup(new EarthArmor(ArmorMaterials.EARTH, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "earth_boots"),

				ModItems.FIRE_HELMET = setup(new FireArmor(ArmorMaterials.FIRE, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_helmet"),
				ModItems.FIRE_CHESTPLATE = setup(new FireArmor(ArmorMaterials.FIRE, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_chestplate"),
				ModItems.FIRE_LEGGINGS = setup(new FireArmor(ArmorMaterials.FIRE, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_leggings"),
				ModItems.FIRE_BOOTS = setup(new FireArmor(ArmorMaterials.FIRE, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "fire_boots"),

				ModItems.LIGHTNING_HELMET = setup(new ArmorItem(ArmorMaterials.LIGHTNING, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_helmet"),
				ModItems.LIGHTNING_CHESTPLATE = setup(new ArmorItem(ArmorMaterials.LIGHTNING, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_chestplate"),
				ModItems.LIGHTNING_LEGGINGS = setup(new ArmorItem(ArmorMaterials.LIGHTNING, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_leggings"),
				ModItems.LIGHTNING_BOOTS = setup(new ArmorItem(ArmorMaterials.LIGHTNING, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "lightning_boots"),

				ModItems.WATER_HELMET = setup(new WaterArmor(ArmorMaterials.WATER, EquipmentSlotType.HEAD, new Item.Properties().group(ElementalSwords.tabSwords)), "water_helmet"),
				ModItems.WATER_CHESTPLATE = setup(new WaterArmor(ArmorMaterials.WATER, EquipmentSlotType.CHEST, new Item.Properties().group(ElementalSwords.tabSwords)), "water_chestplate"),
				ModItems.WATER_LEGGINGS = setup(new WaterArmor(ArmorMaterials.WATER, EquipmentSlotType.LEGS, new Item.Properties().group(ElementalSwords.tabSwords)), "water_leggings"),
				ModItems.WATER_BOOTS = setup(new WaterArmor(ArmorMaterials.WATER, EquipmentSlotType.FEET, new Item.Properties().group(ElementalSwords.tabSwords)), "water_boots"),

				setupBlockItems(new BlockItem(ModBlocks.AMETHYST_BLOCK, new Item.Properties().group(ElementalSwords.tabSwords))),
				setupBlockItems(new BlockItem(ModBlocks.AMETHYST_ORE, new Item.Properties().group(ElementalSwords.tabSwords))),
				setupBlockItems(new BlockItem(ModBlocks.AMETHYST_STONE, new Item.Properties().group(ElementalSwords.tabSwords))),
				setupBlockItems(new BlockItem(ModBlocks.IMBUEMENT_TABLE, new Item.Properties().group(ElementalSwords.tabSwords))),
				setupBlockItems(new BlockItem(ModBlocks.TELEPORTER_BLOCK, new Item.Properties().group(ElementalSwords.tabSwords)))			
				);
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
	}

	@SubscribeEvent 
	public static void onEffectRegistry(RegistryEvent.Register<Effect> event) {
		event.getRegistry().register(ModEffects.FORCED_DROWN = setup(new ForcedDrownEffect(EffectType.HARMFUL, 8181462), "forced_drown_effect"));
	}

	@SubscribeEvent
	public static void onRecipeSerializerRegistry(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().register(ModRecipeSerializers.IMBUEMENT_SHAPELESS = setup(new ImbuementShapelessRecipes.Serializer(), "imbuement"));
	}

	@SubscribeEvent 
	public static void onContainerTypeRegistry(RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(ModContainerTypes.IMBUEMENT_TABLE = setup(IForgeContainerType.create(ImbuementTableContainer::new), "imbuement_table"));
	}

	@SubscribeEvent
	public static void onEntityRegistry(RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().registerAll(
				ModEntityTypes.AIR_BOSS = setup(EntityType.Builder.create(AirBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":air_boss"), "air_boss"),
				ModEntityTypes.EARTH_BOSS = setup(EntityType.Builder.create(EarthBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":earth_boss"), "earth_boss"),
				ModEntityTypes.FIRE_BOSS = setup(EntityType.Builder.create(FireBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).immuneToFire().build(ElementalSwords.MOD_ID + ":fire_boss"), "fire_boss"),
				ModEntityTypes.LIGHTNING_BOSS = setup(EntityType.Builder.create(LightningBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":lightning_boss"), "lightning_boss"),
				ModEntityTypes.WATER_BOSS = setup(EntityType.Builder.create(WaterBossEntity::new, EntityClassification.MONSTER).setTrackingRange(85).size(0.6F, 1.95F).build(ElementalSwords.MOD_ID + ":water_boss"), "water_boss"),

				ModEntityTypes.CHARGED_FIREBALL = setup(EntityType.Builder.<ChargedFireballEntity>create(ChargedFireballEntity::new, EntityClassification.MISC).setTrackingRange(85).setCustomClientFactory((spawnEntity, world) -> new ChargedFireballEntity(world)).build(ElementalSwords.MOD_ID + ":charged_fireball"), "charged_fireball"),
				ModEntityTypes.HAIL = setup(EntityType.Builder.<HailEntity>create(HailEntity::new, EntityClassification.MISC).setTrackingRange(85).setCustomClientFactory((spawnEntity, world) -> new HailEntity(world)).build(ElementalSwords.MOD_ID + ":hail"), "hail"),
				ModEntityTypes.WIND_SEEKER = setup(EntityType.Builder.<WindSeekerEntity>create(WindSeekerEntity::new, EntityClassification.MISC).setTrackingRange(85).setCustomClientFactory((spawnEntity, world) -> new WindSeekerEntity(world)).build(ElementalSwords.MOD_ID + ":wind_seeker"), "wind_seeker")
				);
	}

	private static BlockItem setupBlockItems(BlockItem item) {
		item.setRegistryName(item.getBlock().getRegistryName());
		return item;
	}

	private static <T extends IForgeRegistryEntry<?>> T setup(final T entry, final String name) {
		entry.setRegistryName(new ResourceLocation(ElementalSwords.MOD_ID, name));
		return entry;
	}
}