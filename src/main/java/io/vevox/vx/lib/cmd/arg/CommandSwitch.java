package io.vevox.vx.lib.cmd.arg;

import com.sun.istack.internal.NotNull;
import io.vevox.vx.lib.cmd.CommandDelegator;

/**
 * @author Matthew Struble
 */
@SuppressWarnings("unused WeakerAccess")
public abstract class CommandSwitch {

  /**
   * The name of this flag, used in the command.
   * <p>
   * The name should be all lower case and separated with dashes. The flag will
   * be prefaced with two dashes when the command is parsed.
   * <p>
   * If a flag name contains non-alphanumeric characters or any capitals, an
   * exception will be raised during flag registry.
   * <p>
   * Examples:
   * <pre>
   * <b>Good:</b> <code>toggle</code>
   * <b>Good:</b> <code>use-optional-config</code>
   * <b>Bad: </b> <code>UseOptionalConfig</code>
   * </pre>
   */
  public final String name;

  /**
   * A short single-char name of this flag. If the char is non-alphanumeric, no char will be used.
   * <br>
   * <b>Note: </b> "Alphanumeric" is considered to be <code>/^[0-9A-Z]$/i</code>.
   */
  public final char shortName;

  /**
   * Creates a new switch with the given name.
   * @param name The {@link #name} of this switch.
   */
  public CommandSwitch(@NotNull String name) {
    this(name, (char) 0);
  }

  /**
   * Creates a new switch with the given name and short name.
   * @param name The {@link #name} of this switch.
   * @param shortName The {@link #shortName} of this switch.
   */
  public CommandSwitch(@NotNull String name, char shortName) {
    if (!CommandDelegator.isValidCommandArg(name))
      throw new IllegalArgumentException("'" + name + "' is not valid command name");
    this.name = name;
    this.shortName = shortName;
  }

  /**
   * Gets the message displayed when reading the documentation for this command.
   *
   * @return The message.
   */
  public abstract String getDocsMessage();

  @Override
  public boolean equals(Object o) {
    return o instanceof CommandSwitch && ((CommandSwitch) o).name.equals(name);
  }

}
