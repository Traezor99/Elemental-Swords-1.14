package trazormc.elementalswords.util;

import java.util.function.Supplier;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trazormc.elementalswords.init.ModItems;

public enum ArmorMaterialTypes implements IArmorMaterial {
	STANDARD("amethyst", 35, new int[] {4,6,8,4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3.0f, () -> {
	      return Ingredient.fromItems(ModItems.AMETHYST);
	   }),
	FIRE("fire", 35, new int[] {4,6,8,4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 3.0f, () -> {
	      return Ingredient.fromItems(ModItems.AMETHYST);
	   }),
	EARTH("earth", 35, new int[] {5,7,9,5}, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 4.0f, () -> {
	      return Ingredient.fromItems(ModItems.AMETHYST);
	   });
	
	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
	private final String name;
	private final int maxDamageFactor;
	private final int[] damageReductionAmounts;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final LazyLoadBase<Ingredient> repairMaterial;
	
	private ArmorMaterialTypes(String name, int maxDamageFactor, int[] damageReductionAmounts, int enchantability, SoundEvent sound, float toughness, Supplier<Ingredient> repairMaterial ) {
		this.name = name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmounts = damageReductionAmounts;
		this.enchantability = enchantability;
		this.soundEvent = sound;
		this.toughness = toughness;
		this.repairMaterial = new LazyLoadBase<>(repairMaterial);
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
		return this.repairMaterial.getValue();
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
