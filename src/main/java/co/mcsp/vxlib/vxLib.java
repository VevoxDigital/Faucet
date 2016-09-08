package co.mcsp.vxlib;

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
  public static final Consumer<vxLib> noImplC = (f) -> { };

  /**
   * A no-implementation true-returning predicate function for libraries
   * and plugins not with to implement {@link #enable(Predicate)}.
   */
  public static final Predicate<vxLib> noImplP = (f) -> true;

  protected static vxLib instance;

  {
    // Load
    load((faucet) -> {
      // The faucet variable should be null at this point.
      instance = this;
    });

    // TODO Actually implement this.
    enable(noImplP);
    disable(noImplC);
  }

}
