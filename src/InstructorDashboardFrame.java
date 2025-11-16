
import javax.swing.*;
import java.awt.*;

public class InstructorDashboardFrame extends JFrame {
    private JsonDatabaseManager db;
    private Instructor instructor;

    private DefaultListModel<Course> courseListModel = new DefaultListModel<>();
    private JList<Course> courseJList = new JList<>(courseListModel);

    private JButton btnCreate = new JButton("Create Course");
    private JButton btnEdit = new JButton("Edit Course");
    private JButton btnDelete = new JButton("Delete Course");
    private JButton btnManageLessons = new JButton("Manage Lessons");
    private JButton btnViewStudents = new JButton("View Enrolled Students");
    private JButton btnRefresh = new JButton("Refresh");

    public InstructorDashboardFrame(JsonDatabaseManager db, Instructor instructor) {
        super("Instructor Dashboard - " + instructor.getName());
        this.db = db;
        this.instructor = instructor;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 480);
        setLocationRelativeTo(null);
        initComponents();
        loadCourses();
    }

    private void initComponents() {
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(BorderFactory.createTitledBorder("Your Courses"));
        courseJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        left.add(new JScrollPane(courseJList), BorderLayout.CENTER);

        JPanel leftButtons = new JPanel(new GridLayout(6,1,8,8));
        leftButtons.add(btnCreate);
        leftButtons.add(btnEdit);
        leftButtons.add(btnDelete);
        leftButtons.add(btnManageLessons);
        leftButtons.add(btnViewStudents);
        leftButtons.add(btnRefresh);

        add(left, BorderLayout.WEST);
        add(leftButtons, BorderLayout.CENTER);

        btnCreate.addActionListener(e -> onCreateCourse());
        btnEdit.addActionListener(e -> onEditCourse());
        btnDelete.addActionListener(e -> onDeleteCourse());
        btnManageLessons.addActionListener(e -> onManageLessons());
        btnViewStudents.addActionListener(e -> onViewStudents());
        btnRefresh.addActionListener(e -> loadCourses());
    }

    private void loadCourses() {
        courseListModel.clear();
        db.loadCourses();
        for (Course c : db.getCourses()) {
            if (instructor.getCreatedCourseIds().contains(c.getCourseId())) {
                courseListModel.addElement(c);
            }
        }
    }

    private void onCreateCourse() {
        CourseFormDialog dlg = new CourseFormDialog(this, "Create Course", null);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            Course newCourse = instructor.createCourse(dlg.getCourseId(), dlg.getTitle(), dlg.getDescription());
            db.addCourse(newCourse);
            db.saveUsers();
            loadCourses();
        }
    }

    private void onEditCourse() {
        Course selected = courseJList.getSelectedValue();
        if (selected == null) { JOptionPane.showMessageDialog(this, "Select a course first."); return; }
        CourseFormDialog dlg = new CourseFormDialog(this, "Edit Course", selected);
        dlg.setVisible(true);
        if (dlg.isSaved()) {
            instructor.editCourse(selected, dlg.getTitle(), dlg.getDescription());
            db.updateCourse(selected);
            loadCourses();
        }
    }

    private void onDeleteCourse() {
        Course selected = courseJList.getSelectedValue();
        if (selected == null) { JOptionPane.showMessageDialog(this, "Select a course first."); return; }
        int ok = JOptionPane.showConfirmDialog(this, "Delete course " + selected.getTitle() + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            db.removeCourse(selected.getCourseId());
            instructor.removeCreatedCourse(selected.getCourseId());
            db.saveUsers();
            loadCourses();
        }
    }

    private void onManageLessons() {
        Course selected = courseJList.getSelectedValue();
        if (selected == null) { JOptionPane.showMessageDialog(this, "Select a course first."); return; }
        LessonManagerDialog dlg = new LessonManagerDialog(this, db, instructor, selected);
        dlg.setVisible(true);
        db.loadCourses();
        loadCourses();
    }

    private void onViewStudents() {
        Course selected = courseJList.getSelectedValue();
        if (selected == null) { JOptionPane.showMessageDialog(this, "Select a course first."); return; }
        EnrolledStudentsDialog dlg = new EnrolledStudentsDialog(this, db, selected);
        dlg.setVisible(true);
    }
}
