/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.gui.settings;

import com.pclewis.mcpatcher.mod.Shaders;
import java.util.UUID;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;

public class FancyShadersButton extends AutomatedCheckBox {
	UUID fancyGraphics;
	public FancyShadersButton(UUID fancyGraphics) {
		super("Fancy Shaders");
		this.fancyGraphics = fancyGraphics;
		setChecked(ConfigReader.fancyShaders);
		setTooltip("Shaders\nWARNING: EXPERIMENTAL FEATURE!\nFast - disabled, faster\nFancy - enabled, slower\n");
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		ConfigReader.fancyShaders = !ConfigReader.fancyShaders;
		ConfigReader.write();
		Shaders.setEnabled(ConfigReader.fancyShaders);
	}
}