package io.vevox.vx.lib.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Matthew Struble
 */
public interface CommandDelegator extends CommandExecutor {

  @Override
  default boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
    try {
      command(sender, cmd, args);
    } catch (CommandException.UnhandledCommandException e) {
      return false;
    } catch (CommandException e) {
      sender.sendMessage(String.format("%s%s%s: %s", ChatColor.RED,
          e.getClass().getSimpleName(), ChatColor.RESET, e.getMessage()));
    }
    return true;
  }

  /**
   * Command delegation method exeucted when a command assigned to this delegator is
   * executed by a valid {@link CommandSender} and the event is not canceled.
   *
   * @param sender The sender of the command.
   * @param cmd    The command that was executed.
   * @param args   The arguments of the command.
   *
   * @throws CommandException.UnhandledCommandException If a command was passed to this delegator
   *                                                                      and should not have been delegated here.
   * @throws CommandException                                             If any issues arise during command handling.
   */
  void command(CommandSender sender, Command cmd, String... args) throws CommandException;

}
