package trazormc.elementalswords.util;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;
import trazormc.elementalswords.init.ModItems;

public enum ItemTiers implements IItemTier {
	AMETHYST(3, 2500, 10.0F, 4.0F, 7, () -> {
		return Ingredient.fromItems(ModItems.AMETHYST);
	}),
	STANDARD(3, 2500, 10.0F, 4.0F, 0, () -> {
		return Ingredient.fromItems(ModItems.AMETHYST);
	}),
	LIGHTNING(3, 2500, 10.0F, 5.0F, 0, () -> {
		return Ingredient.fromItems(ModItems.AMETHYST);
	});
	
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final LazyLoadBase<Ingredient> repairMaterial;
	
	private ItemTiers(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairMaterial = new LazyLoadBase<>(repairMaterial);
	}

	@Override
	public int getMaxUses() {
		return maxUses;
	}

	@Override
	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public int getHarvestLevel() {
		return harvestLevel;
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return repairMaterial.getValue();
	}

}
