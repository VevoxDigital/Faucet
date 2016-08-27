package io.vevox.faucet;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Matthew Struble
 * @since 1.10.2-R0.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class FaucetPlugin extends JavaPlugin {



  private Consumer<Faucet> loadF, disableF;
  private Predicate<Faucet> enableF;

  /**
   * Assigns a {@link Consumer consumer} function to be called
   * when the plugin is first loaded (but not enabled).
   * <p>
   * An instanceof {@link Faucet} is passed to the consumer.
   * Called in place of {@link JavaPlugin#onLoad()}.
   */
  protected void load(Consumer<Faucet> func) {
    loadF = func;
  }

  /**
   * Assigns a {@link Predicate predicate} function to be called when
   * the plugin is enabled and the server is starting up or reloading.
   * <p>
   * An instanceof {@link Faucet} is passed to the consumer.
   * Called in place of {@link JavaPlugin#onEnable()}.
   * <p>
   * If the supplier returns false, the enable block will stop and
   * {@link #disable(Consumer)} is called.
   *
   * @param func The supplier function.
   */
  protected void enable(Predicate<Faucet> func) {
    enableF = func;
  }

  /**
   * Assigns a {@link Consumer consumer} function to be called when the plugin is
   * disabled and the server is shutting down or reloading.
   * <p>
   * An instanceof {@link Faucet} is passed to the consumer.
   * Called in place of {@link JavaPlugin#onDisable()}.
   *
   * @param func The supplier function.
   */
  protected void disable(Consumer<Faucet> func) {
    disableF = func;
  }

  @Override
  public final void onLoad() {
    loadF.accept(Faucet.instance);
  }

  @Override
  public final void onEnable() {
    if (!enableF.test(Faucet.instance))
      setEnabled(false);
  }

  @Override
  public final void onDisable() {
    disableF.accept(Faucet.instance);
  }

}
