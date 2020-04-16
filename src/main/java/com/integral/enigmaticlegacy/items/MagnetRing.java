package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Vector3;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class MagnetRing extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();

	public MagnetRing(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		MagnetRing.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		MagnetRing.integratedProperties.maxStackSize(1);
		MagnetRing.integratedProperties.rarity(Rarity.RARE);

		return MagnetRing.integratedProperties;

	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MAGNET_RING_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magnetRing1", ConfigHandler.MAGNET_RING_RANGE.getValue());
			LoreHelper.addLocalizedString(list, ConfigHandler.INVERT_MAGNETS_SHIFT.getValue() ? "tooltip.enigmaticlegacy.magnetRing2_alt" : "tooltip.enigmaticlegacy.magnetRing2");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// Insert existential void here
	}

	@Override
	public void onEquipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.magnetRing))
			return false;
		else
			return true;
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {
		if (ConfigHandler.INVERT_MAGNETS_SHIFT.getValue() ? !living.isShiftKeyDown() : living.isShiftKeyDown() || !(living instanceof PlayerEntity))
			return;

		double x = living.getPosX();
		double y = living.getPosY() + 0.75;
		double z = living.getPosZ();

		List<ItemEntity> items = living.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - ConfigHandler.MAGNET_RING_RANGE.getValue(), y - ConfigHandler.MAGNET_RING_RANGE.getValue(), z - ConfigHandler.MAGNET_RING_RANGE.getValue(), x + ConfigHandler.MAGNET_RING_RANGE.getValue(), y + ConfigHandler.MAGNET_RING_RANGE.getValue(), z + ConfigHandler.MAGNET_RING_RANGE.getValue()));
		int pulled = 0;
		for (ItemEntity item : items)
			if (this.canPullItem(item)) {
				if (pulled > 200)
					break;

				if (!SuperpositionHandler.canPickStack((PlayerEntity) living, item.getItem()))
					continue;

				SuperpositionHandler.setEntityMotionFromVector(item, new Vector3(x, y, z), 0.45F);
				item.setNoPickupDelay();

				for (int counter = 0; counter <= 2; counter++)
					living.world.addParticle(ParticleTypes.WITCH, item.getPosX(), item.getPosY() - item.getYOffset() + item.getHeight() / 2, item.getPosZ(), (Math.random() - 0.5D) * 0.1D, (Math.random() - 0.5D) * 0.1D, (Math.random() - 0.5D) * 0.1D);
				pulled++;
			}

	}

	protected boolean canPullItem(ItemEntity item) {
		ItemStack stack = item.getItem();
		if (!item.isAlive() || stack.isEmpty() || item.getPersistentData().getBoolean("PreventRemoteMovement"))
			return false;

		return true;
	}

}
