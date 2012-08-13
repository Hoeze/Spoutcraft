package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ChunkCache implements IBlockAccess {
	private int chunkX;
	private int chunkZ;
	private Chunk[][] chunkArray;
	private boolean field_72814_d;
	private World worldObj;

	public ChunkCache(World par1World, int par2, int par3, int par4, int par5, int par6, int par7) {
		this.worldObj = par1World;
		this.chunkX = par2 >> 4;
		this.chunkZ = par4 >> 4;
		int var8 = par5 >> 4;
		int var9 = par7 >> 4;
		this.chunkArray = new Chunk[var8 - this.chunkX + 1][var9 - this.chunkZ + 1];
		this.field_72814_d = true;

		for (int var10 = this.chunkX; var10 <= var8; ++var10) {
			for (int var11 = this.chunkZ; var11 <= var9; ++var11) {
				Chunk var12 = par1World.getChunkFromChunkCoords(var10, var11);
				if (var12 != null) {
					this.chunkArray[var10 - this.chunkX][var11 - this.chunkZ] = var12;
					if (!var12.getAreLevelsEmpty(par3, par6)) {
						this.field_72814_d = false;
					}
				}
			}
		}
	}

	public boolean func_72806_N() {
		return this.field_72814_d;
	}

	public int getBlockId(int par1, int par2, int par3) {
		if (par2 < 0) {
			return 0;
		} else if (par2 >= 256) {
			return 0;
		} else {
			int var4 = (par1 >> 4) - this.chunkX;
			int var5 = (par3 >> 4) - this.chunkZ;
			if (var4 >= 0 && var4 < this.chunkArray.length && var5 >= 0 && var5 < this.chunkArray[var4].length) {
				Chunk var6 = this.chunkArray[var4][var5];
				return var6 == null ? 0 : var6.getBlockID(par1 & 15, par2, par3 & 15);
			} else {
				return 0;
			}
		}
	}

	public TileEntity getBlockTileEntity(int par1, int par2, int par3) {
		int var4 = (par1 >> 4) - this.chunkX;
		int var5 = (par3 >> 4) - this.chunkZ;
		return this.chunkArray[var4][var5].getChunkBlockTileEntity(par1 & 15, par2, par3 & 15);
	}

	public float getBrightness(int par1, int par2, int par3, int par4) {
		int var5 = this.getLightValue(par1, par2, par3);
		if (var5 < par4) {
			var5 = par4;
		}

		return this.worldObj.worldProvider.lightBrightnessTable[var5];
	}

	public int getLightBrightnessForSkyBlocks(int par1, int par2, int par3, int par4) {
		int var5 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, par1, par2, par3);
		int var6 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Block, par1, par2, par3);
		if (var6 < par4) {
			var6 = par4;
		}

		return var5 << 20 | var6 << 4;
	}

	public float getLightBrightness(int par1, int par2, int par3) {
		return this.worldObj.worldProvider.lightBrightnessTable[this.getLightValue(par1, par2, par3)];
	}

	public int getLightValue(int par1, int par2, int par3) {
		return this.getLightValueExt(par1, par2, par3, true);
	}

	public int getLightValueExt(int par1, int par2, int par3, boolean par4) {
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 <= 30000000) {
			int var5;
			int var6;
			if (par4) {
				var5 = this.getBlockId(par1, par2, par3);
				if (var5 == Block.field_72079_ak.blockID || var5 == Block.field_72092_bO.blockID || var5 == Block.tilledField.blockID || var5 == Block.stairCompactPlanks.blockID || var5 == Block.stairCompactCobblestone.blockID) {
					var6 = this.getLightValueExt(par1, par2 + 1, par3, false);
					int var7 = this.getLightValueExt(par1 + 1, par2, par3, false);
					int var8 = this.getLightValueExt(par1 - 1, par2, par3, false);
					int var9 = this.getLightValueExt(par1, par2, par3 + 1, false);
					int var10 = this.getLightValueExt(par1, par2, par3 - 1, false);
					if (var7 > var6) {
						var6 = var7;
					}

					if (var8 > var6) {
						var6 = var8;
					}

					if (var9 > var6) {
						var6 = var9;
					}

					if (var10 > var6) {
						var6 = var10;
					}

					return var6;
				}
			}

			if (par2 < 0) {
				return 0;
			} else if (par2 >= 256) {
				var5 = 15 - this.worldObj.skylightSubtracted;
				if (var5 < 0) {
					var5 = 0;
				}

				return var5;
			} else {
				var5 = (par1 >> 4) - this.chunkX;
				var6 = (par3 >> 4) - this.chunkZ;
				return this.chunkArray[var5][var6].getBlockLightValue(par1 & 15, par2, par3 & 15, this.worldObj.skylightSubtracted);
			}
		} else {
			return 15;
		}
	}

	public int getBlockMetadata(int par1, int par2, int par3) {
		if (par2 < 0) {
			return 0;
		} else if (par2 >= 256) {
			return 0;
		} else {
			int var4 = (par1 >> 4) - this.chunkX;
			int var5 = (par3 >> 4) - this.chunkZ;
			return this.chunkArray[var4][var5].getBlockMetadata(par1 & 15, par2, par3 & 15);
		}
	}

	public Material getBlockMaterial(int par1, int par2, int par3) {
		int var4 = this.getBlockId(par1, par2, par3);
		return var4 == 0 ? Material.air : Block.blocksList[var4].blockMaterial;
	}

	public BiomeGenBase getBiomeGenForCoords(int par1, int par2) {
		return this.worldObj.getBiomeGenForCoords(par1, par2);
	}

	public boolean isBlockOpaqueCube(int par1, int par2, int par3) {
		Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	public boolean isBlockNormalCube(int par1, int par2, int par3) {
		Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
		return var4 == null ? false : var4.blockMaterial.blocksMovement() && var4.renderAsNormalBlock();
	}

	public boolean func_72797_t(int par1, int par2, int par3) {
		Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
		return var4 == null ? false : (var4.blockMaterial.isOpaque() && var4.renderAsNormalBlock() ? true : (var4 instanceof BlockStairs ? (this.getBlockMetadata(par1, par2, par3) & 4) == 4 : (var4 instanceof BlockHalfSlab ? (this.getBlockMetadata(par1, par2, par3) & 8) == 8 : false)));
	}

	public boolean isAirBlock(int par1, int par2, int par3) {
		Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
		return var4 == null;
	}

	public int getSkyBlockTypeBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
		if (par3 < 0) {
			par3 = 0;
		}

		if (par3 >= 256) {
			par3 = 255;
		}

		if (par3 >= 0 && par3 < 256 && par2 >= -30000000 && par4 >= -30000000 && par2 < 30000000 && par4 <= 30000000) {
			int var5;
			int var6;
			if (Block.useNeighborBrightness[this.getBlockId(par2, par3, par4)]) {
				var5 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3 + 1, par4);
				var6 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2 + 1, par3, par4);
				int var7 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2 - 1, par3, par4);
				int var8 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3, par4 + 1);
				int var9 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3, par4 - 1);
				if (var6 > var5) {
					var5 = var6;
				}

				if (var7 > var5) {
					var5 = var7;
				}

				if (var8 > var5) {
					var5 = var8;
				}

				if (var9 > var5) {
					var5 = var9;
				}

				return var5;
			} else {
				var5 = (par2 >> 4) - this.chunkX;
				var6 = (par4 >> 4) - this.chunkZ;
				return this.chunkArray[var5][var6].getSavedLightValue(par1EnumSkyBlock, par2 & 15, par3, par4 & 15);
			}
		} else {
			return par1EnumSkyBlock.defaultLightValue;
		}
	}

	public int getSpecialBlockBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
		if (par3 < 0) {
			par3 = 0;
		}

		if (par3 >= 256) {
			par3 = 255;
		}

		if (par3 >= 0 && par3 < 256 && par2 >= -30000000 && par4 >= -30000000 && par2 < 30000000 && par4 <= 30000000) {
			int var5 = (par2 >> 4) - this.chunkX;
			int var6 = (par4 >> 4) - this.chunkZ;
			return this.chunkArray[var5][var6].getSavedLightValue(par1EnumSkyBlock, par2 & 15, par3, par4 & 15);
		} else {
			return par1EnumSkyBlock.defaultLightValue;
		}
	}

	public int getHeight() {
		return 256;
	}

//Spout start
	public int getGrassColorCache(int x, int y, int z) {
		Chunk chunk = Minecraft.theMinecraft.field_71441_e.getChunkFromBlockCoords(x, z);
		if (chunk != null) {
			return chunk.grassColorCache;
		}
		return 0xffffff;
	}

	public void setGrassColorCache(int x, int y, int z, int color) {
		Chunk chunk = Minecraft.theMinecraft.field_71441_e.getChunkFromBlockCoords(x, z);
		if (chunk != null) {
			chunk.grassColorCache = color;
		}
	}

	public int getWaterColorCache(int x, int y, int z) {
		Chunk chunk = Minecraft.theMinecraft.field_71441_e.getChunkFromBlockCoords(x, z);
		if (chunk != null) {
			return chunk.waterColorCache;
		}
		return 0xffffff;
	}

	public void setWaterColorCache(int x, int y, int z, int color) {
		Chunk chunk = Minecraft.theMinecraft.field_71441_e.getChunkFromBlockCoords(x, z);
		if (chunk != null) {
			chunk.waterColorCache = color;
		}
	}
	
	public WorldChunkManager getWorldChunkManager() {
		return Minecraft.theMinecraft.field_71441_e.getWorldChunkManager();
	}
//Spout end
}
