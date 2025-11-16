
import javax.swing.*;
import java.awt.*;

public class LessonManagerDialog extends JDialog {
    private JsonDatabaseManager db;
    private Instructor instructor;
    private Course course;

    private DefaultListModel<Lesson> lessonModel = new DefaultListModel<>();
    private JList<Lesson> lessonJList = new JList<>(lessonModel);

    private JButton btnAdd = new JButton("Add Lesson");
    private JButton btnEdit = new JButton("Edit Lesson");
    private JButton btnDelete = new JButton("Delete Lesson");
    private JButton btnClose = new JButton("Close");

    public LessonManagerDialog(Frame owner, JsonDatabaseManager db, Instructor instructor, Course course) {
        super(owner, "Manage Lessons - " + course.getTitle(), true);
        this.db = db; this.instructor = instructor; this.course = course;
        setSize(540, 420);
        setLocationRelativeTo(owner);
        init();
        loadLessons();
    }

    private void init() {
        setLayout(new BorderLayout());
        add(new JScrollPane(lessonJList), BorderLayout.CENTER);

        JPanel btns = new JPanel(new GridLayout(4,1,6,6));
        btns.add(btnAdd); btns.add(btnEdit); btns.add(btnDelete); btns.add(btnClose);
        add(btns, BorderLayout.EAST);

        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnClose.addActionListener(e -> { db.saveCourses(); setVisible(false); });
    }

    private void loadLessons() {
        lessonModel.clear();
        db.loadCourses();
        Course fresh = db.findCourseById(course.getCourseId()).orElse(course);
        for (Lesson l : fresh.getLessons()) lessonModel.addElement(l);
    }

    private void onAdd() {
        LessonFormDialog dlg = new LessonFormDialog(this, "Add Lesson", null);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            Lesson l = new Lesson(dlg.getLessonId(), dlg.getTitle(), dlg.getContent());
            instructor.addLessonToCourse(course, l);
            db.saveCourses();
            loadLessons();
        }
    }

    private void onEdit() {
        Lesson selected = lessonJList.getSelectedValue();
        if (selected == null) { JOptionPane.showMessageDialog(this, "Select a lesson."); return; }
        LessonFormDialog dlg = new LessonFormDialog(this, "Edit Lesson", selected);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            instructor.editLessonInCourse(course, selected.getId(), dlg.getTitle(), dlg.getContent());
            db.saveCourses();
            loadLessons();
        }
    }

    private void onDelete() {
        Lesson selected = lessonJList.getSelectedValue();
        if (selected == null) { JOptionPane.showMessageDialog(this, "Select a lesson."); return; }
        int ok = JOptionPane.showConfirmDialog(this, "Delete lesson " + selected.getTitle() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            instructor.removeLessonFromCourse(course, selected.getId());
            db.saveCourses();
            loadLessons();
        }
    }
}
