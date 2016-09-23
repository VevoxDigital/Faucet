package io.vevox.vx.lib.cmd;

import org.bukkit.command.Command;

/**
 * Generic exception used in commands.
 *
 * @author Matthew Struble
 * @since 1.10.2-r0.1
 */
@SuppressWarnings("unused WeakerAccess")
public class CommandException extends Exception {

  public CommandException(String msg) {
    super(msg);
  }

  /**
   * Exception used when an argument is unknown.
   *
   * @author Matthew Struble
   * @since 1.10.2-r0.1
   */
  public static class UnknownArgumentException extends CommandException {
    UnknownArgumentException(int index, String arg) {
      super(String.format("Argument %d (\"%s\") is unknown", index + 1, arg));
    }
  }

  /**
   * Exception used when an argument is missing.
   *
   * @author Matthew Struble
   * @since 1.10.2-r0.1
   */
  public static class MissingArgumentException extends CommandException {
    MissingArgumentException(int index) {
      super(String.format("Argument %d is missing", index + 1));
    }
  }

  /**
   * Exception used when there are insufficient permissions to execute a command.
   *
   * @author Matthew Struble
   * @since 1.10.2-r0.1
   */
  public static class InsufficientPermissionsException extends CommandException {
    InsufficientPermissionsException(String permission) {
      super("Missing permission " + permission);
    }
  }

  /**
   * Exception used when a player-only command is executed by the console.
   *
   * @author Matthew Struble
   * @since 1.10.2-r0.1
   */
  public static class ConsoleExecutionException extends CommandException {
    ConsoleExecutionException() {
      super("Command must be executed by a player");
    }
  }

  /**
   * Exception used when a command is not handled by the delegator.
   *
   * @author Matthew Struble
   * @since 1.10.2-r0.1
   */
  public static class UnhandledCommandException extends CommandException {
    UnhandledCommandException(Command cmd) {
      super("Command " + cmd.getName() + " is not handled by this delegator");
    }
  }

}
