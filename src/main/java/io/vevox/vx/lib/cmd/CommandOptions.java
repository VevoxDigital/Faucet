package io.vevox.vx.lib.cmd;

import io.vevox.vx.lib.cmd.arg.CommandFlag;
import io.vevox.vx.lib.cmd.arg.CommandSwitch;

import java.util.Map;
import java.util.Set;

/**
 * @author Matthew Struble
 */
public class CommandOptions {

  public final String[] args;

  private final Map<CommandFlag, String> flags;
  private final Set<CommandSwitch> switches;

  protected CommandOptions(String[] args, Map<CommandFlag, String> flags,
                         Set<CommandSwitch> switches) {
    this.args = args;
    this.flags = flags;
    this.switches = switches;
  }



}
