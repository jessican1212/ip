package canelo;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getDescription() {
        return description;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public String getTypeIcon() {
        return "T";
    }

    /**
     * Converts the todo task into a formatted string representation for saving to a file.
     * The format includes the task type, status icon, description, start time, and end time,
     * separated by a specific delimiter (",,,").
     *
     * @return A string representation of the todo task in save format.
     */
    public String toSaveFormat() {
        return "T,,,"+getStatusIcon()+",,,"+getDescription();
    }
}
