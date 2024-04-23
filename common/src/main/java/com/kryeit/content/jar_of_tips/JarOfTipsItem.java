package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlocks;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.content.equipment.potatoCannon.PotatoProjectileEntity;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class JarOfTipsItem extends BlockItem {

    public NonNullList<ItemStack> inventory;

    public static JarOfTipsItem empty(Properties properties) {
        return new JarOfTipsItem(ModBlocks.JAR_OF_TIPS.get(), properties);
    }

    public JarOfTipsItem(Block block, Properties properties) {
        super(block, properties);

        this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }


    public static void initInventory(ItemStack itemStack, NonNullList<ItemStack> inventory) {
        if (inventory.equals(NonNullList.withSize(9, ItemStack.EMPTY))) {
            return;
        }

        CompoundTag nbt = itemStack.getOrCreateTag();
        if (!nbt.contains("Items")) {
            CompoundTag inventoryTag = new CompoundTag();
            ContainerHelper.saveAllItems(inventoryTag, inventory, true);
            nbt.put("Items", inventoryTag);
            itemStack.setTag(nbt);
        }
    }

    public static NonNullList<ItemStack> getInventory(ItemStack stack) {
        NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("Items")) {
            ContainerHelper.loadAllItems(nbt.getCompound("Items"), inventory);
        }
        return inventory;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);

        if (!level.isClientSide) {
            PotatoProjectileEntity projectile = AllEntityTypes.POTATO_PROJECTILE.create(level);
            projectile.setItem(this.getDefaultInstance());

            Vec3 handPos = ShootableGadgetItemMethods.getGunBarrelVec(player, hand == InteractionHand.MAIN_HAND,
                    new Vec3(0f, 0f, 0f));

            // Calculate the motion of the projectile
            double speed = 1.5; // Adjust this value as needed
            Vec3 splitMotion = player.getLookAngle().scale(speed);

            projectile.setPos(handPos.x, handPos.y, handPos.z);
            projectile.setDeltaMovement(splitMotion);
            projectile.setOwner(player);
            level.addFreshEntity(projectile);
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }
}
