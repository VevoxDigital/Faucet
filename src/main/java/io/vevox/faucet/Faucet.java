package io.vevox.faucet;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Matthew Struble
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Faucet extends FaucetPlugin {

  /**
   * A no-implementation consumer function for libraries and plugins
   * not wishing to implement
   * {@link #load(Consumer)} and/or {@link #disable(Consumer)}.
   */
  public static final Consumer<Faucet> noImplC = (f) -> { };

  /**
   * A no-implementation true-returning predicate function for libraries
   * and plugins not with to implement {@link #enable(Predicate)}.
   */
  public static final Predicate<Faucet> noImplP = (f) -> true;

  protected static Faucet instance;

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
