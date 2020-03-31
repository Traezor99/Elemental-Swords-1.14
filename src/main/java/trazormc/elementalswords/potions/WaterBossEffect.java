package trazormc.elementalswords.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import trazormc.elementalswords.ElementalSwords;

public class WaterBossEffect extends Effect {
	private final ResourceLocation texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/gui/water_boss_effect.png");

	public WaterBossEffect(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		if(entityLivingBaseIn instanceof PlayerEntity) {
			PlayerEntity player = ((PlayerEntity)entityLivingBaseIn);
			if(!player.isCreative() && !player.isSpectator()) {
				player.onKillCommand();
			}
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUDEffect(EffectInstance effect, AbstractGui gui, int x, int y, float z, float alpha) {
		super.renderHUDEffect(effect, gui, x, y, z, alpha);
		Minecraft.getInstance().textureManager.bindTexture(texture);
		AbstractGui.blit(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
		
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, int x, int y, float z) {
		super.renderInventoryEffect(effect, gui, x, y, z);
		Minecraft.getInstance().textureManager.bindTexture(texture);
		AbstractGui.blit(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
	}
}
