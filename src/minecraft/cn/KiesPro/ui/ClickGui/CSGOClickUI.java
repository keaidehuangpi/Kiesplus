/*
 * Decompiled with CFR 0.136.
 */
package cn.KiesPro.ui.ClickGui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import cn.KiesPro.api.value.Mode;
import cn.KiesPro.api.value.Numbers;
import cn.KiesPro.api.value.Option;
import cn.KiesPro.api.value.Value;
import cn.KiesPro.management.ModuleManager;
import cn.KiesPro.module.Module;
import cn.KiesPro.module.ModuleType;
import cn.KiesPro.utils.render.RenderUtil;

public class CSGOClickUI extends GuiScreen implements GuiYesNoCallback {
	public static ModuleType currentModuleType = ModuleType.Combat;
	public static Module currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
			? ModuleManager.getModulesInType(currentModuleType).get(0)
			: null;
	public static float startX = 100, startY = 100;
	public int moduleStart = 0;
	public int valueStart = 0;
	boolean previousmouse = true;
	boolean mouse;
	public Opacity opacity = new Opacity(0);
	public int opacityx = 255;
	public float moveX = 0, moveY = 0;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (isHovered(startX, startY - 25, startX + 400, startY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
			if (moveX == 0 && moveY == 0) {
				moveX = mouseX - startX;
				moveY = mouseY - startY;
			} else {
				startX = mouseX - moveX;
				startY = mouseY - moveY;
			}
			this.previousmouse = true;
		} else if (moveX != 0 || moveY != 0) {
			moveX = 0;
			moveY = 0;
		}
		this.opacity.interpolate((float) opacityx);
		RenderUtil.drawRect(startX, startY, startX + 60, startY + 320,
				new Color(40, 40, 40, (int) opacity.getOpacity()).getRGB());
		RenderUtil.drawRect(startX + 60, startY, startX + 200, startY + 320,
				new Color(31, 31, 31, (int) opacity.getOpacity()).getRGB());
		RenderUtil.drawRect(startX + 200, startY, startX + 420, startY + 320,
				new Color(40, 40, 40, (int) opacity.getOpacity()).getRGB());
		for (int i = 0; i < ModuleType.values().length; i++) {
			ModuleType[] iterator = ModuleType.values();
			if (iterator[i] != currentModuleType) {
				RenderUtil.drawFilledCircle(startX + 30, startY + 30 + i * 40, 15,
						new Color(56, 56, 56, (int) opacity.getOpacity()).getRGB(), 5);
			} else {
				RenderUtil.drawFilledCircle(startX + 30, startY + 30 + i * 40, 15,
						new Color(101, 81, 255, (int) opacity.getOpacity()).getRGB(), 5);
			}
			try {
				if (this.isCategoryHovered(startX + 15, startY + 15 + i * 40, startX + 45, startY + 45 + i * 40, mouseX,
						mouseY) && Mouse.isButtonDown((int) 0)) {
					currentModuleType = iterator[i];
					currentModule = ModuleManager.getModulesInType(currentModuleType).size() != 0
							? ModuleManager.getModulesInType(currentModuleType).get(0)
							: null;
					moduleStart = 0;
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		int m = Mouse.getDWheel();
		if (this.isCategoryHovered(startX + 60, startY, startX + 200, startY + 320, mouseX, mouseY)) {
			if (m < 0 && moduleStart < ModuleManager.getModulesInType(currentModuleType).size() - 1) {
				moduleStart++;
			}
			if (m > 0 && moduleStart > 0) {
				moduleStart--;
			}
		}
		if (this.isCategoryHovered(startX + 200, startY, startX + 420, startY + 320, mouseX, mouseY)) {
			if (m < 0 && valueStart < currentModule.getValues().size() - 1) {
				valueStart++;
			}
			if (m > 0 && valueStart > 0) {
				valueStart--;
			}
		}
		mc.fontRendererObj.drawStringWithShadow(
				currentModule == null ? currentModuleType.toString()
						: currentModuleType.toString() + "/" + currentModule.getName(),
				startX + 70, startY + 15, new Color(248, 248, 248).getRGB());
		if (currentModule != null) {
			float mY = startY + 30;
			for (int i = 0; i < ModuleManager.getModulesInType(currentModuleType).size(); i++) {
				Module module = ModuleManager.getModulesInType(currentModuleType).get(i);
				if (mY > startY + 300)
					break;
				if (i < moduleStart) {
					continue;
				}

				RenderUtil.drawRect(startX + 75, mY, startX + 185, mY + 2,
						new Color(40, 40, 40, (int) opacity.getOpacity()).getRGB());
				mc.fontRendererObj.drawStringWithShadow(module.getName(), startX + 90, mY + 9,
						new Color(248, 248, 248, (int) opacity.getOpacity()).getRGB());
				if (!module.isEnabled()) {
					RenderUtil.drawFilledCircle(startX + 75, mY + 13, 2,
							new Color(255, 0, 0, (int) opacity.getOpacity()).getRGB(), 5);
				} else {

					RenderUtil.drawFilledCircle(startX + 75, mY + 13, 2,
							new Color(0, 255, 0, (int) opacity.getOpacity()).getRGB(), 5);
				}
				if (isSettingsButtonHovered(startX + 90, mY,
						startX + 100 + (mc.fontRendererObj.getStringWidth(module.getName())),
						mY + 8 + mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY)) {
					if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
						if (module.isEnabled()) {
							module.setEnabled(false);
						} else {
							module.setEnabled(true);
						}
						previousmouse = true;
					}
					if (!this.previousmouse && Mouse.isButtonDown((int) 1)) {
						previousmouse = true;
					}
				}

				if (!Mouse.isButtonDown((int) 0)) {
					this.previousmouse = false;
				}
				if (isSettingsButtonHovered(startX + 90, mY,
						startX + 100 + (mc.fontRendererObj.getStringWidth(module.getName())),
						mY + 8 + mc.fontRendererObj.FONT_HEIGHT, mouseX, mouseY) && Mouse.isButtonDown((int) 1)) {
					currentModule = module;
					valueStart = 0;
				}
				mY += 25;
			}
			mY = startY + 30;
			for (int i = 0; i < currentModule.getValues().size(); i++) {
				if (mY > startY + 300)
					break;
				if (i < valueStart) {
					continue;
				}
				Value value = currentModule.getValues().get(i);
				if (value instanceof Numbers) {
					float x = startX + 300;
					double render = (double) (68.0F
							* (((Number) value.getValue()).floatValue() - ((Numbers) value).getMinimum().floatValue())
							/ (((Numbers) value).getMaximum().floatValue()
									- ((Numbers) value).getMinimum().floatValue()));
					RenderUtil.drawRect((float) x - 6, mY + 2, (float) ((double) x + 75), mY + 3,
							(new Color(50, 50, 50, (int) opacity.getOpacity())).getRGB());
					RenderUtil.drawRect((float) x - 6, mY + 2, (float) ((double) x + render + 6.5D), mY + 3,
							(new Color(61, 141, 255, (int) opacity.getOpacity())).getRGB());
					RenderUtil.drawRect((float) ((double) x + render + 2D), mY, (float) ((double) x + render + 7D),
							mY + 5, (new Color(61, 141, 255, (int) opacity.getOpacity())).getRGB());
					mc.fontRendererObj.drawStringWithShadow(value.getName() + ": " + value.getValue(), startX + 210, mY, -1);
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}
					if (this.isButtonHovered(x, mY - 2, x + 100, mY + 7, mouseX, mouseY)
							&& Mouse.isButtonDown((int) 0)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
							render = ((Numbers) value).getMinimum().doubleValue();
							double max = ((Numbers) value).getMaximum().doubleValue();
							double inc = ((Numbers) value).getIncrement().doubleValue();
							double valAbs = (double) mouseX - ((double) x + 1.0D);
							double perc = valAbs / 68.0D;
							perc = Math.min(Math.max(0.0D, perc), 1.0D);
							double valRel = (max - render) * perc;
							double val = render + valRel;
							val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
							((Numbers) value).setValue(Double.valueOf(val));
						}
						if (!Mouse.isButtonDown((int) 0)) {
							this.previousmouse = false;
						}
					}
					mY += 20;
				}
				if (value instanceof Option) {
					float x = startX + 300;
					/*font.drawString(value.getName(), startX + 210, mY, -1);
					RenderUtil.drawRect(x + 56, mY, x + 76, mY + 1,
							new Color(255, 255, 255, (int) opacity.getOpacity()).getRGB());
					RenderUtil.drawRect(x + 56, mY + 8, x + 76, mY + 9,
							new Color(255, 255, 255, (int) opacity.getOpacity()).getRGB());
					RenderUtil.drawRect(x + 56, mY, x + 57, mY + 9,
							new Color(255, 255, 255, (int) opacity.getOpacity()).getRGB());
					RenderUtil.drawRect(x + 77, mY, x + 76, mY + 9,
							new Color(255, 255, 255, (int) opacity.getOpacity()).getRGB());
					if ((boolean) value.getValue()) {
						RenderUtil.drawRect(x + 67, mY + 2, x + 75, mY + 7,
								new Color(61, 141, 255, (int) opacity.getOpacity()).getRGB());
					} else {
						RenderUtil.drawRect(x + 58, mY + 2, x + 65, mY + 7,
								new Color(150, 150, 150, (int) opacity.getOpacity()).getRGB());
					}*/
					mc.fontRendererObj.drawStringWithShadow(value.getName(), startX + 210, mY, -1);
					Gui.drawRect(x + 56, mY, x + 76, mY + 1, new Color(255, 255, 255).getRGB());
					Gui.drawRect(x + 56, mY + 8, x + 76, mY + 9, new Color(255, 255, 255).getRGB());
					Gui.drawRect(x + 56, mY, x + 57, mY + 9, new Color(255, 255, 255).getRGB());
					Gui.drawRect(x + 77, mY, x + 76, mY + 9, new Color(255, 255, 255).getRGB());
					if ((boolean) value.getValue()) {
						Gui.drawRect(x + 67, mY + 2, x + 75, mY + 7,
								new Color(61, 141, 255).getRGB());
					} else {
						Gui.drawRect(x + 58, mY + 2, x + 65, mY + 7,
								new Color(150, 150, 150).getRGB());
				}
					if (this.isCheckBoxHovered(x + 56, mY, x + 76, mY + 9, mouseX, mouseY)) {
						if (!this.previousmouse && Mouse.isButtonDown((int) 0)) {
							this.previousmouse = true;
							this.mouse = true;
						}

						if (this.mouse) {
							value.setValue(!(boolean) value.getValue());
							this.mouse = false;
						}
					}
					if (!Mouse.isButtonDown((int) 0)) {
						this.previousmouse = false;
					}
					mY += 20;
				}
				if (value instanceof Mode) {
					float x = startX + 300;
					mc.fontRendererObj.drawStringWithShadow(value.getName(), startX + 210, mY, -1);
					RenderUtil.drawRect(x - 5, mY - 5, x + 90, mY + 15,
							new Color(56, 56, 56, (int) opacity.getOpacity()).getRGB());
					RenderUtil.drawBorderRect(x - 5, mY - 5, x + 90, mY + 15,
							new Color(101, 81, 255, (int) opacity.getOpacity()).getRGB(), 2);
					mc.fontRendererObj.drawStringWithShadow(((Mode) value).getModeAsString(),
							(float) (x + 44 - mc.fontRendererObj.getStringWidth(((Mode) value).getModeAsString()) / 2), mY + 1, -1);
					if (this.isStringHovered(x, mY - 5, x + 100, mY + 15, mouseX, mouseY)) {
						if (Mouse.isButtonDown((int) 0) && !this.previousmouse) {
							Enum current = (Enum) ((Mode) value).getValue();
							int next = current.ordinal() + 1 >= ((Mode) value).getModes().length ? 0
									: current.ordinal() + 1;
							value.setValue(((Mode) value).getModes()[next]);
							this.previousmouse = true;
						}
						if (!Mouse.isButtonDown((int) 0)) {
							this.previousmouse = false;
						}

					}
					mY += 25;
				}
			}
		}

	}

	public boolean isStringHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isSettingsButtonHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isButtonHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCheckBoxHovered(float f, float y, float g, float y2, int mouseX, int mouseY) {
		if (mouseX >= f && mouseX <= g && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isCategoryHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
			return true;
		}

		return false;
	}

	@Override
	public void onGuiClosed() {
		this.opacity.setOpacity(0);
	}
}
