package co.mcsp.vxlib.logging;

import org.bukkit.ChatColor;

/**
 * When logging, a specified logging level should be used to determine the
 * importance of the output line.
 * <p>
 * Logging levels are, in order from least to most important,
 * {@link #SILLY}, {@link #DEBUG}, {@link #VERBOSE},
 * {@link #INFO}, {@link #WARNING}, and {@link #ERROR}
 *
 * @author Matthew Struble
 */
@SuppressWarnings("unused")
public enum LoggingLevel {

  /**
   * Lowest logging level. Used only for trivial output.
   */
  SILLY,

  /**
   * Used to for debugging output.
   */
  DEBUG,

  /**
   * Extra output that may not be considered "necessary."
   */
  VERBOSE,

  /**
   * General information.
   */
  INFO,

  /**
   * A recoverable or minor issue.
   */
  WARNING,

  /**
   * A major and often fatal issue.
   */
  ERROR;

  /**
   * Gets the color of the specified error level.
   * @return The color.
   */
  public ChatColor color() {
    switch (this) {
      case SILLY:
      case VERBOSE:
      case INFO:
        return ChatColor.WHITE;
      case DEBUG:
        return ChatColor.LIGHT_PURPLE;
      case WARNING:
        return ChatColor.YELLOW;
      case ERROR:
        return ChatColor.RED;
    }
    return ChatColor.RESET;
  }

}
