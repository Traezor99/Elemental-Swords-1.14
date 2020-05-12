package trazormc.elementalswords.items.armor;

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

public class EarthArmor extends ArmorItem {

	public EarthArmor(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Item.Properties properties) {
		super(materialIn, equipmentSlotIn, properties);
	}
	
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		super.onArmorTick(stack, world, player);
		
		if(player.inventory.armorInventory.get(3).getItem() == ModItems.EARTH_HELMET 
				&& player.inventory.armorInventory.get(2).getItem() == ModItems.EARTH_CHESTPLATE
				&& player.inventory.armorInventory.get(1).getItem() == ModItems.EARTH_LEGGINGS
				&& player.inventory.armorInventory.get(0).getItem() == ModItems.EARTH_BOOTS)
			ModUtils.effectPlayer(player, Effects.HASTE, 200, 1);
	}
}
