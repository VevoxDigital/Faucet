package co.mcsp.vxlib.logging;

/**
 * @author Matthew Struble
 */
@SuppressWarnings("unused WeakerAccess")
public interface Transport {

  /**
   * A message that has been transported from a {@link vxLogger} to a {@link Transport}.
   */
  class TransportedMessage {

    /**
     * The level at which this message was transported.
     */
    public final LoggingLevel level;

    /**
     * The message contents
     */
    public final String message;

    /**
     * The logger that transported this message.
     */
    public final vxLogger logger;

    protected TransportedMessage(String message, vxLogger logger, LoggingLevel level) {
      this.level = level;
      this.message = message;
      this.logger = logger;
    }

  }

  /**
   * Fetches and returns the {@link LoggingLevel} of this transport. A setter
   * method is up to the implementing class.
   *
   * @return The logging level.
   */
  LoggingLevel level();

  /**
   * Gets the name of this transport.
   *
   * @return The name.
   */
  String name();

  /**
   * Called when a transport is to receive a specified message. Messages with
   * lesser logging levels will <b>not</b> be sent to this method.
   *
   * @param msg The message to be sent.
   */
  void receive(TransportedMessage msg);

}
