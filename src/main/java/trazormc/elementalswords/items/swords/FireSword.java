package trazormc.elementalswords.items.swords;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class FireSword extends SwordItem {

	public FireSword(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack item = playerIn.getHeldItem(handIn);
		if(!worldIn.isRemote) {
			Vec3d aim = playerIn.getLookVec();
			FireballEntity fireball = new FireballEntity(worldIn, playerIn, 1, 1, 1);
			
			fireball.setPosition(playerIn.posX + aim.x * 1.5d, playerIn.posY + 0.5d + aim.y * 1.5d, playerIn.posZ + aim.z * 1.5d);
			fireball.accelerationX = aim.x * 0.1;
			fireball.accelerationY = aim.y * 0.1;
			fireball.accelerationZ = aim.z * 0.1;
			worldIn.addEntity(fireball);
			fireball.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);


			item.damageItem(1, (ServerPlayerEntity)playerIn, (serverPlayer) -> {
				serverPlayer.sendBreakAnimation(EquipmentSlotType.MAINHAND);
			});		
			return new ActionResult<ItemStack>(ActionResultType.SUCCESS, item);
		} else {
			return new ActionResult<ItemStack>(ActionResultType.FAIL, item);
		}
	}	

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
		if(map.get(Enchantments.FIRE_ASPECT) == null) {
			stack.addEnchantment(Enchantments.FIRE_ASPECT, 2);
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Does 4 extra damage to Blaze, Wither Skeletons, Magma Cubes, Zombie Pigmen, Ghasts, and the Wither."));
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hitEntity(stack, target, attacker);
		if(target.isImmuneToFire() && !(target instanceof EnderDragonEntity) && !(target instanceof ShulkerEntity)) {
			if(attacker instanceof PlayerEntity) {
				target.hurtResistantTime = 0;
				target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity)attacker), 4);
			} else {
				target.attackEntityFrom(DamageSource.causeMobDamage(attacker), 4);
			}
		}
		return true;
	}
}
