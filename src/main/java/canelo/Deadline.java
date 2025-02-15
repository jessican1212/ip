package canelo;

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

    @Override
    public String toSaveFormat() {
        return "D,,,"+getStatusIcon()+",,,"+getDescription()+",,,"+by;
    }
}
