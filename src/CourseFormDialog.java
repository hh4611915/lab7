
import javax.swing.*;
import java.awt.*;

public class CourseFormDialog extends JDialog {
    private JTextField tfId = new JTextField();
    private JTextField tfTitle = new JTextField();
    private JTextArea taDesc = new JTextArea(5, 20);
    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");
    private boolean saved = false;

    public CourseFormDialog(Frame owner, String title, Course course) {
        super(owner, title, true);
        setSize(420, 300);
        setLocationRelativeTo(owner);
        initComponents(course);
    }

    private void initComponents(Course course) {
        JPanel p = new JPanel(new BorderLayout(6,6));
        JPanel fields = new JPanel(new GridLayout(3,1,4,4));
        fields.add(labeled("Course ID:", tfId));
        fields.add(labeled("Title:", tfTitle));
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        descPanel.add(new JScrollPane(taDesc), BorderLayout.CENTER);
        p.add(fields, BorderLayout.NORTH);
        p.add(descPanel, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(btnSave); buttons.add(btnCancel);
        p.add(buttons, BorderLayout.SOUTH);

        if (course != null) {
            tfId.setText(course.getCourseId()); tfId.setEnabled(false);
            tfTitle.setText(course.getTitle());
            taDesc.setText(course.getDescription());
        }

        add(p);

        btnSave.addActionListener(e -> {
            if (tfId.getText().trim().isEmpty() || tfTitle.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Title required.");
                return;
            }
            saved = true;
            setVisible(false);
        });
        btnCancel.addActionListener(e -> setVisible(false));
    }

    private JPanel labeled(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    public boolean isSaved() { return saved; }
    public String getCourseId() { return tfId.getText().trim(); }
    public String getTitle() { return tfTitle.getText().trim(); }
    public String getDescription() { return taDesc.getText().trim(); }
}
