package trazormc.elementalswords.items.swords;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trazormc.elementalswords.entities.AmethystMinerEntity;
import trazormc.elementalswords.init.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class WaterSword extends SwordItem {

	public WaterSword(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		if(!worldIn.isRemote) {
			AmethystMinerEntity miner = new AmethystMinerEntity(ModEntityTypes.ENTITY_AMETHYST_MINER, worldIn);
			double x = playerIn.posX + 5;
			double z = playerIn.posZ + 5;
			double y = ModUtils.calculateGenerationHeight(worldIn, (int)x, (int)z);
			
			miner.setPosition(x, y +1, z);
			worldIn.addEntity(miner);
		}
	}
	
	//Add drowned	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		super.hitEntity(stack, target, attacker);
		if(target instanceof GuardianEntity) {
			if(attacker instanceof PlayerEntity) {
				target.hurtResistantTime = 0;
			target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity)attacker), 4);
			} else {
				target.attackEntityFrom(DamageSource.causeMobDamage(attacker), 4);
			}
		}
		return true;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Does 4 extra damage to Guardians and Drowned."));
	}

}
