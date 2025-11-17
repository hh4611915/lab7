import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LessonDashboard extends JFrame {
    // GUI Components
    private JPanel mainPanel;
    private JList<String> lessonList;
    private JTextArea contentArea;
    private JButton markCompletedButton;
    private JButton backButton;
    private JLabel courseTitleLabel;
    private JProgressBar progressBar;
    private JScrollPane listScrollPane;
    private JScrollPane contentScrollPane;
    private JSplitPane splitPane;

    // Data Fields
    private Course course;
    private Student student;
    private JFrame parentFrame;
    private ArrayList<Lesson> lessons;

    public LessonDashboard(Course course, Student student, JFrame parentFrame) {
        this.course = course;
        this.student = student;
        this.parentFrame = parentFrame;
        this.lessons = (ArrayList<Lesson>) course.getLessons();

        // 1. Window Setup
        setTitle("Course View: " + course.getName());
        setSize(900, 600);
        setLocationRelativeTo(null); // Centers the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2. Main Panel Layout (BorderLayout)
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 3. Top Panel: Back Button + Title
        JPanel topPanel = new JPanel(new BorderLayout());
        backButton = new JButton("<< Back");
        courseTitleLabel = new JLabel("Course: " + course.getName(), SwingConstants.CENTER);
        courseTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(courseTitleLabel, BorderLayout.CENTER);

        // 4. Center Area: Split Pane (List on left, Content on right)

        // Setup Lesson List
        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (lessons != null) {
            for (Lesson l : lessons) {
                listModel.addElement(l.getTitle());
            }
        }
        lessonList = new JList<>(listModel);
        lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        listScrollPane = new JScrollPane(lessonList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Lessons"));

        // Setup Content Area
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setBorder(BorderFactory.createTitledBorder("Lesson Content"));

        // Combine into SplitPane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, contentScrollPane);
        splitPane.setDividerLocation(250); // Width of the list column
        splitPane.setResizeWeight(0.3);

        // 5. Bottom Panel: Progress Bar + Action Button
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        markCompletedButton = new JButton("Mark Current Lesson as Completed");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        bottomPanel.add(progressBar, BorderLayout.CENTER);
        bottomPanel.add(markCompletedButton, BorderLayout.EAST);

        // 6. Add Panels to Main Layout
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Finalize Frame
        setContentPane(mainPanel);

        // --- Logic Initialization ---
        updateProgressBar();

        // --- Event Listeners ---

        // Listener 1: Display Content when a lesson is selected
        lessonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int idx = lessonList.getSelectedIndex();
                if (idx != -1 && lessons != null) {
                    contentArea.setText(lessons.get(idx).getContent());
                    contentArea.setCaretPosition(0); // Scroll to top
                }
            }
        });

        // Listener 2: Mark as Completed
        markCompletedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = lessonList.getSelectedIndex();
                if (idx == -1) {
                    JOptionPane.showMessageDialog(LessonDashboard.this, "Please select a lesson from the list first.");
                    return;
                }

                if (lessons == null || lessons.isEmpty()) return;

                // Calculate simple progress increment
                double increment = 100.0 / lessons.size();

                // Fetch current progress
                double currentP = 0;
                for(Student.Progress p : student.getProgresses()) {
                    if(p.getCourse().getId().equals(course.getId())) {
                        currentP = p.getPercentage();
                        break;
                    }
                }

                // Update logic
                double newProgress = Math.min(100.0, currentP + increment);
                student.updateProgress(course.getId(), newProgress);

                updateProgressBar();
                JOptionPane.showMessageDialog(LessonDashboard.this, "Lesson completed! Progress updated.");
            }
        });

        // Listener 3: Back Button
        backButton.addActionListener(e -> {
            parentFrame.setVisible(true);
            dispose(); // Close this window
        });
    }

    private void updateProgressBar() {
        for(Student.Progress p : student.getProgresses()) {
            if(p.getCourse().getId().equals(course.getId())) {
                progressBar.setValue((int) p.getPercentage());
                break;
            }
        }
    }
}