import java.io.*;
import java.util.ArrayList;

public class CourseDatabase {
    private ArrayList<Course> courses;
    private String filename;

    public CourseDatabase(String filename) {
        this.filename = filename;
        courses = new ArrayList<>();
        loadFromFile();
    }

    public void addCourse(Course course) {
        courses.add(course);
        saveToFile();
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        saveToFile();
    }

    public ArrayList<Course> getCourses() { return courses; }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Course c : courses) {
                StringBuilder sb = new StringBuilder();
                sb.append(c.getId()).append("|").append(c.getName()).append("|");
                ArrayList<Lesson> lessons = c.getLessons();
                for (int i = 0; i < lessons.size(); i++) {
                    Lesson l = lessons.get(i);
                    sb.append(l.getId()).append(":").append(l.getTitle()).append(":").append(l.getContent());
                    if (i < lessons.size() - 1) sb.append(",");
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        courses.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                Course c = new Course(parts[0], parts[1]);
                if (parts.length > 2 && !parts[2].isEmpty()) {
                    String[] lessonParts = parts[2].split(",");
                    for (String l : lessonParts) {
                        String[] ldata = l.split(":");
                        c.addLesson(new Lesson(ldata[0], ldata[1], ldata[2]));
                    }
                }
                courses.add(c);
            }
        } catch (IOException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }
}
