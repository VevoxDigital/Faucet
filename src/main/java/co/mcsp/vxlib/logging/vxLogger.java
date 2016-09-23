package co.mcsp.vxlib.logging;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Struble
 */
@SuppressWarnings("unused WeakerAccess")
public class vxLogger {

  private Map<String, Transport> transports = new HashMap<>();

  public final String name;
  public final vxLogger parent;

  /**
   * Creates a new logging system with the given name and starting list of transports. This logger
   * will be created as a top-level orphan.
   *
   * @param name       The name of this logger.
   * @param transports An array of transports for the logger.
   *
   * @throws IllegalArgumentException If an argument is incorrect or invalid.
   * @since 1.10.2-r0.1
   */
  public vxLogger(String name, Transport... transports) throws IllegalArgumentException {
    this(name, null, transports);
  }

  /**
   * Creates a new logging system with the given name. This logger will be created as a top-level
   * orphan and posses no transports.
   *
   * @param name The name of the new logger.
   *
   * @throws IllegalArgumentException If an argument is incorrect or invalid.
   * @since 1.10.2-r0.1
   */
  public vxLogger(String name) throws IllegalArgumentException {
    this(name, null, new Transport[0]);
  }

  /**
   * Creates a new logging system with the given name and under the given parent logger. This logger
   * will be posses no transports.<br>
   * Any logs piped through this logger will also be piped to its parent.
   *
   * @param name   The name of the new logger.
   * @param parent The parent of the new logger.
   *
   * @throws IllegalArgumentException If an argument is incorrect or invalid.
   * @since 1.10.2-r0.1
   */
  public vxLogger(String name, vxLogger parent) throws IllegalArgumentException {
    this(name, parent, new Transport[0]);
  }

  /**
   * Creates a new logging system with the given name and transports under the given parent.<br>
   * Any logs piped through this logger will also be piped to its parent.
   *
   * @param name       The name of the new logger.
   * @param parent     The parent of the new logger.
   * @param transports Any transports to assign to the logger.
   *
   * @throws IllegalArgumentException If an argument is incorrect or invalid.
   * @since 1.10.2-r0.1
   */
  public vxLogger(String name, @Nullable vxLogger parent, Transport... transports) throws IllegalArgumentException {
    Validate.notNull(name, "Logger name cannot be null");
    Validate.isTrue(name.length() > 0, "Logger name cannot be an empty String");
    this.name = name;
    this.parent = parent;

    for (Transport transport : transports) add(transport);
  }

  /**
   * Adds a new logging transport to this logger.
   *
   * @param transport The transport to add.
   *
   * @return This logger.
   * @throws IllegalArgumentException If the transport is null or its name already exists.
   * @since 1.10.2-r0.1
   */
  public vxLogger add(Transport transport) throws IllegalArgumentException {
    Validate.notNull(transport);
    if (this.transports.containsKey(transport.name()))
      throw new IllegalArgumentException("Transport with given name already present");

    transports.put(transport.name(), transport);

    return this;
  }

  /**
   * Sends a messages to this logger to be handled by its transports. If a parent
   * logger is present, the parent will also receive this message.
   *
   * @param level   The level at which to log this message.
   * @param message The message to send.
   *
   * @throws IllegalArgumentException If the level or message is null.
   * @see #log(LoggingLevel, String, Object...)
   * @since 1.10.2-r0.1
   */
  public void log(LoggingLevel level, String message) throws IllegalArgumentException {
    Validate.notNull(level);
    Validate.notNull(message);
    transports.entrySet().stream()
        .filter(e -> level.ordinal() >= e.getValue().level().ordinal())
        .forEach(e -> e.getValue().receive(new Transport.TransportedMessage(message, this, level)));
    if (parent != null)
      parent.log(level, message);
  }

  /**
   * Sends a formatted message through {@link String#format(String, Object...)} before sending it
   * to the logger. If a parent is present, the parent will also recieve this message.
   *
   * @param level   The level at which to log this message.
   * @param message The formatted message.
   * @param args    The objects to populate the formatted message with.
   *
   * @throws IllegalArgumentException If the level or message is null.
   * @see #log(LoggingLevel, String)
   * @since 1.10.2-r0.1
   */
  public void log(LoggingLevel level, String message, Object... args) throws IllegalArgumentException {
    log(level, String.format(message, args));
  }

  /**
   * Logs a {@link LoggingLevel#SILLY silly} message to the logger.
   *
   * @param message The message to log.
   *
   * @throws IllegalArgumentException If the message is null.
   * @see #silly(String, Object...)
   * @see #log(LoggingLevel, String)
   * @since 1.10.2-r0.1
   */
  public void silly(String message) throws IllegalArgumentException {
    log(LoggingLevel.SILLY, message);
  }

