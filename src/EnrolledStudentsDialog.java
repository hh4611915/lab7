


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnrolledStudentsDialog extends JDialog {
    private JsonDatabaseManager db;
    private Course course;

    public EnrolledStudentsDialog(Frame owner, JsonDatabaseManager db, Course course) {
        super(owner, "Enrolled Students - " + course.getTitle(), true);
        this.db = db; this.course = course;
        setSize(520, 320);
        setLocationRelativeTo(owner);
        init();
    }

    private void init() {
        String[] cols = {"Student ID", "Name", "Email"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);

        List<String> sids = course.getStudentIds();
        db.loadUsers();
        for (String sid : sids) {
            User u = db.findUserById(sid).orElse(null);
            if (u instanceof Student) {
                model.addRow(new Object[]{u.getId(), u.getName(), u.getEmail()});
            } else {
                model.addRow(new Object[]{sid, "(not found)", "(not found)"});
            }
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        JButton close = new JButton("Close");
        close.addActionListener(e -> setVisible(false));
        JPanel bottom = new JPanel(); bottom.add(close);
        add(bottom, BorderLayout.SOUTH);
    }
}
