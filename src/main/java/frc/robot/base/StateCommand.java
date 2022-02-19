package frc.robot.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import edu.wpi.first.wpilibj2.command.Command;

public interface StateCommand extends Command {
    @Nullable
    String next();

    @Nullable
    String next(@Nullable String stateName);
    
    @NotNull
    String getStateName();
}
