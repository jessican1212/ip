package canelo;

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

    @Override
    public String toSaveFormat() {
        return "E,,,"+getStatusIcon()+",,,"+getDescription()+",,,"+from+",,,"+to;
    }
}
