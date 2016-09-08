package co.mcsp.vxlib;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Matthew Struble
 * @since 1.10.2-R0.1
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class vxPlugin extends JavaPlugin {

  private Consumer<vxLib> loadF, disableF;
  private Predicate<vxLib> enableF;

  /**
   * Assigns a {@link Consumer consumer} function to be called
   * when the plugin is first loaded (but not enabled).
   * <p>
   * An instanceof {@link vxLib} is passed to the consumer.
   * Called in place of {@link JavaPlugin#onLoad()}.
   */
  protected void load(Consumer<vxLib> func) {
    loadF = func;
  }

  /**
   * Assigns a {@link Predicate predicate} function to be called when
   * the plugin is enabled and the server is starting up or reloading.
   * <p>
   * An instanceof {@link vxLib} is passed to the consumer.
   * Called in place of {@link JavaPlugin#onEnable()}.
   * <p>
   * If the supplier returns false, the enable block will stop and
   * {@link #disable(Consumer)} is called.
   *
   * @param func The supplier function.
   */
  protected void enable(Predicate<vxLib> func) {
    enableF = func;
  }

  /**
   * Assigns a {@link Consumer consumer} function to be called when the plugin is
   * disabled and the server is shutting down or reloading.
   * <p>
   * An instanceof {@link vxLib} is passed to the consumer.
   * Called in place of {@link JavaPlugin#onDisable()}.
   *
   * @param func The supplier function.
   */
  protected void disable(Consumer<vxLib> func) {
    disableF = func;
  }

  @Override
  public final void onLoad() {
    loadF.accept(vxLib.instance);
  }

  @Override
  public final void onEnable() {
    if (!enableF.test(vxLib.instance))
      setEnabled(false);
  }

  @Override
  public final void onDisable() {
    disableF.accept(vxLib.instance);
  }

}
