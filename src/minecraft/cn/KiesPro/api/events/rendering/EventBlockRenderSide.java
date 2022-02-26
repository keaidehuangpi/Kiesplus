/*******************************************************************************
 *     DarkForge a Forge Hacked Client
 *     Copyright (C) 2017  Hexeption (Keir Davis)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package cn.KiesPro.api.events.rendering;

import cn.KiesPro.api.Event;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Keir on 21/04/2017.
 */
public class EventBlockRenderSide extends Event {

	private final IBlockState state;
	private final IBlockAccess world;
	public final BlockPos pos;
	private final EnumFacing side;
	private boolean toRender;
	public double maxX;
	public double maxY;
	public double maxZ;
	public double minX;
	public double minY;
	public double minZ;

	public EventBlockRenderSide(IBlockAccess world, BlockPos pos, EnumFacing side, double maxX, double minX,
			double maxY, double minY, double maxZ, double minZ) {
		this.state = Minecraft.getMinecraft().theWorld.getBlockState(pos);
		this.world = world;
		this.pos = pos;
		this.side = side;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
	}

	public IBlockState getState() {
		return state;
	}

	public IBlockAccess getWorld() {
		return world;
	}

	public BlockPos getPos() {
		return pos;
	}

	public EnumFacing getSide() {
		return side;
	}

	public boolean isToRender() {
		return toRender;
	}

	public void setToRender(boolean toRender) {
		this.toRender = toRender;
	}
}
