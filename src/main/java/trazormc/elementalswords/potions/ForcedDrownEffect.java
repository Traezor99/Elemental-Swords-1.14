package trazormc.elementalswords.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import trazormc.elementalswords.ElementalSwords;

public class ForcedDrownEffect extends Effect {
	private final ResourceLocation texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/gui/forced_drown_effect.png");

	public ForcedDrownEffect(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}
	
	@Override
	public void performEffect(LivingEntity entityLivingIn, int amplifier) {
		if(entityLivingIn instanceof PlayerEntity) {
			PlayerEntity player = ((PlayerEntity)entityLivingIn);
			if(!player.isCreative() && !player.isSpectator()) 
				player.attackEntityFrom(DamageSource.DROWN, amplifier + 4);
		}
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void renderHUDEffect(EffectInstance effect, AbstractGui gui, int x, int y, float z, float alpha) {
		Minecraft.getInstance().textureManager.bindTexture(texture);
		AbstractGui.blit(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
		
	}
	
	@Override
	public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, int x, int y, float z) {
		Minecraft.getInstance().textureManager.bindTexture(texture);
		AbstractGui.blit(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
	}
}
