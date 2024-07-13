package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlocks;
import com.kryeit.registry.ModEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class JarOfTipsItem extends BlockItem {

    public static JarOfTipsItem empty(Properties properties) {
        return new JarOfTipsItem(ModBlocks.JAR_OF_TIPS.get(), properties);
    }

    public JarOfTipsItem(Block block, Properties properties) {
        super(block, properties);

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
            Entity entity = ModEntityTypes.JAR_OF_TIPS_PROJECTILE.create(level);

            if (entity instanceof JarOfTipsProjectile projectile) {
                ItemStack stack = player.getItemInHand(hand);

                projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                projectile.setOwner(player);
                projectile.setInventory(getInventory(stack));

                float pitch = player.getXRot();
                float yaw = player.getYRot();
                projectile.shootFromRotation(player, pitch, yaw, 0.0F, 0.5F, 0.3F);
                level.addFreshEntity(projectile);
                player.getCooldowns().addCooldown(this, 14);

                stack.shrink(1);
            }

        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        NonNullList<ItemStack> inventory = getInventory(stack);
        if (!inventory.isEmpty()) {

            int displayedItems = 0;
            for (ItemStack item : inventory) {
                if (!item.isEmpty()) {
                    if (displayedItems < 5) {
                        tooltip.add(Component.literal(item.getItem().getName(item).getString() + " x " + item.getCount()).withStyle(ChatFormatting.WHITE));
                        displayedItems++;
                    } else {
                        tooltip.add(Component.literal("and " + (inventory.size() - displayedItems) + " more...").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
                        break;
                    }
                }
            }
        }
    }
}
