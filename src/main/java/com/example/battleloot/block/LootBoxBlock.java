package com.example.battleloot.block;

import com.example.battleloot.ModItems;
import com.example.battleloot.menu.LootConfigMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class LootBoxBlock extends BaseEntityBlock {
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");

    public LootBoxBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(OPENED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPENED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LootBoxBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof LootBoxBlockEntity box) || !(player instanceof ServerPlayer sp)) {
            return InteractionResult.PASS;
        }

        boolean holdsWand = player.getMainHandItem().is(ModItems.CONFIG_WAND.get())
                || player.getOffhandItem().is(ModItems.CONFIG_WAND.get());

        // --- Konfigurations-GUI ---
        if (holdsWand) {
            if (!player.hasPermissions(2)) {
                sp.displayClientMessage(Component.translatable("msg.battleloot.no_permission"), true);
                return InteractionResult.CONSUME;
            }
            NetworkHooks.openScreen(sp,
                    new SimpleMenuProvider((id, inv, p) -> new LootConfigMenu(id, inv, box),
                            Component.translatable("gui.battleloot.title")),
                    pos);
            return InteractionResult.CONSUME;
        }

        // --- Normales Oeffnen ---
        if (player.isSpectator()) {
            return InteractionResult.PASS;
        }
        if (state.getValue(OPENED)) {
            sp.displayClientMessage(Component.translatable("msg.battleloot.already_opened"), true);
            return InteractionResult.CONSUME;
        }
        if (!box.giveLoot(sp)) {
            sp.displayClientMessage(Component.translatable("msg.battleloot.empty"), true);
            return InteractionResult.CONSUME;
        }

        level.playSound(null, pos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 0.8F, 1.1F);
        if (box.getMode() == 1) {
            level.setBlock(pos, state.setValue(OPENED, true), 3);
            level.scheduleTick(pos, this, box.getRefillSeconds() * 20);
        } else {
            level.destroyBlock(pos, false);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(OPENED)) {
            level.setBlock(pos, state.setValue(OPENED, false), 3);
            level.playSound(null, pos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.7F, 1.0F);
        }
    }
}
