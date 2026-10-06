package com.eric.luciernagas.entity;

import com.eric.luciernagas.registry.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class FireflyEntity extends TamableAnimal {
    private static final EntityDataAccessor<Integer> COLOR =
            SynchedEntityData.defineId(FireflyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(FireflyEntity.class, EntityDataSerializers.BOOLEAN);

    public static final int BLUE = 0, YELLOW = 1, GREEN = 2, PINK = 3;

    public FireflyEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.setNoGravity(false);
        if (!level.isClientSide) this.setColor(level.random.nextInt(4));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.15, 4.0f, 12.0f, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLOR, YELLOW);
        this.entityData.define(SITTING, false);
    }

    public int getColor() { return this.entityData.get(COLOR); }
    public void setColor(int color) { this.entityData.set(COLOR, Math.max(0, Math.min(3, color))); }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.GLOW_BERRIES);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (this.isTame() && this.isOwnedBy(player)) {
            if (!this.level().isClientSide && stack.isEmpty()) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.playSound(net.minecraft.sounds.SoundEvents.ALLAY_ITEM_TAKEN, 0.6f, 1.6f);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (stack.is(Items.GLOW_BERRIES)) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!this.level().isClientSide) {
                if (this.random.nextFloat() < 0.35f) {
                    this.tame(player);
                    this.setOrderedToSit(false);
                    this.playSound(net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, 0.7f, 1.8f);
                    ((ServerLevel)this.level()).sendParticles(
                            net.minecraft.core.particles.ParticleTypes.END_ROD,
                            this.getX(), this.getY()+0.25, this.getZ(),
                            8, 0.2, 0.2, 0.2, 0.03
                    );
                } else {
                    this.playSound(net.minecraft.sounds.SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.7f, 1.2f);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) return false;
        return super.hurt(source, amount);
    }

    @Override
    protected void dropCustomDeathLoot(net.minecraft.world.damagesource.DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        if (this.random.nextFloat() < 0.45f) {
            this.spawnAtLocation(new ItemStack(ModItems.LUMINOUS_DUST.get(), 1 + this.random.nextInt(2)));
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isTame() && distanceToClosestPlayer > 48.0;
    }

    @Override
    public boolean isPersistenceRequired() {
        return this.isTame() || super.isPersistenceRequired();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("FireflyColor", this.getColor());
        tag.putBoolean("Sitting", this.isOrderedToSit());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setColor(tag.getInt("FireflyColor"));
        this.setOrderedToSit(tag.getBoolean("Sitting"));
    }

    public static boolean canSpawn(EntityType<FireflyEntity> type, ServerLevelAccessor level, MobSpawnType reason,
                                   net.minecraft.core.BlockPos pos, Random random) {
        if (level.getLevel().isDay()) return false;
        if (!level.getBlockState(pos.below()).isSolid()) return false;
        return level.getBlockState(pos).isAir() && level.getMaxLocalRawBrightness(pos) <= 7;
    }

    @Override
    public float getWalkTargetValue(net.minecraft.core.BlockPos pos, net.minecraft.world.level.LevelReader level) {
        return level.getBlockState(pos).is(Blocks.GRASS_BLOCK) ? 10.0f : 0.0f;
    }

    public float getGlowIntensity(float partialTick) {
        return 0.75f + 0.25f * (float)Math.sin((this.tickCount + partialTick) * 0.18f);
    }
}
