package io.vevox.vx.lib.cmd.arg;

import com.sun.istack.internal.NotNull;
import io.vevox.vx.lib.cmd.CommandDelegator;

import java.util.function.Predicate;

/**
 * A command flag is any portion of the command that provides additional
 * data from the command sender. This is different from a {@link CommandSwitch} in that
 * a flag accepts a parameter that will be handed to the command.
 * <p>
 * <pre>
 *    /command arg1 arg2      -r       -t 6s
 *       ^     ^     ^         ^          ^
 *    The Command & args   A switch    A flag
 * </pre>
 *
 * @author Matthew Struble
 * @since 0.2.0-m1.10.2
 */
@SuppressWarnings("unused WeakerAccess")
public abstract class CommandFlag {

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
   * A validator {@link Predicate} for strings passed to this flag.
   */
  public final Predicate<String> validator;

  /**
   * Creates a new command flag with the given name and validator, using no short name.
   *
   * @param name      The {@link #name} of the command flag.
   * @param validator The {@link #validator} to use.
   */
  public CommandFlag(@NotNull String name, Predicate<String> validator) {
    this(name, (char) 0, validator);
  }

  /**
   * Creates a new command flag with the given name, short name, and validator.
   *
   * @param name      The {@link #name} of this flag.
   * @param shortName The {@link #shortName} of this flag.
   * @param validator The {@link #validator} of this flag.
   */
  public CommandFlag(@NotNull String name, char shortName, Predicate<String> validator) {
    if (!CommandDelegator.isValidCommandArg(name))
      throw new IllegalArgumentException("'" + name + "' is not valid command name");
    this.name = name;
    this.shortName = shortName;
    this.validator = validator;
  }

  /**
   * Gets the message played when a input does not pass validation.
   *
   * @return The message.
   */
  public abstract String getValidationMessage();

  /**
   * Gets the message displayed when reading the documentation for this command.
   *
   * @return The message.
   */
  public abstract String getDocsMessage();

  @Override
  public boolean equals(Object o) {
    return o instanceof CommandFlag && ((CommandFlag) o).name.equals(name);
  }

}
