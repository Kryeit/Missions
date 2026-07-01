package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class JarOfTipsFallingBlockEntity extends FallingBlockEntity {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);

    public JarOfTipsFallingBlockEntity(EntityType<? extends FallingBlockEntity> type, Level level) {
        super(type, level);
    }

    private JarOfTipsFallingBlockEntity(Level level, double d, double e, double f, BlockState blockState) {
        this(ModEntityTypes.JAR_OF_TIPS_FALLING_BLOCK.get(), level);
        // Set the vanilla FallingBlockEntity blockState field (otherwise it defaults to sand).
        ((FallingBlockEntityHelper) this).missions$setBlockState(blockState);
        this.blocksBuilding = true;
        this.setPos(d, e, f);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = d;
        this.yo = e;
        this.zo = f;
        this.setStartPos(this.blockPosition());

        inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    public static FallingBlockEntity fall(Level level, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        JarOfTipsFallingBlockEntity fallingBlockEntity = new JarOfTipsFallingBlockEntity(level, (double) blockPos.getX() + 0.5, (double) blockPos.getY(), (double) blockPos.getZ() + 0.5, blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState);
        if (blockEntity instanceof JarOfTipsBlockEntity jar) {
            fallingBlockEntity.setInventory(jar.inventory);
        }

        level.setBlock(blockPos, blockState.getFluidState().createLegacyBlock(), 3);

        level.addFreshEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        CompoundTag inventoryTag = new CompoundTag();
        ContainerHelper.saveAllItems(inventoryTag, inventory, true, this.registryAccess());
        compound.put("Items", inventoryTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound.getCompound("Items"), this.inventory, this.registryAccess());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity, Block.getId(this.getBlockState()));
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }
}
