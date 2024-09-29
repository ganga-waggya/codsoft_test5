import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

class Course
{
    private String code;
    private String title;
    private String description;
    private int capacity;
    private String schedule;

    public Course(String code, String title, String description, int capacity, String schedule) 
    {
        this.code = code;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
    }

    public String getCode() 
    {
        return code;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription() 
    {
        return description;
    }

    public int getCapacity() 
    {
        return capacity;
    }

    public String getSchedule() 
    {
        return schedule;
    }

    @Override
    public String toString() 
    {
        return code + " - " + title;
    }
}

class Student
{
    private String id;
    private String name;
    private ArrayList<Course> registeredCourses;

    public Student(String id, String name)
    {
        this.id = id;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    public String getId() 
    {
        return id;
    }

    public String getName() 
    {
        return name;
    }

    public ArrayList<Course> getRegisteredCourses() 
    {
        return registeredCourses;
    }

    public void addCourse(Course course)
    {
        registeredCourses.add(course);
    }

    public void removeCourse(Course course) 
    {
        registeredCourses.remove(course);
    }

    @Override
    public String toString() 
    {
        return id + " - " + name;
    }
}

class CourseDatabase 
{
    private ArrayList<Course> courses;

    public CourseDatabase() 
    {
        courses = new ArrayList<>();
    }

    public void addCourse(Course course) 
    {
        courses.add(course);
    }

    public void removeCourse(Course course) 
    {
        courses.remove(course);
    }

    public ArrayList<Course> listAllCourses() 
    {
        return courses;
    }
}

class StudentDatabase 
{
    private ArrayList<Student> students;

    public StudentDatabase()
    {
        students = new ArrayList<>();
    }

    public void addStudent(Student student)
    {
        students.add(student);
    }

    public void removeStudent(Student student)
    {
        students.remove(student);
    }

    public ArrayList<Student> listAllStudents()
    {
        return students;
    }
}

class CourseListingFrame extends JFrame 
{
    private JTextArea courseListArea;

    public CourseListingFrame(CourseDatabase courseDatabase)
    {
        setTitle("Course Listing");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        courseListArea = new JTextArea();
        courseListArea.setEditable(false);

        ArrayList<Course> courses = courseDatabase.listAllCourses();

        for (Course course : courses) {
            courseListArea.append(
                    course.getCode() + " - " + course.getTitle() + " (Slots: " + (course.getCapacity() - 1) + ")\n");
        }

        add(courseListArea);
        setVisible(true);
    }
}

class StudentRegistrationFrame extends JFrame
{
    private JComboBox<String> studentComboBox;
    private JComboBox<String> courseComboBox;
    private JButton registerButton;
    private CourseDatabase courseDatabase; 

    public StudentRegistrationFrame(StudentDatabase studentDatabase, CourseDatabase courseDatabase) 
    {
        this.courseDatabase = courseDatabase; 
        setTitle("Student Registration");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        studentComboBox = new JComboBox<>();
        ArrayList<Student> students = studentDatabase.listAllStudents();
        for (Student student : students)
        {
            studentComboBox.addItem(student.toString());
        }

        courseComboBox = new JComboBox<>();
        ArrayList<Course> courses = courseDatabase.listAllCourses();
        for (Course course : courses) 
        {
            courseComboBox.addItem(course.toString());
        }

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                String selectedCourse = (String) courseComboBox.getSelectedItem();

                String studentId = selectedStudent.split(" - ")[0];
                String courseCode = selectedCourse.split(" - ")[0];

                Student selectedStudentObject = null;
                for (Student student : students)
                {
                    if (student.getId().equals(studentId)) 
                    {
                        selectedStudentObject = student;
                        break;
                    }
                }

                Course selectedCourseObject = null;
                for (Course course : courses) 
                {
                    if (course.getCode().equals(courseCode))
                    {
                        selectedCourseObject = course;
                        break;
                    }
                }

                if (selectedStudentObject != null && selectedCourseObject != null)
                {
                    selectedStudentObject.addCourse(selectedCourseObject);
                    courseDatabase.removeCourse(selectedCourseObject);
                    JOptionPane.showMessageDialog(null, "Registered for course: " + selectedCourse);
                } else {
                    JOptionPane.showMessageDialog(null, "Error registering for course.");
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(studentComboBox);
        panel.add(courseComboBox);
        panel.add(registerButton);

        add(panel);
        setVisible(true);
    }
}

class CourseRemovalFrame extends JFrame 
{
    private JComboBox<String> studentComboBox;
    private JComboBox<String> courseComboBox;
    private JButton removeButton;
    private StudentDatabase studentDatabase;
    private CourseDatabase courseDatabase;

    public CourseRemovalFrame(StudentDatabase studentDatabase, CourseDatabase courseDatabase) 
    {
        this.studentDatabase = studentDatabase;
        this.courseDatabase = courseDatabase;
        setTitle("Course Removal");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        studentComboBox = new JComboBox<>();
        ArrayList<Student> students = studentDatabase.listAllStudents();
        for (Student student : students) 
        {
            studentComboBox.addItem(student.toString());
        }

        courseComboBox = new JComboBox<>();

        studentComboBox.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                courseComboBox.removeAllItems();
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                String studentId = selectedStudent.split(" - ")[0];

                Student selectedStudentObject = null;
                for (Student student : students) 
                {
                    if (student.getId().equals(studentId)) 
                    {
                        selectedStudentObject = student;
                        break;
                    }
                }

                if (selectedStudentObject != null)
                {
                    ArrayList<Course> registeredCourses = selectedStudentObject.getRegisteredCourses();
                    for (Course course : registeredCourses) 
                    {
                        courseComboBox.addItem(course.toString());
                    }
                }
            }
        });

        removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String selectedStudent = (String) studentComboBox.getSelectedItem();
                String selectedCourse = (String) courseComboBox.getSelectedItem();

                String studentId = selectedStudent.split(" - ")[0];
                String courseCode = selectedCourse.split(" - ")[0];

                Student selectedStudentObject = null;
                for (Student student : students) 
                {
                    if (student.getId().equals(studentId)) 
                    {
                        selectedStudentObject = student;
                        break;
                    }
                }

                Course selectedCourseObject = null;
                for (Course course : courseDatabase.listAllCourses()) 
                {
                    if (course.getCode().equals(courseCode)) 
                    {
                        selectedCourseObject = course;
                        break;
                    }
                }

                if (selectedStudentObject != null && selectedCourseObject != null) 
                {
                    selectedStudentObject.removeCourse(selectedCourseObject);
                    courseDatabase.addCourse(selectedCourseObject);
                    JOptionPane.showMessageDialog(null, "Removed course: " + selectedCourse);
                } 
                else 
                {
                    JOptionPane.showMessageDialog(null, "Error removing course.");
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(studentComboBox);
        panel.add(courseComboBox);
        panel.add(removeButton);

        add(panel);
        setVisible(true);
    }
}

public class StudentCourseRegistrationSystem
{
    public static void main(String[] args) 
    {
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.addCourse(new Course("IT4001", "CN", "Introduction to Computer Networks", 25, "MWF 8:00 AM"));
        courseDatabase.addCourse(new Course("IT4002", "AWP", "Advanced Web Programming", 35, "TTH 3:00 PM"));
        courseDatabase.addCourse(new Course("IT4003", "Introduction to C", "Introduction to C Programming", 30, "MWF 1:00 PM"));
        courseDatabase.addCourse(new Course("IT4004", "DM", "Introduction to Discete Mathematics", 25, "TTH 11:00 AM"));
        courseDatabase.addCourse(new Course("IT4005", "DLA", "Introduction to DLA ", 30, "MWF 5:00 PM"));
        courseDatabase.addCourse(new Course("IT4006", "Web Applications", "Introduction to Web Application", 35, "TTH 4:00 PM"));
        courseDatabase.addCourse(new Course("IT4007", "Introcution to C++", "Introduction to C++", 30, "MWF 10:00 AM"));
        courseDatabase.addCourse(new Course("IT4008", "Introduction to Linux", "Introduction to Linux", 20, "TTH 1:00 PM"));
        courseDatabase.addCourse(new Course("IT4009", "CG", "Introduction to Computer Graphics", 30, "MWF 2:00 PM"));
        courseDatabase.addCourse(new Course("IT4010", "SE", "Introduction to Software Engineering", 35, "TTH 7:00 AM"));

        StudentDatabase studentDatabase = new StudentDatabase();
        studentDatabase.addStudent(new Student("10000", "Alfred Hutheesing"));
        studentDatabase.addStudent(new Student("10001", "Freedon annunziato"));
        studentDatabase.addStudent(new Student("10002", "Bernadetter Mintanez"));
        studentDatabase.addStudent(new Student("10003", "Davis Thares"));
        studentDatabase.addStudent(new Student("10004", "Lloyd Kellam"));
        studentDatabase.addStudent(new Student("10005", "Olivia Taylor"));
        studentDatabase.addStudent(new Student("10006", "Jenny Goldsmith"));
        studentDatabase.addStudent(new Student("10007", "Barbara Robichaud"));
        studentDatabase.addStudent(new Student("10008", "Reynaldo Chatman"));
        studentDatabase.addStudent(new Student("10009", "Eric Da Silva"));
        studentDatabase.addStudent(new Student("10010", "Alexander Grambell"));
        studentDatabase.addStudent(new Student("10011", "Madhva Dusenberry"));
        studentDatabase.addStudent(new Student("10012", "Robert Segall"));
        studentDatabase.addStudent(new Student("10013", "Alex"));
        studentDatabase.addStudent(new Student("10014", "Andy"));
        studentDatabase.addStudent(new Student("10015", "Scoct"));
        studentDatabase.addStudent(new Student("10016", "Emily"));
        studentDatabase.addStudent(new Student("10017", "Andrew"));
        studentDatabase.addStudent(new Student("10018", "Joseph"));
        studentDatabase.addStudent(new Student("10019", "Daniel"));
        


        CourseListingFrame courseListingFrame = new CourseListingFrame(courseDatabase);
        StudentRegistrationFrame studentRegistrationFrame = new StudentRegistrationFrame(studentDatabase,
                courseDatabase);
        CourseRemovalFrame courseRemovalFrame = new CourseRemovalFrame(studentDatabase, courseDatabase);
    }
}
