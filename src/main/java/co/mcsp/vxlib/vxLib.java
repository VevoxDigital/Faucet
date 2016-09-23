package co.mcsp.vxlib;

import co.mcsp.vxlib.logging.LoggingLevel;
import co.mcsp.vxlib.logging.TransportConsole;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Matthew Struble
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class vxLib extends vxPlugin {

  /**
   * A no-implementation consumer function for libraries and plugins
   * not wishing to implement
   * {@link #load(Consumer)} and/or {@link #disable(Consumer)}.
   */
  public static final Consumer<vxLib> noImplC = l -> { };

  /**
   * A no-implementation true-returning predicate function for libraries
   * and plugins not with to implement {@link #enable(Predicate)}.
   */
  public static final Predicate<vxLib> noImplP = l -> true;

  protected static vxLib instance;

  {
    // Load
    load((l) -> {
      // The vxLib instance should be null at this point. Let's initialize it.
      instance = this;
      logger().add(new TransportConsole(LoggingLevel.DEBUG));
    });

    // TODO Actually implement this.
    enable(noImplP);
    disable(noImplC);
  }



}
