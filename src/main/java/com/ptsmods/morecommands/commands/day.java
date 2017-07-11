package com.ptsmods.morecommands.commands;

import java.util.ArrayList;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class day {

	public static Object instance;

	public day() {
	}

	public void preInit(FMLPreInitializationEvent event) {
	}

	public static class Commandday extends CommandBase {
		public boolean isUsernameIndex(int var1) {
			return false;
		}

	    public int getRequiredPermissionLevel() {
	        return 2;
	    }

		public java.util.List getAliases() {
			return new ArrayList();
		}

		public java.util.List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
			return new ArrayList();
		}

		public boolean isUsernameIndex(String[] string, int index) {
			return true;
		}

		public String getName() {
			return "day";
		}

		public String getUsage(ICommandSender var1) {
			return "/day Sets time to day.";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender var1, String[] cmd) {
			EntityPlayer entity = (EntityPlayer) var1;

			World world = null;
			WorldServer[] list = server.worlds;
			for (WorldServer ins : list) {
				if (ins.provider.getDimension() == entity.world.provider.getDimension())
					world = ins;
			}
			if (world == null)
				world = list[0];
			
			if (entity instanceof EntityPlayerMP) {
				MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
				if (minecraftserver != null) {
					world.setWorldTime(1000);
					var1.sendMessage(new TextComponentString("The time has been changed to 1000 ticks, aka day-time."));
				}
			}

		}

	}

}