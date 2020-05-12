package trazormc.elementalswords.util;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import trazormc.elementalswords.holders.ModItems;

public enum ItemTiers implements IItemTier {
	AMETHYST(3, 2500, 10.0F, 4.0F, 15, ModItems.AMETHYST),
	STANDARD(3, 2500, 10.0F, 4.0F, 0, ModItems.AMETHYST),
	LIGHTNING(3, 2500, 10.0F, 5.0F, 0, ModItems.AMETHYST);
	
	private final int harvestLevel;
	private final int maxUses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	private final Item repairItem;
	
	private ItemTiers(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Item repairItem) {
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairItem = repairItem;
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
		return Ingredient.fromItems(repairItem);
	}
}
