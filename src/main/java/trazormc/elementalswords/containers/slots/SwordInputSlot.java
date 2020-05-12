package trazormc.elementalswords.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import trazormc.elementalswords.holders.ModItems;

public class SwordInputSlot extends Slot {

	public SwordInputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return super.getItemStackLimit(stack);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return isValid(stack.getItem());
	}
	
	private boolean isValid(Item item) {
		if(item == ModItems.AMETHYST_SWORD || item == ModItems.AMETHSYT_HELMET || item == ModItems.AMETHYST_CHESTPLATE 
				|| item == ModItems.AMETHYST_LEGGINGS || item == ModItems.AMETHYST_BOOTS || item == ModItems.SUMMONING_SHARD) {
			return true;
		}
		return false;
	}

}
