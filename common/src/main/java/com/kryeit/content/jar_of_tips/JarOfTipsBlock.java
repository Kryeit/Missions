package com.kryeit.content.jar_of_tips;

import com.kryeit.registry.ModBlockEntities;
import com.kryeit.registry.ModItems;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class JarOfTipsBlock extends FallingBlock implements IBE<JarOfTipsBlockEntity>, SimpleWaterloggedBlock, Nameable {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);


    public JarOfTipsBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    /* FACING */

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    /* BLOCK ENTITY */
    @Override
    public SoundType getSoundType(BlockState blockState) {
        return SoundType.GLASS;
    }

    @Override
    public Class<JarOfTipsBlockEntity> getBlockEntityClass() {
        return JarOfTipsBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends JarOfTipsBlockEntity> getBlockEntityType() {
        return ModBlockEntities.JAR_OF_TIPS.get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new JarOfTipsBlockEntity(ModBlockEntities.JAR_OF_TIPS.get(), pos, state);
    }

    /* NAMEABLE */



    /* COMMON */
    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);

        if (level.isClientSide) return;
        if (!(blockEntity instanceof JarOfTipsBlockEntity jar)) {
            return;
        }

        if (!(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) > 0)) {
            jar.drops();
            return;
        }

        ItemStack jarItem = new ItemStack(ModItems.JAR_OF_TIPS.get());
        JarOfTipsItem.initInventory(jarItem, jar.inventory);
        popResource(level, blockPos, jarItem);
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player.isCreative()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof JarOfTipsBlockEntity jar) {
                if (!jar.isEmpty()) {
                    ItemStack jarItem = new ItemStack(ModItems.JAR_OF_TIPS.get());
                    JarOfTipsItem.initInventory(jarItem, jar.inventory);
                    popResource(world, pos, jarItem);
                }
            }
        }
        super.playerWillDestroy(world, pos, state, player);
    }
    
    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof JarOfTipsBlockEntity) {
                if (itemStack.hasTag() && itemStack.getTag().contains("Items")) {
                    CompoundTag itemsTag = itemStack.getTag().getCompound("Items");
                    NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(itemsTag, inventory);
                    ((JarOfTipsBlockEntity) be).setInventory(inventory);
                }
            }
        }
    }

    /* FALLING BLOCK */

    @Override
    public int getDustColor(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getMapColor(blockGetter, blockPos).col;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (isFree(serverLevel.getBlockState(blockPos.below())) && blockPos.getY() >= serverLevel.getMinBuildHeight()) {
            FallingBlockEntity fallingBlockEntity = JarOfTipsFallingBlockEntity.fall(serverLevel, blockPos, blockState);
            this.falling(fallingBlockEntity);
        }
    }

    @Override
    public void onLand(Level level, BlockPos blockPos, BlockState blockState, BlockState blockState2, FallingBlockEntity fallingBlockEntity) {
        super.onLand(level, blockPos, blockState, blockState2, fallingBlockEntity);

        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!(blockEntity instanceof JarOfTipsBlockEntity jar)) {
            return;
        }

        if (fallingBlockEntity instanceof JarOfTipsFallingBlockEntity fallingJar) {
            if ((fallingBlockEntity.getStartPos().getY() - fallingBlockEntity.getY()) > 3) {
                Containers.dropContents(level, blockPos, fallingJar.getInventory());
                level.removeBlock(blockPos, false);
                level.playSound(null, blockPos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

                level.getServer().execute(() -> {
                    BlockPos downPos = blockPos.below();
                    BlockState downState = level.getBlockState(downPos);
                    if (downState.getBlock() instanceof JarOfTipsBlock) {
                        JarOfTipsBlockEntity downJar = (JarOfTipsBlockEntity) level.getBlockEntity(downPos);
                        if (downJar != null) {
                            downJar.drops();
                        }
                        level.removeBlock(downPos, false);
                        level.playSound(null, downPos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                });

            } else {
                jar.setInventory(fallingJar.getInventory());
            }
        }
    }

    /* WATERLOGGED */

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if ((Boolean)blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return (Boolean)blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

}