  /**
   * Logs a formatted {@link LoggingLevel#SILLY silly} message to the logger.
   *
   * @param message The message to log.
   * @param args    The objects to format.
   *
   * @throws IllegalArgumentException If the message or args are null.
   * @see #silly(String)
   * @see #log(LoggingLevel, String, Object...)
   * @since 1.10.2-r0.1
   */
  public void silly(String message, Object... args) throws IllegalArgumentException {
    log(LoggingLevel.SILLY, message, args);
  }

  /**
   * Logs a {@link LoggingLevel#DEBUG debug} message to the logger.
   *
   * @param message The message to log.
   *
   * @throws IllegalArgumentException If the message is null.
   * @see #debug(String, Object...)
   * @see #log(LoggingLevel, String)
   * @since 1.10.2-r0.1
   */
  public void debug(String message) throws IllegalArgumentException {
    log(LoggingLevel.DEBUG, message);
  }

  /**
   * Logs a formatted {@link LoggingLevel#DEBUG debug} message to the logger.
   *
   * @param message The message to log.
   * @param args    The objects to format.
   *
   * @throws IllegalArgumentException If the message or args are null.
   * @see #debug(String)
   * @see #log(LoggingLevel, String, Object...)
   * @since 1.10.2-r0.1
   */
  public void debug(String message, Object... args) throws IllegalArgumentException {
    log(LoggingLevel.DEBUG, message, args);
  }

  /**
   * Logs a {@link LoggingLevel#VERBOSE verbose} message to the logger.
   *
   * @param message The message to log.
   *
   * @throws IllegalArgumentException If the message is null.
   * @see #verbose(String, Object...)
   * @see #log(LoggingLevel, String)
   * @since 1.10.2-r0.1
   */
  public void verbose(String message) throws IllegalArgumentException {
    log(LoggingLevel.VERBOSE, message);
  }

  /**
   * Logs a formatted {@link LoggingLevel#VERBOSE verbose} message to the logger.
   *
   * @param message The message to log.
   * @param args    The objects to format.
   *
   * @throws IllegalArgumentException If the message or args are null.
   * @see #verbose(String)
   * @see #log(LoggingLevel, String, Object...)
   * @since 1.10.2-r0.1
   */
  public void verbose(String message, Object... args) throws IllegalArgumentException {
    log(LoggingLevel.VERBOSE, message, args);
  }

  /**
   * Logs a {@link LoggingLevel#INFO info} message to the logger.
   *
   * @param message The message to log.
   *
   * @throws IllegalArgumentException If the message is null.
   * @see #info(String, Object...)
   * @see #log(LoggingLevel, String)
   * @since 1.10.2-r0.1
   */
  public void info(String message) throws IllegalArgumentException {
    log(LoggingLevel.INFO, message);
  }

  /**
   * Logs a formatted {@link LoggingLevel#INFO info} message to the logger.
   *
   * @param message The message to log.
   * @param args    The objects to format.
   *
   * @throws IllegalArgumentException If the message or args are null.
   * @see #info(String)
   * @see #log(LoggingLevel, String, Object...)
   * @since 1.10.2-r0.1
   */
  public void info(String message, Object... args) throws IllegalArgumentException {
    log(LoggingLevel.INFO, message, args);
  }

  /**
   * Logs a {@link LoggingLevel#WARNING warning} message to the logger.
   *
   * @param message The message to log.
   *
   * @throws IllegalArgumentException If the message is null.
   * @see #warning(String, Object...)
   * @see #log(LoggingLevel, String)
   * @since 1.10.2-r0.1
   */
  public void warning(String message) throws IllegalArgumentException {
    log(LoggingLevel.WARNING, message);
  }

  /**
   * Logs a formatted {@link LoggingLevel#WARNING warning} message to the logger.
   *
   * @param message The message to log.
   * @param args    The objects to format.
   *
   * @throws IllegalArgumentException If the message or args are null.
   * @see #warning(String)
   * @see #log(LoggingLevel, String, Object...)
   * @since 1.10.2-r0.1
   */
  public void warning(String message, Object... args) throws IllegalArgumentException {
    log(LoggingLevel.WARNING, message, args);
  }

  /**
   * Logs a {@link LoggingLevel#ERROR error} message to the logger.
   *
   * @param message The message to log.
   *
   * @throws IllegalArgumentException If the message is null.
   * @see #error(String, Object...)
   * @see #log(LoggingLevel, String)
   * @since 1.10.2-r0.1
   */
  public void error(String message) throws IllegalArgumentException {
    log(LoggingLevel.ERROR, message);
  }

  /**
   * Logs a formatted {@link LoggingLevel#ERROR error} message to the logger.
   *
   * @param message The message to log.
   * @param args    The objects to format.
   *
   * @throws IllegalArgumentException If the message or args are null.
   * @see #error(String)
   * @see #log(LoggingLevel, String, Object...)
   * @since 1.10.2-r0.1
   */
  public void error(String message, Object... args) throws IllegalArgumentException {
    log(LoggingLevel.ERROR, message, args);
  }

}
