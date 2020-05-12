package trazormc.elementalswords.items.armor;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import trazormc.elementalswords.holders.ModItems;
import trazormc.elementalswords.util.ModUtils;

public class WaterArmor extends ArmorItem {

	public WaterArmor(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Item.Properties properties) {
		super(materialIn, equipmentSlotIn, properties);
	}
	
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		super.onArmorTick(stack, world, player);
		
		if(player.inventory.armorInventory.get(3).getItem() == ModItems.WATER_HELMET 
				&& player.inventory.armorInventory.get(2).getItem() == ModItems.WATER_CHESTPLATE
				&& player.inventory.armorInventory.get(1).getItem() == ModItems.WATER_LEGGINGS
				&& player.inventory.armorInventory.get(0).getItem() == ModItems.WATER_BOOTS) {
			ModUtils.effectPlayer(player, Effects.NIGHT_VISION, 200, 0);
			ModUtils.effectPlayer(player, Effects.WATER_BREATHING, 200, 0);
		}
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		if(stack.getItem() == ModItems.WATER_HELMET) {
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
			if(map.get(Enchantments.AQUA_AFFINITY) == null) {
				stack.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
			}
		} else if(stack.getItem() == ModItems.WATER_BOOTS) {
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
			if(map.get(Enchantments.DEPTH_STRIDER) == null) {
				stack.addEnchantment(Enchantments.DEPTH_STRIDER, 3);
			}
		}
	}
}
