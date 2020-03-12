package trazormc.elementalswords.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import trazormc.elementalswords.ElementalSwords;
import trazormc.elementalswords.containers.ImbuementTableContainer;

public class ImbuementTableGui extends ContainerScreen<ImbuementTableContainer> {
	
	private ResourceLocation texture;

	public ImbuementTableGui(ImbuementTableContainer container, PlayerInventory playerInventory, ITextComponent name) {
		super(container, playerInventory, name);
		this.xSize = 176;
		this.ySize = 166;
		texture = new ResourceLocation(ElementalSwords.MOD_ID, "textures/gui/imbuement_table.png");
	}
	
	@Override
	public void onClose() {
		super.onClose();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.font.drawString("Imbuement Table", 10, 5, 0x000000);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1f, 1f, 1f, 1f);
		this.minecraft.getTextureManager().bindTexture(texture);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) /2;
		this.blit(x, y, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
