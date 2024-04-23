package com.kryeit.content.jar_of_tips;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
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
    
    private BlockState blockState;

    private NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);

    public JarOfTipsFallingBlockEntity(EntityType<? extends FallingBlockEntity> type, Level level) {
        super(type, level);

    }

    private JarOfTipsFallingBlockEntity(Level level, double d, double e, double f, BlockState blockState) {
        this(EntityType.FALLING_BLOCK, level);
        this.blockState = blockState;
        this.blocksBuilding = true;
        this.setPos(d, e, f);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = d;
        this.yo = e;
        this.zo = f;
        this.setStartPos(this.blockPosition());

        inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();


        FallingBlockEntityHelper helper = (FallingBlockEntityHelper) this;

        if (helper.missions$getBlockState().equals(blockState)) {
            return;
        }

        helper.missions$setBlockState(blockState);
    }

    public static FallingBlockEntity fall(Level level, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        JarOfTipsFallingBlockEntity fallingBlockEntity = new JarOfTipsFallingBlockEntity(level, (double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5, blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? (BlockState)blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState);
        fallingBlockEntity.setInventory(((JarOfTipsBlockEntity) blockEntity).inventory);

        level.setBlock(blockPos, blockState.getFluidState().createLegacyBlock(), 3);

        level.addFreshEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        CompoundTag inventoryTag = new CompoundTag();
        ContainerHelper.saveAllItems(inventoryTag, inventory, true);
        compound.put("Items", inventoryTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound.getCompound("Items"), this.inventory);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, Block.getId(blockState));
    }

    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
    }


}
