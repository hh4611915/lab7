import javax.swing.*;
import java.awt.*;

public class LessonFormDialog extends JDialog {
    private JTextField tfLessonId = new JTextField();
    private JTextField tfTitle = new JTextField();
    private JTextArea taContent = new JTextArea(6, 25);
    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");
    private boolean saved = false;
    private ValidationAndHashing v = new ValidationAndHashing();
    public LessonFormDialog(Window owner, String title, Lesson lesson) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setSize(420, 320);
        setLocationRelativeTo(owner);
        init(lesson);
    }

    private void init(Lesson lesson) {
        JPanel p = new JPanel(new BorderLayout(6,6));
        JPanel fields = new JPanel(new GridLayout(2,1,4,4));
        fields.add(labeled("Lesson ID:", tfLessonId));
        fields.add(labeled("Title:", tfTitle));
        p.add(fields, BorderLayout.NORTH);
        JPanel contentP = new JPanel(new BorderLayout());
        contentP.add(new JLabel("Content:"), BorderLayout.NORTH);
        contentP.add(new JScrollPane(taContent), BorderLayout.CENTER);
        p.add(contentP, BorderLayout.CENTER);
        JPanel btns = new JPanel();
        btns.add(btnSave);
        btns.add(btnCancel);
        p.add(btns, BorderLayout.SOUTH);
        add(p);
        if (lesson != null) {
            tfLessonId.setText(lesson.getId());
            tfLessonId.setEnabled(false);
            tfTitle.setText(lesson.getTitle());
            taContent.setText(lesson.getContent());
        }
        btnSave.addActionListener(e -> {

            String id = tfLessonId.getText().trim();
            String title = tfTitle.getText().trim();

            if (id.isEmpty() || title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Title required.");
                return;
            }

            if (!v.validID(id, "Lesson")) {
                JOptionPane.showMessageDialog(this,
                        "Invalid Lesson ID.\nMust start with L followed by digits.");
                return;
            }

            if (tfLessonId.isEnabled() && v.lessonIdExist(id)) {
                JOptionPane.showMessageDialog(this,
                        "This Lesson ID already exists for this course.");
                return;
            }

            saved = true;
            setVisible(false);
        });
        btnCancel.addActionListener(e -> setVisible(false));
    }

    private JPanel labeled(String lbl, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(lbl), BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    public boolean isSaved() { return saved; }
    public String getLessonId() { return tfLessonId.getText().trim(); }
    public String getTitle() { return tfTitle.getText().trim(); }
    public String getContent() { return taContent.getText().trim(); }
}
