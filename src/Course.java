import java.util.ArrayList;

public class Course {
    private String id;
    private String name;
    private ArrayList<Lesson> lessons;
    private ArrayList<Student> students;

    public Course(String id, String name) {
        this.id = id;
        this.name = name;
        lessons = new ArrayList<>();
        students = new ArrayList<>();
    }

    // Lessons management
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void removeLesson(Lesson lesson) {
        lessons.remove(lesson);
    }

    public void viewLessons() {
        System.out.println("Lessons in course " + name + ":");
        for (Lesson l : lessons) {
            System.out.println("- " + l.getTitle());
        }
    }

    // Students management
    public void addStudent(Student student) {
        students.add(student);
    }

    public void viewStudents() {
        System.out.println("Students enrolled in " + name + ":");
        for (Student s : students) {
            System.out.println("- " + s.getName());
        }
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
}
