package canelo;

/**
 * Represents a deadline task with a due date.
 * This class extends the Task class and includes a due date for the task.
 */
public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Converts the deadline task into a formatted string representation for saving to a file.
     * The format includes the task type, status icon, description, start time, and end time,
     * separated by a specific delimiter (",,,").
     *
     * @return A string representation of the deadline task in save format.
     */
    @Override
    public String toSaveFormat() {
        return "D,,,"+getStatusIcon()+",,,"+getDescription()+",,,"+by;
    }
}
