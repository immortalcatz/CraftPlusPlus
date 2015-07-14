package io.github.anon10w1z.craftPP.items;

import io.github.anon10w1z.craftPP.entities.EntityObsidianBoat;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import java.util.List;

/**
 * Obsidian boat item to along-side obsidian boat entity
 */
public class ItemObsidianBoat extends Item {
	public ItemObsidianBoat() {
		super();
		this.setUnlocalizedName("boatObsidian");
		this.setCreativeTab(CreativeTabs.tabTransport);
		this.setMaxStackSize(1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch);
		float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);
		double d0 = player.posX;
		double d1 = player.posY + player.getEyeHeight();
		double d2 = player.posZ;
		Vec3 vec3 = new Vec3(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		int d3 = 5;
		Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
		MovingObjectPosition movingObjectPosition = world.rayTraceBlocks(vec3, vec31, true);
		if (movingObjectPosition == null)
			return itemstack;

		Vec3 vec32 = player.getLook(1);
		boolean flag = false;
		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand(1, 1, 1));
		for (Entity entity : entities)
			if (entity.canBeCollidedWith()) {
				float f10 = entity.getCollisionBorderSize();
				AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(f10, f10, f10);
				if (axisAlignedBB.isVecInside(vec3)) {
					flag = true;
					break;
				}
			}
		if (flag)
			return itemstack;
		if (movingObjectPosition.typeOfHit == MovingObjectType.BLOCK) {
			BlockPos blockPos = movingObjectPosition.getBlockPos();
			if (world.getBlockState(blockPos).getBlock() == Blocks.snow_layer)
				blockPos = blockPos.down();
			EntityObsidianBoat obsidianBoat = new EntityObsidianBoat(world, (float) blockPos.getX() + 0.5, (float) blockPos.getY() + 1, (float) blockPos.getZ() + 0.5);
			obsidianBoat.rotationYaw = (float) (((MathHelper.floor_double(player.rotationYaw * 4 / 360 + 0.5) & 3) - 1) * 90);

			if (!world.getCollidingBoundingBoxes(obsidianBoat, obsidianBoat.getEntityBoundingBox().expand(-0.1, -0.1, -0.1)).isEmpty())
				return itemstack;
			if (!world.isRemote)
				world.spawnEntityInWorld(obsidianBoat);
			if (!player.capabilities.isCreativeMode)
				--itemstack.stackSize;
			player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
		}
		return itemstack;
	}
}
