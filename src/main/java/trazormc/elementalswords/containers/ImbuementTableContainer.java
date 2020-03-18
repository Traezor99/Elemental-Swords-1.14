package trazormc.elementalswords.containers;

import java.util.Map;
import java.util.Optional;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import trazormc.elementalswords.containers.slots.SwordInputSlot;
import trazormc.elementalswords.init.ModContainerTypes;
import trazormc.elementalswords.init.ModRecipeSerializers;
import trazormc.elementalswords.items.swords.FireSword;

public class ImbuementTableContainer extends Container  {
	
	public CraftingInventory imbuementInventory = new CraftingInventory(this, 3, 1);
	public CraftResultInventory imbuementResult = new CraftResultInventory();
	private final PlayerEntity player;	
	public ImbuementTableContainer(int id, PlayerInventory playerInventory, PacketBuffer data) {
		this(id, playerInventory);
	}
	
	public ImbuementTableContainer(int id, PlayerInventory playerInventory) {
		this(id, playerInventory, IWorldPosCallable.DUMMY);
	}
	
	public ImbuementTableContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPos) {
		super(ModContainerTypes.IMBUEMENT_TABLE, id);
		player = playerInventory.player;
		
		this.addSlot(new CraftingResultSlot(player, imbuementInventory, imbuementResult, 0, 124, 35));
		this.addSlot(new Slot(imbuementInventory, 1, 66, 35));
		this.addSlot(new SwordInputSlot(imbuementInventory, 0, 30, 35));
		
		for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
	}
	
	@Override
	public boolean getCanCraft(PlayerEntity player) {
		return true;
	}
	
	@Override
	public void setCanCraft(PlayerEntity player, boolean canCraft) {
		super.setCanCraft(player, true);
	}
	
	protected void slotChangedCraftMatrix(int windowId, World world, PlayerEntity player, CraftingInventory inventory, CraftResultInventory resultInventory) {
		if(!world.isRemote) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
	        ItemStack itemstack = ItemStack.EMPTY;
	        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(imbuementInventory.getStackInSlot(0));
	        Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, inventory, world);
	        if(optional.isPresent()) {
	        	ICraftingRecipe icraftingrecipe = optional.get(); 
	            if(resultInventory.canUseRecipe(world, serverplayerentity, icraftingrecipe) && icraftingrecipe.getSerializer() == ModRecipeSerializers.IMBUEMENT_SHAPELESS) {	     
	            	itemstack = icraftingrecipe.getCraftingResult(inventory);
	            	if(itemstack.getItem().getClass() == FireSword.class) {
	            		enchantments.put(Enchantments.FIRE_ASPECT, 2);
	                }
	                EnchantmentHelper.setEnchantments(enchantments, itemstack);
	            }
	        }

	        resultInventory.setInventorySlotContents(0, itemstack);
	        serverplayerentity.connection.sendPacket(new SSetSlotPacket(windowId, 0, itemstack));
		}
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		this.detectAndSendChanges();
		slotChangedCraftMatrix(windowId, player.world, player, imbuementInventory, imbuementResult);
	}
	
	@Override
	protected void clearContainer(PlayerEntity playerIn, World worldIn, IInventory inventoryIn) {
		imbuementInventory.clear();
		imbuementResult.clear();
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		if(playerIn.isSneaking())
			return false;
		else
			return true;
	}
	
	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);	      
	    for(int i = 0; i < imbuementInventory.getSizeInventory(); i ++) {
	    	ItemStack stack = imbuementInventory.getStackInSlot(i).copy();
	    	playerIn.addItemStackToInventory(stack);
	    }
	    this.clearContainer(playerIn, playerIn.world, imbuementInventory);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0) {
                itemstack1.getItem().onCreated(itemstack1, playerIn.world, playerIn);

                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index >= 3 && index < 30) {
                if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 30 && index < 39) {
                if (!this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.dropItem(itemstack2, false);
            }
        }        
        return itemstack;
	}
	
	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return slotIn.inventory != this.imbuementResult && super.canMergeSlot(stack, slotIn);
	}
}
