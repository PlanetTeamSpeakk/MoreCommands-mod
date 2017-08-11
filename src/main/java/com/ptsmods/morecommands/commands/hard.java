package com.ptsmods.morecommands.commands;

import java.util.ArrayList;

import com.ptsmods.morecommands.miscellaneous.CommandType;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;

public class hard {

	public hard() {
	}

	public static class Commandhard extends com.ptsmods.morecommands.miscellaneous.CommandBase {

	    public int getRequiredPermissionLevel() {
	        return 2;
	    }

		public java.util.List getAliases() {
			ArrayList aliases = new ArrayList();
			return aliases;
		}

		public java.util.List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
			return new ArrayList();
		}

		public boolean isUsernameIndex(String[] string, int index) {
			return true;
		}

		public String getName() {
			return "hard";
		}

		public String getUsage(ICommandSender sender) {
			return "/hard Sets difficulty to hard.";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws NumberInvalidException, CommandException {
            server.setDifficultyForAllWorlds(EnumDifficulty.HARD);
            sender.sendMessage(new TextComponentString("The difficulty has been set to hard."));

		}
		
		@Override
		public CommandType getCommandType() {
			return CommandType.SERVER;
		}

	}

}