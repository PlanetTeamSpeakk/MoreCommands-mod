package com.ptsmods.morecommands.commands.server.unelevated;

import com.mojang.brigadier.CommandDispatcher;
import com.ptsmods.morecommands.miscellaneous.Command;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

public class ToggleCheatsCommand extends Command {
	@Override
	public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literalReq("togglecheats").executes(ctx -> {
			MinecraftServer server = ctx.getSource().getServer();
			if (server.isSinglePlayer()) {
				server.getPlayerManager().setCheatsAllowed(!server.getPlayerManager().areCheatsAllowed());
				sendMsg(ctx, "Cheats are now " + formatFromBool(server.getPlayerManager().areCheatsAllowed(), "allowed", "disallowed") + DF + ".");
				return 1;
			} else sendError(ctx, "This command can only be used on singleplayer.");
			return 0;
		}));
	}
}
