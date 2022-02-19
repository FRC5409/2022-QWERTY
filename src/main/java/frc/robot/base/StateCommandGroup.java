package frc.robot.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import edu.wpi.first.wpilibj2.command.CommandBase;

public abstract class StateCommandGroup extends CommandBase {
    protected Map<String, StateCommand> m_states;
    protected StateCommand m_default;
    protected StateCommand m_active;

    protected static Set<StateCommand> getUngrouped(StateCommand... commands) {
        return Set.of(commands);
    }

    public StateCommandGroup() {
        m_active = null;
        m_default = null;
        m_states = new HashMap<>();
    }
    
    public StateCommandGroup(StateCommand... commands) {
        this();
        addCommands(commands);
    }

    public StateCommandGroup(String defaultStateName, StateCommand... commands) {
        this();
        addCommands(commands);
        m_default = getCommand(defaultStateName);
    }

    @Override
    public void initialize() {
        // Check if state was externally set before command execution
        if (m_active == null) {
            // Use default command if specified
            if (m_default == null) {
                System.err.println("No initial state specified");
                return;
            }
            
            m_active = m_default;
            System.out.println ("Starting with state " + m_active.getStateName());
        }

        m_active.initialize();
    }

    @Override
    public void execute() {
        if (m_active == null)
            return;

        // A state can only switch when finished
        if (m_active.isFinished()) {
            m_active.end(false);

            // 'pop' next state name off of state
            String next = m_active.next(null);
            if (next != null) {
                System.out.println("Moving to state " + next);
                m_active = getCommand(next);
                m_active.initialize();
            } else 
                m_active = null;
        } else
            m_active.execute();
    }

    @Override
    public void end(boolean interrupted) {
        if (m_active == null)
            return;

        m_active.end(interrupted);
        m_active = null;
    }

    public void addCommands(StateCommand... commands) {
        for (StateCommand cmd : getUngrouped(commands)) {
            final String name = cmd.getStateName();
            if (m_states.containsKey(name)) {
                throw new IllegalArgumentException(
                    "Conflict between commands using the name '"
                        + name + "' between command '" + cmd.getName()
                            +  "' and '" + m_states.get(name).getName() + "'");
            }

            m_requirements.addAll(cmd.getRequirements());
            m_states.put(name, cmd);
        }
    }

    public void setDefaultState(String name) {
        m_default = getCommand(name);
    }
    
    public boolean setActiveState(String name) {
        StateCommand command = getCommand(name);

        // Interrupt the active command
        m_active.end(true);

        // Start new (or previously running) command
        m_active = command;
        m_active.initialize();

        return true;
    }

    @Nullable
    public String getActiveState() {
        if (m_active == null)
            return null;
        return m_active.getStateName();
    }

    public StateCommand getCommand(String name) throws UnknownStateException {
        StateCommand command = m_states.get(name);
        if (command == null)
            throw new UnknownStateException("Command state '" + name + "' does not exist.");
        return command;
    }

    public Map<String, StateCommand> getCommands() {
        return Collections.unmodifiableMap(m_states);
    }

    @Override
    public boolean isFinished() {
        return m_active == null;
    }
}
