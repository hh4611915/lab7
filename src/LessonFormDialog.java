
import javax.swing.*;
import java.awt.*;

public class LessonFormDialog extends JDialog {
    private JTextField tfId = new JTextField();
    private JTextField tfTitle = new JTextField();
    private JTextArea taContent = new JTextArea(6, 25);
    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");
    private boolean saved = false;

    public LessonFormDialog(Dialog owner, String title, Lesson lesson) {
        super(owner, title, true);
        setSize(420, 320);
        setLocationRelativeTo(owner);
        init(lesson);
    }

    private void init(Lesson lesson) {
        JPanel p = new JPanel(new BorderLayout(6,6));
        JPanel fields = new JPanel(new GridLayout(2,1,4,4));
        fields.add(labeled("Lesson ID:", tfId));
        fields.add(labeled("Title:", tfTitle));
        p.add(fields, BorderLayout.NORTH);
        JPanel contentP = new JPanel(new BorderLayout());
        contentP.add(new JLabel("Content:"), BorderLayout.NORTH);
        contentP.add(new JScrollPane(taContent), BorderLayout.CENTER);
        p.add(contentP, BorderLayout.CENTER);

        JPanel btns = new JPanel(); btns.add(btnSave); btns.add(btnCancel);
        p.add(btns, BorderLayout.SOUTH);
        add(p);

        if (lesson != null) {
            tfId.setText(lesson.getId()); tfId.setEnabled(false);
            tfTitle.setText(lesson.getTitle()); taContent.setText(lesson.getContent());
        }

        btnSave.addActionListener(e -> {
            if (tfId.getText().trim().isEmpty() || tfTitle.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Title required.");
                return;
            }
            saved = true; setVisible(false);
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
    public String getLessonId() { return tfId.getText().trim(); }
    public String getTitle() { return tfTitle.getText().trim(); }
    public String getContent() { return taContent.getText().trim(); }
}
