package com.ptsmods.morecommands.commands;

import java.util.ArrayList;

import com.ptsmods.morecommands.miscellaneous.CommandType;
import com.ptsmods.morecommands.miscellaneous.Reference;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class tpdeny {

	public tpdeny() {
	}

	public static class Commandtpdeny extends com.ptsmods.morecommands.miscellaneous.CommandBase {

	    public int getRequiredPermissionLevel() {
	        return 0;
	    }

		public java.util.List getAliases() {
			ArrayList aliases = new ArrayList();
			aliases.add("tpno");
			return aliases;
		}

		public java.util.List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
			return new ArrayList();
		}

		public String getName() {
			return "tpdeny";
		}

		public String getUsage(ICommandSender sender) {
			return usage;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			if (Reference.tpRequests.get(sender.getName()) == null) {
				Reference.sendMessage(sender, "You do not have a tpa request open.");
			} else {
				try {
					EntityPlayer requester = CommandBase.getPlayer(server, sender, Reference.tpRequests.get(sender.getName()));
					Reference.sendMessage(requester, sender.getName() + " has denied your tpa request.");
					Reference.tpRequests.remove(sender.getName());
					Reference.sendMessage(sender, "You have denied " + requester.getName() + "'s tpa request.");
				} catch (PlayerNotFoundException e) {
					Reference.sendMessage(sender, "Error getting the person who tried to teleport to you, are they offline?");
				}
			}

		}
		
		@Override
		public CommandType getCommandType() {
			return CommandType.SERVER;
		}
		
		protected String usage = "/tpdeny Deny a tpa request.";

		@Override
		public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
			return true;
		}

	}

}