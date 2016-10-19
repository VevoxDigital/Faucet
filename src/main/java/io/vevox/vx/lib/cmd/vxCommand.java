package io.vevox.vx.lib.cmd;

import com.sun.istack.internal.NotNull;
import io.vevox.vx.lib.cmd.arg.CommandFlag;
import io.vevox.vx.lib.cmd.arg.CommandSwitch;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Matthew Struble
 */
@SuppressWarnings("unused WeakerAccess")
public abstract class vxCommand {

  public final Command command;

  private final Set<CommandFlag> flags;
  private final Set<CommandSwitch> switches;

  public vxCommand(@NotNull JavaPlugin plugin, @NotNull String cmd) {
    command = plugin.getCommand(cmd);

    flags = new HashSet<>();
    switches = new HashSet<>();
  }

  public vxCommand register(CommandFlag flag) throws IllegalArgumentException {
    if (hasName(flag.name) || hasChar(flag.shortName))
      throw new IllegalArgumentException("Flag name or short name already in use.");
    if (!flags.contains(flag))
      flags.add(flag);
    return this;
  }

  public vxCommand register(CommandSwitch s) throws IllegalArgumentException {
    if (hasName(s.name) || hasChar(s.shortName))
      throw new IllegalArgumentException("Switch name or short name already in use.");
    if (!switches.contains(s))
      switches.add(s);
    return this;
  }

  private boolean hasFlag(@NotNull String name) {
    return getFlag(name) != null;
  }

  private boolean hasFlag(char c) {
    return getFlag(c) != null;
  }

  private boolean hasSwitch(@NotNull String name) {
    return getSwitch(name) != null;
  }

  private boolean hasSwitch(char c) {
    return getSwitch(c) != null;
  }

  private boolean hasName(@NotNull String name) {
    return hasFlag(name) || hasSwitch(name);
  }

  private boolean hasChar(char c) {
    return CommandDelegator.isAlphanumeric(c, true, false)
        && (hasFlag(c) || hasSwitch(c));
  }


  private CommandFlag getFlag(String f) {
    for (CommandFlag flag : flags)
      if (flag.name.equals(f)) return flag;
    return null;
  }

  private CommandFlag getFlag(char c) {
    for (CommandFlag flag : flags)
      if (flag.shortName == c) return flag;
    return null;
  }

  private CommandSwitch getSwitch(String f) {
    for (CommandSwitch s : switches)
      if (s.name.equals(f)) return s;
    return null;
  }

  private CommandSwitch getSwitch(char c) {
    for (CommandSwitch s : switches)
      if (s.shortName == c) return s;
    return null;
  }

  private String getStringFrom(@NotNull String args, int index)
      throws CommandException.MissingArgumentException {
    try {
      String str = args.substring(index);
      while (str.charAt(0) == ' ') str = str.substring(1);
      if (str.charAt(0) == '-') throw new CommandException.MissingArgumentException(0);

      if (str.charAt(0) == '"') {
        Matcher m = Pattern.compile("(?<=[^\\\\])\"").matcher(str);
        if (!m.find(1)) return str.substring(1);
        return str.substring(1, m.start());
      } else {
        return str.split(" ")[0];
      }

    } catch (IndexOutOfBoundsException e) {
      throw new CommandException.MissingArgumentException(0);
    }
  }

  public CommandOptions parse(@NotNull String args) throws CommandException {

    Map<CommandFlag, String> flags = new HashMap<>();
    Set<CommandSwitch> switches = new HashSet<>();

    // Regex is spooky but magical, thanks StackOverflow @jens
    Pattern flagPattern =
        Pattern.compile(" -([^ ]+)(?=(?:[^\"\\\\]*(?:\\\\.|\"(?:[^\"\\\\]*\\\\.)*[^\"\\\\]*\"))*[^\"]*$)");
    Matcher m = flagPattern.matcher(args);

    int flagStart = -1;

    while (m.find()) {
      if (flagStart < 0)
        flagStart = m.start();

      String str = m.group(1);

      if (str.charAt(0) == '-') {
        // Long-named flag

        // TODO

      } else {
        // Short-named flag
        char[] chars;
        if (str.length() > 1)
          chars = str.toCharArray();
        else chars = new char[]{ str.charAt(0) };

        for (char c : chars) {
          if (!hasChar(c)) throw new CommandException("Unknown flag or switch: " + c);
          if (hasFlag(c)) {

            if (chars.length > 1) throw new CommandException("Flag used as switch: " + c);
            else try {
              CommandFlag flag = getFlag(c);
              String arg = getStringFrom(args, m.end());
              if (flags.containsKey(flag)) throw new CommandException("Duplicate flag " + flag.name);
              if (!flag.validator.test(arg)) throw new CommandException(flag.getValidationMessage());
              flags.put(flag, arg);
            } catch (CommandException.MissingArgumentException e) {
              throw new CommandException("Flag " + c + " requires argument");
            }

          } else {
            // hasSwitch(c)
            CommandSwitch s = getSwitch(c);
            if (!switches.contains(s)) switches.add(s);
          }

        }
      }

    }

    return new CommandOptions(
        (flagStart < 0 ? args : args.substring(0, flagStart)).split(" "),
        flags, switches);

  }

  public CommandOptions parse(String... args) throws CommandException {
    return parse(String.join(" ", (CharSequence[]) args));
  }

}
