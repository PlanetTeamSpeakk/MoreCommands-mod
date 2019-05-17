package com.ptsmods.morecommands.commands;

import java.io.IOException;
import java.util.ArrayList;

import com.ptsmods.morecommands.miscellaneous.CommandType;
import com.ptsmods.morecommands.miscellaneous.Permission;
import com.ptsmods.morecommands.miscellaneous.Reference;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class setWarp {

	public setWarp() {
	}

	public static class CommandsetHome extends com.ptsmods.morecommands.miscellaneous.CommandBase {

		@Override
		public int getRequiredPermissionLevel() {
			return 2;
		}

		@Override
		public java.util.List getAliases() {
			ArrayList aliases = new ArrayList();
			return aliases;
		}

		@Override
		public java.util.List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
			return new ArrayList();
		}

		@Override
		public String getName() {
			return "setwarp";
		}

		@Override
		public String getUsage(ICommandSender sender) {
			return usage;
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length != 0)
				try {
					Reference.addWarp(args[0], player.getPositionVector(), MathHelper.wrapDegrees(player.rotationYaw), MathHelper.wrapDegrees(player.rotationPitch));
					Reference.saveWarpsFile();
					Reference.sendMessage(player, "The warp has been set.");
				} catch (IOException e) {
					Reference.sendMessage(sender, "An unknown error occured while reading the file.");
					return;
				}
			else Reference.sendCommandUsage(sender, usage);
		}

		@Override
		public CommandType getCommandType() {
			return CommandType.SERVER;
		}

		@Override
		public Permission getPermission() {
			return new Permission(Reference.MOD_ID, "setwarp", "Permission to use the setwarp command.", true);
		}

		protected String usage = "/setwarp <name> Sets a warp.";

	}

}