package com.ptsmods.morecommands.miscellaneous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.ptsmods.morecommands.commands.fixTime.CommandfixTime;
import com.ptsmods.morecommands.commands.superPickaxe.CommandsuperPickaxe;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

public class Reference {
	public static final String MOD_ID = "morecommands";
	public static final String MOD_NAME = "MoreCommands";
	public static final String VERSION = "1.17.1";
	public static final String MC_VERSIONS = "[1.11,1.12]";
	public static final String UPDATE_URL = "https://raw.githubusercontent.com/PlanetTeamSpeakk/MoreCommands/master/version.json";
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }

	    return true;
	}
	
	public static boolean isBoolean(String s) {
		return s.toLowerCase().equals("true") || s.toLowerCase().equals("false");
	}
	
	public static String parseTime(int time, boolean isTimeRetarded) { // retarded time = 10AM and 10PM, non-retarded time = 10:00 and 22:00 
        int gameTime = time;
        int hours = gameTime / 1000 + 6;
        int minutes = (gameTime % 1000) * 60 / 1000;
        String ampm = "AM";
        if (isTimeRetarded) {
	        if (hours >= 12) {
	            hours -= 12; ampm = "PM"; 
	        }
	 
	        if (hours < 12) {
	            ampm = "AM";
	        }
	 
	        if (hours == 0) hours = 12;
        } else {
        	if (hours >= 24) hours -= 24;
        }
 
        String mm = "0" + minutes; 
        mm = mm.substring(mm.length() - 2, mm.length());
        
        if (isTimeRetarded) {
        	return hours + ":" + mm + " " + ampm;
        } else {
        	return hours + ":" + mm;
        }
    }
	
	public static void sendMessage(Object player, String message) {
		if (message == null) message = "null";
		try {
			((EntityPlayer) player).sendMessage(new TextComponentString(message));
		} catch (NullPointerException e) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
		} catch (ClassCastException e) { // occurs when trying to send a message to the console
			System.out.println(message);
		}
	}
	
	public static void sendServerMessage(MinecraftServer server, ICommandSender sender, String message) {
		for (int x = 0; x < server.getOnlinePlayerNames().length; x += 1) {
			try {
				EntityPlayer player = CommandBase.getPlayer(server, sender, server.getOnlinePlayerNames()[x]);
				sendMessage(player, message);
			} catch (CommandException e) {}
		}
	}
	
	public static FMLServerStartingEvent getServerStartingEvent() {
		return serverStartingEvent;
	}
	
	public static void setServerStartingEvent(FMLServerStartingEvent event) {
		serverStartingEvent = event;
	}
	
	public static void sendCommandUsage(Object player, String usage) {
		sendMessage(player, TextFormatting.RED + "Usage: " + usage);
	}
	
	public static void teleportSafely(EntityPlayer player) {
		World world = player.getEntityWorld();
		float x = player.getPosition().getX();
		float z = player.getPosition().getZ();
		boolean found = false;
		if (!world.isRemote) {
			while (!found) {
				for (Integer y = 256; y != player.getPosition().getY(); y -= 1) {
					Block block = world.getBlockState(new BlockPos(x, y-1, z)).getBlock();
					Block tpblock = world.getBlockState(new BlockPos(x, y, z)).getBlock();
					if (!getBlockBlacklist().contains(block) && getBlockWhitelist().contains(tpblock)) {
						player.setPositionAndUpdate(x+0.5, y, z+0.5);
						found = true;
						break;
					}
				}
				x -= 1;
				z -= 1;
			}
		}
	}
	
	public static String getLookDirectionFromLookVec(Vec3d lookvec) {
		String direction = "unknown";
		Integer x = (int) Math.round(lookvec.x);
		Integer y = (int) Math.round(lookvec.y);
		Integer z = (int) Math.round(lookvec.z);
		if (y == 1) {
			direction = "up";
		} else if (y == -1) {
			direction = "down";
		} else if (x == 0 && z == 1) {
			direction = "south";
		} else if (x == 0 && z == -1) {
			direction = "north";
		} else if (x == 1 && z == 0) {
			direction = "east";
		} else if (x == -1 && z == 0) {
			direction = "west";
		} else if (x == 1 && z == 1) {
			direction = "south-east";
		} else if (x == -1 && z == -1) {
			direction = "north-west";
		} else if (x == 1 && z == -1) {
			direction = "north-east";
		} else if (x == -1 && z == 1) {
			direction = "south-west";
		}
		return direction;
	}
	
	private static ArrayList<Block> blockBlacklist = new ArrayList<Block>();
	
	public static ArrayList getBlockBlacklist() {
		return blockBlacklist;
	}

	public static boolean addBlockToBlacklist(Block block) {
		return blockBlacklist.add(block);
	}
	
	public static void resetBlockBlackAndWhitelist() {
		blockBlacklist = new ArrayList();
		blockWhitelist = new ArrayList();
	}
	
	private static ArrayList<Block> blockWhitelist = new ArrayList<Block>();
	
	public static ArrayList getBlockWhitelist() {
		return blockWhitelist;
	}

	public static boolean addBlockToWhitelist(Block block) {
		return blockWhitelist.add(block);
	}
	
	@SideOnly(Side.CLIENT)
	public static void powerToolCommand(EntityPlayer player, EnumHand hand, Event event, Boolean isLeftClick) throws CommandException {
		ItemStack holding = player.getHeldItem(hand);
		if (holding.getItem() == Items.AIR) return;
		if (holding.hasTagCompound()) {
			NBTTagCompound nbt = holding.getTagCompound();
			if (nbt.hasKey("ptcmd")) {
				MinecraftServer server = Minecraft.getMinecraft().getIntegratedServer();
				EntityPlayer player1;
				try {
					player1 = CommandBase.getPlayer(server, (ICommandSender) player, player.getName());
				} catch (PlayerNotFoundException e) {
					return;
				}
				ICommandSender sender = (ICommandSender) player1;
				server = player1.getServer();
				if (!isLeftClick) {
					powerToolCounter += 1;
				} else if (powerToolCounter%2 != 0) {
					powerToolCounter += 1;
				}
				if (powerToolCounter%2 == 0 && player1.getUniqueID().equals(nbt.getUniqueId("ptowner"))) {
					try {event.setCanceled(true);} catch (UnsupportedOperationException e) {} catch (IllegalArgumentException e) {} // UnsupportedOperationException is for 1.12+, IllegalArgumentException for 1.11.2-
					server.getCommandManager().executeCommand(sender, nbt.getString("ptcmd"));
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void superPickaxeBreak(EntityPlayer player, EnumHand hand) throws CommandException {
		MinecraftServer server = Minecraft.getMinecraft().getIntegratedServer();
		EntityPlayer player2;
		try {
			player2 = CommandBase.getPlayer(server, (ICommandSender) player, player.getName());
		} catch (PlayerNotFoundException e) {
			return;
		} catch (NullPointerException e) {return;} // why do these even occur?
		server = player2.getServer();
		World world = server.getWorld(player2.dimension);
		BlockPos lookingAt = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
		if (CommandsuperPickaxe.enabled && player.getHeldItem(hand).getItem() instanceof ItemPickaxe) world.destroyBlock(lookingAt, true);
	}
	
	public static String getLocalizedName(Item item) {
		return item.getRegistryName().toString().split(":")[1].replaceAll("_", " ");
	}
	
	public static String evalJavaScript(String script) throws ScriptException {
		return evalCode(script, "nashorn");
	}
	
	public static String evalCode(String script, String language) throws ScriptException {
		return new ScriptEngineManager(null).getEngineByName(language).eval(script).toString();
	}
	
	public static TextFormatting getRandomColor(String... exceptions) {
		TextFormatting[] colors = {TextFormatting.AQUA, TextFormatting.BLACK, TextFormatting.BLUE, TextFormatting.DARK_AQUA, TextFormatting.DARK_BLUE, TextFormatting.DARK_GRAY, TextFormatting.DARK_GREEN,
				TextFormatting.DARK_PURPLE, TextFormatting.DARK_RED, TextFormatting.GOLD, TextFormatting.GRAY, TextFormatting.GREEN, TextFormatting.LIGHT_PURPLE, TextFormatting.RED, TextFormatting.WHITE,
				TextFormatting.YELLOW};
		TextFormatting color = colors[ThreadLocalRandom.current().nextInt(0, colors.length+1)];
		while (Arrays.asList(exceptions).contains(getColorName(color))) {
			color = colors[ThreadLocalRandom.current().nextInt(0, colors.length+1)];
		}
		
		return color;
		
	}
	
	public static String getColorName(TextFormatting color) {
		if (color == TextFormatting.AQUA) return "AQUA";
		else if (color == TextFormatting.BLACK) return "BLACK";
		else if (color == TextFormatting.BLUE) return "BLUE";
		else if (color == TextFormatting.BOLD) return "BOLD";
		else if (color == TextFormatting.DARK_AQUA) return "DARK_AQUA";
		else if (color == TextFormatting.DARK_BLUE) return "DARK_BLUE";
		else if (color == TextFormatting.DARK_GRAY) return "DARK_GRAY";
		else if (color == TextFormatting.DARK_GREEN) return "DARK_GREEN";
		else if (color == TextFormatting.DARK_PURPLE) return "DARK_PURPLE";
		else if (color == TextFormatting.DARK_RED) return "DARK_RED";
		else if (color == TextFormatting.GOLD) return "GOLD";
		else if (color == TextFormatting.GRAY) return "GRAY";
		else if (color == TextFormatting.GREEN) return "GREEN";
		else if (color == TextFormatting.ITALIC) return "ITALIC";
		else if (color == TextFormatting.LIGHT_PURPLE) return "LIGHT_PURPLE";
		else if (color == TextFormatting.OBFUSCATED) return "OBFUSCATED";
		else if (color == TextFormatting.RED) return "RED";
		else if (color == TextFormatting.RESET) return "RESET";
		else if (color == TextFormatting.STRIKETHROUGH) return "STRIKETHROUGH";
		else if (color == TextFormatting.UNDERLINE) return "UNDERLINE";
		else if (color == TextFormatting.WHITE) return "WHITE";
		else if (color == TextFormatting.YELLOW) return "YELLOW";
		else return "UNKNOWN";
	}
	
	public static TextFormatting getColorByName(String name) {
		if (name.toLowerCase().equals("aqua")) return TextFormatting.AQUA;
		else if (name.toLowerCase().equals("black")) return TextFormatting.BLACK;
		else if (name.toLowerCase().equals("blue")) return TextFormatting.BLUE;
		else if (name.toLowerCase().equals("bold")) return TextFormatting.BOLD;
		else if (name.toLowerCase().equals("dark_aqua")) return TextFormatting.DARK_AQUA;
		else if (name.toLowerCase().equals("dark_blue")) return TextFormatting.DARK_BLUE;
		else if (name.toLowerCase().equals("dark_gray")) return TextFormatting.DARK_GRAY;
		else if (name.toLowerCase().equals("dark_green")) return TextFormatting.DARK_GREEN;
		else if (name.toLowerCase().equals("dark_purple")) return TextFormatting.DARK_PURPLE;
		else if (name.toLowerCase().equals("dark_red")) return TextFormatting.DARK_RED;
		else if (name.toLowerCase().equals("gold")) return TextFormatting.GOLD;
		else if (name.toLowerCase().equals("gray")) return TextFormatting.GRAY;
		else if (name.toLowerCase().equals("green")) return TextFormatting.GREEN;
		else if (name.toLowerCase().equals("italic")) return TextFormatting.ITALIC;
		else if (name.toLowerCase().equals("light_purple")) return TextFormatting.LIGHT_PURPLE;
		else if (name.toLowerCase().equals("obfuscated")) return TextFormatting.OBFUSCATED;
		else if (name.toLowerCase().equals("red")) return TextFormatting.RED;
		else if (name.toLowerCase().equals("reset")) return TextFormatting.RESET;
		else if (name.toLowerCase().equals("strikethrough")) return TextFormatting.STRIKETHROUGH;
		else if (name.toLowerCase().equals("underline")) return TextFormatting.UNDERLINE;
		else if (name.toLowerCase().equals("white")) return TextFormatting.WHITE;
		else if (name.toLowerCase().equals("yellow")) return TextFormatting.YELLOW;
		else return TextFormatting.BLUE;
	}
	
	public static boolean isConsole(ICommandSender sender) {
		try {
			EntityPlayer player = (EntityPlayer) sender;
		} catch (ClassCastException e) {return true;}
		return false;
	}

	public static void setAllWorldTimes(MinecraftServer server, Integer time) {
		CommandfixTime.time = -1;
		new CommandfixTime().setAllWorldTimes(server, time);
	}
	
	public static void setWorldTime(World world, Integer time) {
		CommandfixTime.time = -1;
		world.setWorldTime(time);
	}
	
	public static String getArrayAsString(Object[] array) {
		String arrayString = "";
		for (int x = 0; x < array.length; x += 1) {
			arrayString += array[x].toString();
			if (x+1 == array.length);
			else if (x+2 != array.length) arrayString += ", ";
			else if (x+2 == array.length) arrayString += " and ";
		}
		return arrayString;
	}
	
	public static String getHTML(String url) throws IOException {
		StringBuilder result = new StringBuilder();
		java.net.URL URL = new java.net.URL(url);
		HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
		connection.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}
	
	public static String getServerStatus() throws IOException {
		String statuses = getHTML("https://status.mojang.com/check");
		String[] statusesArray = statuses.split(",");
		for (int x = 0; x < statusesArray.length; x += 1) {
			statusesArray[x] = statusesArray[x].substring(1, statusesArray[x].length()-1).replaceAll("\"", "");
		}
		HashMap<String, String> statusesMap = new HashMap<String, String>();
		for (int x = 0; x < statusesArray.length; x += 1) {
			statusesMap.put(statusesArray[x].split(":")[0], statusesArray[x].split(":")[1]);
		}
		String statusesMapString = statusesMap.toString();
		statusesMapString = statusesMapString.replaceAll("\\{", "");
		statusesMapString = statusesMapString.replaceAll("\\}", "");
		String[] statusesMapArray = statusesMapString.split(", ");
		String statusesFinal = "";
		for (int x = 0; x < statusesMapArray.length; x += 1) {
			statusesFinal += statusesMapArray[x].split("=")[0] + " = " + getColorByName(statusesMapArray[x].split("=")[1]) + statusesMapArray[x].split("=")[1] + TextFormatting.RESET + (x+1 != statusesMapArray.length ? "\n" : "");
		}
		return statusesFinal;
	}
	
	@Nullable
	public static String getUUIDFromName(String name) throws IOException {
		String data = getHTML("https://api.mojang.com/users/profiles/minecraft/" + name);
		if (data.split(",").length == 1) return null;
		else {
			String[] dataArray = data.substring(1, data.length()-1).split(",");
			HashMap<String, String> dataMap = new HashMap<String, String>();
			for (int x = 0; x < dataArray.length; x += 1) {
				dataMap.put(dataArray[x].split(":")[0].substring(1, dataArray[x].split(":")[0].length()-1), dataArray[x].split(":")[1].substring(1, dataArray[x].split(":")[1].length()-1));
			}
			if (dataMap.get("name") != null && dataMap.get("name").equals(name)) return dataMap.get("id");
			else return null;
		}
	}
	
	public static HashMap getPastNamesFromUUID(String UUID) throws IOException {
		String data = getHTML("https://api.mojang.com/user/profiles/" + UUID + "/names");
		String[] dataArray;
		try {
			dataArray = data.substring(1, data.length()-1).split("},");
		} catch (StringIndexOutOfBoundsException e) {
			return new HashMap<String, Long>();
		}
		String firstName = "";
		HashMap<String, Long> dataMap = new HashMap<String, Long>();
		for (int x = 0; x < dataArray.length; x += 1) {
			if (dataArray[x].split(",").length == 1) {firstName = dataArray[x].split(":")[1].substring(1, dataArray[x].split(":")[1].length()-1); dataMap.put(firstName, null);}
			else {
				String name = dataArray[x].split(",")[0].split(":")[1];
				name = name.substring(1, name.length()-1);
				Long changedAt = Long.parseLong(dataArray[x].split(",")[1].split(":")[1].substring(0, dataArray[x].split(",")[1].split(":")[1].length()-1));
				dataMap.put(name, changedAt);
			}
		}
		return dataMap;
	}
	
	public static void sitOnStairs(RightClickBlock event, EntityPlayer player, BlockPos pos, @Nullable MinecraftServer server) throws CommandException {
		World world = player.getEntityWorld();
		Block block = world.getBlockState(pos).getBlock();
		if (server == null) {
			EntityPlayer player1;
			try {
				player1 = CommandBase.getPlayer(server, (ICommandSender) player, player.getName());
			} catch (PlayerNotFoundException e) {
				return;
			} catch (NullPointerException e) {return;}
			event.setCanceled(true);
			server = player1.getServer();
			world = player1.getEntityWorld();
			player = player1;
		}
		if (block instanceof BlockStairs) {
			event.setCanceled(true);
			NBTTagCompound nbt = new NBTTagCompound();
			try {
				nbt = JsonToNBT.getTagFromJson("{id:\"minecraft:arrow\",NoGravity:1b,pickup:0}");
			} catch (NBTException e) {
				e.printStackTrace();
				return;
			}
			double d0 = pos.getX() + 0.5;
			double d1 = pos.getY();
			double d2 = pos.getZ() + 0.5;
			Entity entity = AnvilChunkLoader.readWorldEntityPos(nbt, world, d0, d1, d2, true);
			entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
			entity.setInvisible(true); // for some reason this function exists but doesn't work, I'll just leave it be.
			player.startRiding(entity);
			isSittingOnChair = true;
			arrow = entity;
			Reference.player = player;
		}
	}
	
	public static void dismountStairs() {
		if (arrow != null && isSittingOnChair) {
			player.dismountRidingEntity();
			arrow.onKillCommand();
			isSittingOnChair = false;
		}
	}
	
	public static Entity arrow = null;
	public static boolean isSittingOnChair = false;
	public static EntityPlayer player = null;
	private static FMLServerStartingEvent serverStartingEvent = null;
	private static int powerToolCounter = 0; // every event gets called twice except for leftclickempty.
	public static HashMap<String, String> tpRequests = new HashMap<String, String>();
	
}
