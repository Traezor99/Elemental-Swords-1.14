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

public class FireArmor extends ArmorItem {

	public FireArmor(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Item.Properties properties) {
		super(materialIn, equipmentSlotIn, properties);
	}
	
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		super.onArmorTick(stack, world, player);
		
		if(player.inventory.armorInventory.get(3).getItem() == ModItems.FIRE_HELMET 
				&& player.inventory.armorInventory.get(2).getItem() == ModItems.FIRE_CHESTPLATE
				&& player.inventory.armorInventory.get(1).getItem() == ModItems.FIRE_LEGGINGS
				&& player.inventory.armorInventory.get(0).getItem() == ModItems.FIRE_BOOTS)
			ModUtils.effectPlayer(player, Effects.FIRE_RESISTANCE, 200, 0);
	}
}

