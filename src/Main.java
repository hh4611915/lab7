import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        // --- Ensure files exist ---
        try {
            File userFile = new File("users.txt");
            if (!userFile.exists()) userFile.createNewFile();

            File courseFile = new File("courses.txt");
            if (!courseFile.exists()) courseFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating files: " + e.getMessage());
            return;
        }

        // --- Create databases ---
        UserDatabase userDB = new UserDatabase("users.txt");
        CourseDatabase courseDB = new CourseDatabase("courses.txt");

        // --- Create an instructor ---
        Instructor instructor1 = new Instructor("I001", "Dr. Ahmed", "ahmed@uni.com", "hashed123");
        userDB.addUser(instructor1);

        // --- Create courses ---
        Course course1 = new Course("C001", "Java Programming");
        Course course2 = new Course("C002", "Data Structures");

        // Add courses to database
        courseDB.addCourse(course1);
        courseDB.addCourse(course2);

        // --- Add lessons to courses ---
        Lesson lesson1 = new Lesson("L001", "Java Basics", "Intro to Java");
        Lesson lesson2 = new Lesson("L002", "OOP in Java", "Classes and Objects");
        Lesson lesson3 = new Lesson("L003", "Arrays & Lists", "Working with arrays and lists");

        instructor1.addLessonToCourse(course1, lesson1);
        instructor1.addLessonToCourse(course1, lesson2);
        instructor1.addLessonToCourse(course2, lesson3);

        // --- Create students ---
        Student student1 = new Student("S001", "Mona", "mona@uni.com", "hashed456");
        Student student2 = new Student("S002", "Omar", "omar@uni.com", "hashed789");

        // Add students to database
        userDB.addUser(student1);
        userDB.addUser(student2);

        // --- Enroll students in courses ---
        student1.enrollCourse(course1);
        student2.enrollCourse(course1);
        student2.enrollCourse(course2);

        // --- Update student progress ---
        student1.updateProgress("C001", 50.0);
        student2.updateProgress("C001", 75.0);
        student2.updateProgress("C002", 20.0);

        // --- Display info ---
        System.out.println("=== Instructors ===");
        instructor1.showInfo();
        instructor1.viewCreatedCourses();

        System.out.println("\n=== Courses and Lessons ===");
        for (Course c : courseDB.getCourses()) {
            System.out.println("Course: " + c.getName());
            c.viewLessons();
            c.viewStudents();
            System.out.println();
        }

        System.out.println("=== Students and Progress ===");
        student1.showInfo();
        student1.viewProgress();

        student2.showInfo();
        student2.viewProgress();

        // --- Save databases again ---
        userDB.saveToFile();
        courseDB.saveToFile();
    }
}
