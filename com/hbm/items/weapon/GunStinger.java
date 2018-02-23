package com.hbm.items.weapon;

import java.util.List;

import com.google.common.collect.Multimap;
import com.hbm.entity.projectile.EntityRocket;
import com.hbm.entity.projectile.EntityRocketHoming;
import com.hbm.items.ModItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class GunStinger extends Item {

	public GunStinger()
    {
        this.maxStackSize = 1;
        this.setMaxDamage(500);
    }
	
	@Override
	public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, EntityPlayer p_77615_3_, int p_77615_4_) {
		int j = this.getMaxItemUseDuration(p_77615_1_) - p_77615_4_;

		ArrowLooseEvent event = new ArrowLooseEvent(p_77615_3_, p_77615_1_, j);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return;
		}
		j = event.charge;

		boolean flag = p_77615_3_.capabilities.isCreativeMode
				|| EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, p_77615_1_) > 0;

		if (flag || p_77615_3_.inventory.hasItem(ModItems.gun_stinger_ammo)) {
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if (j < 25.0D) {
				return;
			}

			if (j > 25.0F) {
				f = 25.0F;
			}

			EntityRocketHoming entityarrow = new EntityRocketHoming(p_77615_2_, p_77615_3_, 1.0F);

			if (f == 1.0F) {
				entityarrow.setIsCritical(true);
			}

			p_77615_1_.damageItem(1, p_77615_3_);
			p_77615_2_.playSoundAtEntity(p_77615_3_, "hbm:weapon.rpgShoot", 1.0F, 0.25F);

			if (flag) {
				entityarrow.canBePickedUp = 2;
			} else {
				p_77615_3_.inventory.consumeInventoryItem(ModItems.gun_stinger_ammo);
			}

			if (!p_77615_2_.isRemote) {
				entityarrow.canBePickedUp = 2;
				p_77615_2_.spawnEntityInWorld(entityarrow);
			}
		}
	}

	@Override
	public ItemStack onEaten(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_) {
		return p_77654_1_;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.bow;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		ArrowNockEvent event = new ArrowNockEvent(p_77659_3_, p_77659_1_);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return event.result;
		}

		if (p_77659_3_.capabilities.isCreativeMode || p_77659_3_.inventory.hasItem(ModItems.gun_stinger_ammo)) {
			p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
		}

		return p_77659_1_;
	}

	@Override
	public int getItemEnchantability() {
		return 1;
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean bool) {

		list.add("Kaboom!");
		list.add("");
		list.add("Ammo: Rockets");
		list.add("Projectiles explode on impact.");
	}

	@Override
	public Multimap getItemAttributeModifiers() {
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(),
				new AttributeModifier(field_111210_e, "Weapon modifier", 4, 0));
		return multimap;
	}
}
