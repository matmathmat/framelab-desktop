package fr.framelab.modules;

public class EditorModule {

    private final String title;
    private final String description;
    private final Runnable onTrigger;

    public EditorModule(String title, String description, Runnable onTrigger) {
        this.title = title;
        this.description = description;
        this.onTrigger = onTrigger;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Runnable getOnTrigger() {
        return this.onTrigger;
    }

    @Override
    public String toString() {
        return this.title;
    }
}

