package entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
@Table(name = "course", schema = "cy_e_admin")
/**
 * Entity linked to the student table.
 */
public class Course {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "field", nullable = false)
    private String field;

    @Column(name = "day", nullable = false)
    private Date day;

    @Column(name = "hour", nullable = false)
    private String hour;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "school_year", nullable = false)
    private String schoolYear;

    //TODO : CASCADING for deleting

    // No PERSIST cascading because Teacher instances must be created before Course instances
    //so Teacher instances are not transient while Course ones are
    // MERGE cascading not required because .merge() is called during transactions
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)
    private Teacher teacher;

    //TODO : STUDENT CASCADING -> MERGE, REFRESH
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "student_courses",
            joinColumns = { @JoinColumn(name = "course_id") },
            inverseJoinColumns = { @JoinColumn(name = "student_id") }
    )
    /**
     * Association table student_courses.
     * Course table is equivalent to the host.
     */
    private List<Student> students;

    public Course(String field, Date day, String hour, String duration, String schoolYear, Teacher teacher) {
        this.field = field;
        this.day = day;
        // Check if hour entry is valid
        if (Arrays.asList(new String[] {"8h30", "10h15", "12h00", "13h00", "14h45", "16h30", "18h15"}).contains(hour))
            this.hour = hour;
        // Check if duration entry is valid
        if (duration.equals("1h30") || duration.equals("3h00"))
            this.duration = duration;
        /*else
            throw new AddException("Could not create course instance, invalid duration value");*/
        // Check if schoolYear entry is valid
        if (Arrays.asList(new String[] {"ING1 GI GR1", "ING1 GI GR2", "ING1 GI GR3", "ING1 GI GR4", "ING2 GSI GR1", "ING2 GSI GR1",
                "ING2 GSI GR1", "ING3 Artificial Intelligence", "ING3 Business Intelligence", "ING3 Cloud Computing",
                "ING3 Cybersecurity", "ING3 Embedded Systems"}).contains(schoolYear))
            this.schoolYear = schoolYear;

        this.teacher = teacher;
        students = new ArrayList<>();
    }

    public Course() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getHour() { return hour; }

    public void setHour(String newHour) { hour = newHour; }

    public String getDuration() { return duration; }

    public void setDuration(String newDuration) { duration = newDuration; }

    public String getSchoolYear() { return schoolYear; }

    public void setSchoolYear(String newSchoolYear) { schoolYear = newSchoolYear; }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() { return students; }

    public void setStudents(List<Student> students) { this.students = students; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        if (field != null ? !field.equals(course.field) : course.field != null) return false;
        if (day != null ? !day.equals(course.day) : course.day != null) return false;
        if (duration != null ? !duration.equals(course.duration) : course.duration != null) return false;
        if (schoolYear != null ? !schoolYear.equals(course.schoolYear) : course.schoolYear != null) return false;

        return true;
    }

    /**
     * For tests purposes.<br>
     * Because ids are generated automatically, no constructor with an id as a parameter is required.
     * Since test data is known, the corresponding id has to be specified.
     */
    public boolean equals(Object o, int id) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (this.id != id) return false;
        if (field != null ? !field.equals(course.field) : course.field != null) return false;
        if (day != null ? !day.equals(course.day) : course.day != null) return false;
        if (duration != null ? !duration.equals(course.duration) : course.duration != null) return false;
        if (schoolYear != null ? !schoolYear.equals(course.schoolYear) : course.schoolYear != null) return false;

        return true;
    }

    /**
     * For debugging.
     */
    @Override
    public String toString() {
        return "Course instance\n" +
                "id : " + id + "; field : " + field + "; slot : " + day.toString() + " " + hour.toString()
                + "; duration : " + duration + ";\nschoolYear : " + schoolYear + "; teacher : " + teacher.getFirstname()
                + " " + teacher.getLastname() + "\n"
                /*+ courseStudentsToString()*/;
    }

    /**
     * For debugging.<br>
     * Be careful when calling .courseStudentToString() in .toString() while debugging any operations linked to an entity relation to Course.<br>
     * It will overlap with the other entity .toString() because each instance will print the other one infinitely.<br>
     * For instance, with .studentTimetableToString() from Student entity.<br>
     * Do not call .courseStudentsToString() when debugging IS NOT focused on a course instance.
     */
    public String courseStudentsToString() {
        String courseStudents = "";
        for (Student signedUpStudent : students)
            courseStudents += "Student instance\nid : " + signedUpStudent.getId() + "; firstname : " + signedUpStudent.getFirstname()
                    + "; lastname : " + signedUpStudent.getLastname() + "\n";

        return "Course students :\n" + courseStudents;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (schoolYear != null ? schoolYear.hashCode() : 0);
        return result;
    }


}
