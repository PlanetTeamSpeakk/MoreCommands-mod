package com.ptsmods.morecommands.mixin.common;

import com.ptsmods.morecommands.MoreCommands;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerCommandSource.class)
public abstract class MixinServerCommandSource {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/ServerCommandSource;sendToOps(Lnet/minecraft/text/Text;)V"), method = "sendFeedback")
	private void sendFeedback_sendToOps(ServerCommandSource source, Text message) {
		if (source.getWorld() == null || source.getWorld().getGameRules().getBoolean(MoreCommands.sendCommandFeedbackToOpsRule)) sendToOps(new LiteralText(MoreCommands.textToString(message, null, false)));
	}

	@Shadow private void sendToOps(Text message) {}
}
