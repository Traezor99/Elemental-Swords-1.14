package trazormc.elementalswords.util;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.init.ModItems;

public enum ArmorMaterials implements IArmorMaterial {
	AMETHYST("amethyst", 40, new int[] {4,6,8,4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3.0f, ModItems.AMETHYST),
	AIR("air", 40, new int[] {4,6,8,4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3.0f, ModItems.AMETHYST),
	EARTH("earth", 40, new int[] {5,7,9,5}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 4.0f, ModItems.AMETHYST),
	FIRE("fire", 40, new int[] {5,6,9,4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3.0f, ModItems.AMETHYST),
	LIGHTNING("lightning", 40, new int[] {4,6,8,4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3.0f, ModItems.AMETHYST),
	WATER("water", 40, new int[] {4,6,8,4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3.0f, ModItems.AMETHYST);
	
	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int maxDamageFactor;
	private final int[] damageReductionAmounts;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final Item repairItem;
	
	private ArmorMaterials(String name, int maxDamageFactor, int[] damageReductionAmounts, int enchantability, SoundEvent sound, float toughness, Item repairItem) {
		this.name = ElementalSwords.MOD_ID + ":" + name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmounts = damageReductionAmounts;
		this.enchantability = enchantability;
		this.soundEvent = sound;
		this.toughness = toughness;
		this.repairItem = repairItem;
	}

	@Override
	public int getDurability(EquipmentSlotType slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slotIn) {
		return this.damageReductionAmounts[slotIn.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return this.soundEvent;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromItems(this.repairItem);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}
}
