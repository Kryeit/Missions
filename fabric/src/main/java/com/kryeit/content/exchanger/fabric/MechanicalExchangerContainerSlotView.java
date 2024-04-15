package com.kryeit.content.exchanger.fabric;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.item.ItemStack;

public class MechanicalExchangerContainerSlotView extends SnapshotParticipant<ItemStack> implements StorageView<ItemVariant> {

	protected MechanicalExchangerContainerInterface inventory;
	protected int slot;

	public MechanicalExchangerContainerSlotView(MechanicalExchangerContainerInterface inventory, int slot) {
		this.inventory = inventory;
		this.slot = slot;
	}

	@Override
	public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
		if (resource.matches(inventory.getStack(0))) return 0;
		return this.inventory.extract(resource, maxAmount, transaction);
	}

	@Override public boolean isResourceBlank() { return this.getResource().isBlank(); }
	@Override public ItemVariant getResource() { return ItemVariant.of(this.getRawStack()); }

	@Override public long getAmount() { return this.getRawStack().getCount(); }

	@Override public long getCapacity() { return this.inventory.getCapacityForSlot(slot); }

	@Override protected ItemStack createSnapshot() { return this.getRawStack().copy(); }

	public ItemStack getRawStack() { return this.inventory.getStack(slot); }

	@Override protected void readSnapshot(ItemStack snapshot) { this.inventory.restoreViewSnapshot(slot, snapshot); }

}
