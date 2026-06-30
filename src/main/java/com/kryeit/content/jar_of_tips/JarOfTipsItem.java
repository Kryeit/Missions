package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlocks;
import com.kryeit.registry.ModEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class JarOfTipsItem extends BlockItem {

    public static JarOfTipsItem empty(Properties properties) {
        return new JarOfTipsItem(ModBlocks.JAR_OF_TIPS.get(), properties);
    }

    public JarOfTipsItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static void initInventory(ItemStack stack, NonNullList<ItemStack> inventory) {
        boolean allEmpty = inventory.stream().allMatch(ItemStack::isEmpty);
        if (allEmpty) return;
        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(inventory));
    }

    public static NonNullList<ItemStack> getInventory(ItemStack stack) {
        NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        contents.copyInto(inventory);
        return inventory;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);

        if (!level.isClientSide) {
            JarOfTipsProjectile projectile = new JarOfTipsProjectile(ModEntityTypes.JAR_OF_TIPS_PROJECTILE.get(), level);
            ItemStack stack = player.getItemInHand(hand);

            projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            projectile.setOwner(player);
            projectile.setInventory(getInventory(stack));
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.5F, 0.3F);
            level.addFreshEntity(projectile);
            player.getCooldowns().addCooldown(this, 3);

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, player.getSoundSource(), 1.0F, 1.0F);
            stack.shrink(1);
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        NonNullList<ItemStack> inventory = getInventory(stack);
        int displayedItems = 0;
        for (ItemStack item : inventory) {
            if (item.isEmpty()) continue;
            if (displayedItems < 5) {
                tooltip.add(Component.literal(item.getHoverName().getString() + " x " + item.getCount()).withStyle(ChatFormatting.WHITE));
                displayedItems++;
            } else {
                tooltip.add(Component.literal("...").withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
                break;
            }
        }
    }
}
