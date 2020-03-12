package trazormc.elementalswords.items.swords;

import java.util.Random;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import trazormc.elementalswords.entities.AmethystMinerEntity;
import trazormc.elementalswords.init.ModEntityTypes;
import trazormc.elementalswords.util.ModUtils;

public class LightningSword extends SwordItem {

	public LightningSword(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
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
	
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		BlockPos pos = ctx.getPos();
		LightningBoltEntity lightning = new LightningBoltEntity(ctx.getWorld(), ctx.getHitVec().x, ctx.getHitVec().y, ctx.getHitVec().z, false);
		ItemStack item = ctx.getPlayer().getHeldItem(ctx.getHand());

		lightning.setPosition(pos.getX(), pos.getY(), pos.getZ());
		//ctx.getWorld().addEntity(lightning);
		
		if(!ctx.getWorld().isRemote) {
			ctx.getPlayer().getServer().func_71218_a(ctx.getPlayer().dimension).addLightningBolt(lightning);
			item.attemptDamageItem(1, new Random(), (ServerPlayerEntity)ctx.getPlayer());		
		}
		return super.onItemUse(ctx);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		LightningBoltEntity lightning = new LightningBoltEntity(playerIn.world, target.posX, target.posY, target.posZ, false);
			 
		lightning.setPosition(target.posX, target.posY, target.posZ);
		playerIn.world.addEntity(lightning);
			
		if(!playerIn.world.isRemote) {
			stack.attemptDamageItem(1, new Random(), (ServerPlayerEntity)playerIn);
		}
		return true;		
	}
}
