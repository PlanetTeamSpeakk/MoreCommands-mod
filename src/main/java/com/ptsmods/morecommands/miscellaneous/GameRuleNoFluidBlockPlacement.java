package com.ptsmods.morecommands.miscellaneous;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules.ValueType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.BlockEvent;

public class GameRuleNoFluidBlockPlacement implements IGameRule<Boolean> {

	@Override
	public String getName() {
		return "noFluidBlockPlacement";
	}

	@Override
	public ValueType getType() {
		return ValueType.BOOLEAN_VALUE;
	}

	@Override
	public Boolean getDefaultValue() {
		return false;
	}

	@Override
	public void onUpdate(WorldServer world, Boolean oldValue, Boolean newValue) {}

	@Override
	public void onCreateWorld(MinecraftServer server, World world) {}

	@Override
	public void initWorld(MinecraftServer server, World world, Boolean value) {}

	@Override
	public void initServer(MinecraftServer server) {}

	public void onFluidBlockPlacement(BlockEvent.FluidPlaceBlockEvent event) {
		if (getValue(event.getWorld())) event.setCanceled(true);
	}

}