package io.vevox.vx.lib.cmd;

import com.sun.istack.internal.NotNull;
import io.vevox.vx.lib.cmd.arg.CommandFlag;
import io.vevox.vx.lib.cmd.arg.CommandSwitch;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.function.Predicate;

/**
 * @author Matthew Struble
 */
@SuppressWarnings("unused")
public interface CommandDelegator extends CommandExecutor {

  /**
   * Validates a command argument, ensuring it is all lower case, numbers, or dashes and that
   * the string does not begin with a dash.
   * @param name The name.
   * @return If the name is valid.
   */
  static boolean isValidCommandArg(@NotNull String name) {
    if (name.length() == 0) return false;
    if (name.charAt(0) == '-') return false;
    for (char c : name.toCharArray())
      if (!( ('0' <= c && c <= '9') || ('a' <= c && c <= 'z') || c == '-' )) return false;
    return true;
  }

  static CommandFlag createFlag(String name, Predicate<String> validator,
                                String docsMsg, String validationMsg) {
    return new CommandFlag(name, validator) {
      @Override
      public String getValidationMessage() {
        return validationMsg;
      }

      @Override
      public String getDocsMessage() {
        return docsMsg;
      }
    };
  }

  static CommandFlag createFlag(String name, char shortName,
                                Predicate<String> validator, String docsMsg, String validationMsg) {
    return new CommandFlag(name, shortName, validator) {
      @Override
      public String getValidationMessage() {
        return validationMsg;
      }

      @Override
      public String getDocsMessage() {
        return docsMsg;
      }
    };
  }

  static CommandSwitch createSwitch(String name, String docsMsg) {
    return new CommandSwitch(name) {
      @Override
      public String getDocsMessage() {
        return docsMsg;
      }
    };
  }

  static CommandSwitch createSwitch(String name, char shortName, String docsMsg) {
    return new CommandSwitch(name, shortName) {
      @Override
      public String getDocsMessage() {
        return docsMsg;
      }
    };
  }

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
