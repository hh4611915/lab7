import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class CourseLessonDB {
    private final String filename = "courses.json";
    private ArrayList<Course> courses;
    private ArrayList<Lesson> lessons;

    public CourseLessonDB() {
        courses = new ArrayList<>();
        lessons = new ArrayList<>();
        load();
    }

    public void addCourse(Course c) {
        courses.add(c);
        save();
    }

    public void addLesson(Lesson l) {
        lessons.add(l);
        save();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    private void save() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("courses", new JSONArray(courses));
            obj.put("lessons", new JSONArray(lessons));

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(obj.toString(4));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            File file = new File(filename);
            if (!file.exists()) return;

            StringBuilder text = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) text.append(line);
            }

            if (text.length() == 0) return;

            JSONObject obj = new JSONObject(text.toString());

            courses.clear();
            lessons.clear();

            JSONArray cArr = obj.optJSONArray("courses");
            if (cArr != null) {
                for (int i = 0; i < cArr.length(); i++) {
                    JSONObject c = cArr.getJSONObject(i);
                    Course course = new Course(
                            c.getString("id"),
                            c.getString("name")
                    );
                    courses.add(course);
                }
            }

            JSONArray lArr = obj.optJSONArray("lessons");
            if (lArr != null) {
                for (int i = 0; i < lArr.length(); i++) {
                    JSONObject l = lArr.getJSONObject(i);
                    Lesson lesson;
                    lesson = new Lesson(
                            l.getString("id"),
                            l.getString("name"),
                            l.getString("content")
                    );
                    lessons.add(lesson);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
