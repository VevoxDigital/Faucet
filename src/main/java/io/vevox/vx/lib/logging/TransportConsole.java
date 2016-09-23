package io.vevox.vx.lib.logging;

import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Built-in transport for handing output to bukkit's console.
 *
 * @author Matthew Struble
 * @since 1.10.2-r0.1
 */
@SuppressWarnings("unused WeakerAccess")
public class TransportConsole implements Transport {

  public static final int BACKSPACE_CHARS = 17;
  public static final String BACKSPACES;

  static {
    String bs = "";
    for (int i = 0; i < BACKSPACE_CHARS; i++)
      bs += "\b";
    BACKSPACES = bs;
  }

  private final LoggingLevel level;
  private final String name;

  public TransportConsole(LoggingLevel level) {
    this("console", level);
  }

  public TransportConsole(String name, LoggingLevel level) {
    this.name = name;
    this.level = level;
  }

  @Override
  public LoggingLevel level() {
    return level;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public void receive(TransportedMessage msg) {
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    System.out.println(BACKSPACE_CHARS +
        format.format(new Date()) + '-' +
        msg.level.color() + msg.level.toString().toLowerCase() +
        ChatColor.RESET + ": " + msg.message);
  }

}
