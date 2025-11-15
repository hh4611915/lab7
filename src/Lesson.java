public class Lesson {
    private String id;
    private String title;
    private String content;

    public Lesson(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }

    public void showLesson() {
        System.out.println("Lesson: " + title + "\nContent: " + content);
    }
}
