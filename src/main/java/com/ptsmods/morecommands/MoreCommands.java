package com.ptsmods.morecommands;

import java.io.File;
import java.nio.file.Files;

import com.ptsmods.morecommands.miscellaneous.CommandType;
import com.ptsmods.morecommands.miscellaneous.IncorrectCommandType;
import com.ptsmods.morecommands.miscellaneous.Reference;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.reflect.io.Path;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.VERSION, acceptedMinecraftVersions=Reference.MC_VERSIONS, updateJSON=Reference.UPDATE_URL)
public class MoreCommands {
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		try {
			Reference.resetCommandRegistry(CommandType.SERVER); // for when you relog and a new command has been added, only needed for development.
			Reference.resetCommandRegistry(CommandType.CLIENT);
			Initialize.setupCommandRegistry();
		} catch (IncorrectCommandType e) {
			e.printStackTrace();
		}
		if (this.shouldRegisterCommands) Initialize.registerCommands(event);
		Initialize.setupBlockLists();
		Reference.setServerStartingEvent(event);
	}
	
	@EventHandler
	@SideOnly(Side.CLIENT)
	public void postInit(FMLPostInitializationEvent event) {
		Initialize.setupCommandRegistry();
    	if (this.shouldRegisterCommands) Initialize.registerClientCommands();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if (!new File("mods/javassist.jar").exists()) {
			System.out.println("Could not find javassist.jar file, downloading it now...");
			boolean downloaded = Reference.downloadFile("https://raw.githubusercontent.com/PlanetTeamSpeakk/MoreCommands/master/javassist.jar", "mods/javassist.jar");
			if (!downloaded) {
				System.err.println("Javassist.jar could not be downloaded, thus MoreCommands cannot be used.");
				this.shouldRegisterCommands = false;
			} else {
				System.out.println("Successfully download javassist.jar.");
			}
		}
		
		if (!new File("mods/reflections.jar").exists()) {
			System.out.println("Could not find reflections.jar file, download it now...");
			boolean downloaded = Reference.downloadFile("https://raw.githubusercontent.com/PlanetTeamSpeakk/MoreCommands/master/reflections.jar", "mods/reflections.jar");
			if (!downloaded) {
				System.err.println("Reflections.jar could not be downloaded, thus MoreCommands cannot be used.");
				this.shouldRegisterCommands = false;
			} else {
				System.out.println("Successfully download reflections.jar.");
			}
		}
		
	}
	
	private boolean shouldRegisterCommands = true;
	
}
