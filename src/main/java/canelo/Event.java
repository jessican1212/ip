package canelo;

/**
 * Represents an event task with a start and end time.
 * This class extends the Task class and includes additional details about event timing.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Converts the event task into a formatted string representation for saving to a file.
     * The format includes the task type, status icon, description, start time, and end time,
     * separated by a specific delimiter (",,,").
     *
     * @return A string representation of the event task in save format.
     */
    @Override
    public String toSaveFormat() {
        return "E,,,"+getStatusIcon()+",,,"+getDescription()+",,,"+from+",,,"+to;
    }
}
