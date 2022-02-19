package frc.robot.base;

import org.jetbrains.annotations.Nullable;

import edu.wpi.first.wpilibj2.command.CommandBase;

public abstract class StateCommandBase extends CommandBase implements StateCommand {
    private String nextStateName;

    @Override
    @Nullable
    public String next() {
        return nextStateName;
    }

    @Override
    @Nullable
    public String next(String stateName) {
        String temp = nextStateName;
        nextStateName = stateName;
        return temp;
    }
}
