package entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student",  schema = "cy_e_admin")
/**
 * Entity linked to the student table.
 */
public class Student {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    /**
     * Cannot be NULL (see SQL file)
     */
    @Column(name = "firstname", nullable = false)
    private String firstname;

    /**
     * Cannot be NULL (see SQL file)
     */
    @Column(name = "lastname", nullable = false)
    private String lastname;

    /**
     * Is unique (see SQL file)
     */
    //TODO : REGEX
    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    /**
     * Cannot be NULL (see SQL file)
     */
    //TODO : REGEX
    @Column(name = "pswd", nullable = false)
    private String pswd;

    /**
     * Cannot be NULL (see SQL file)
     */
    @Column(name = "birth", nullable = false)
    private Date birth;

    /**
     * Cannot be NULL (see SQL file)
     */
    @Column(name = "school_year", nullable = false)
    private String schoolYear;

    @OneToMany(mappedBy = "student")
    private Set<Result> results;

    //TODO : STUDENT CASCADING -> PERSIST, MERGE (?), REFRESH OU SAVE_UPDATE


    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "students")
    /**
     * Student table is equivalent to the referenced.
     */
    private List<Course> timetable;

    public Student(String firstname, String lastname, String mail, String pswd, Date birth,
                   String schoolYear) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.pswd = pswd;
        this.birth = birth;
        this.schoolYear = schoolYear;
        results = new HashSet<>();
        timetable = new ArrayList<>();
    }

    public Student() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public List<Course> getTimetable() { return timetable; }

    public void setTimetable(List<Course> timetable) { this.timetable = timetable; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != student.id) return false;
        if (firstname != null ? !firstname.equals(student.firstname) : student.firstname != null) return false;
        if (lastname != null ? !lastname.equals(student.lastname) : student.lastname != null) return false;
        if (mail != null ? !mail.equals(student.mail) : student.mail != null) return false;
        if (pswd != null ? !pswd.equals(student.pswd) : student.pswd != null) return false;
        if (birth != null ? !birth.equals(student.birth) : student.birth != null) return false;
        if (schoolYear != null ? !schoolYear.equals(student.schoolYear) : student.schoolYear != null) return false;

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

        Student student = (Student) o;

        if (this.id != id) return false;
        if (firstname != null ? !firstname.equals(student.firstname) : student.firstname != null) return false;
        if (lastname != null ? !lastname.equals(student.lastname) : student.lastname != null) return false;
        if (mail != null ? !mail.equals(student.mail) : student.mail != null) return false;
        if (pswd != null ? !pswd.equals(student.pswd) : student.pswd != null) return false;
        if (birth != null ? !birth.equals(student.birth) : student.birth != null) return false;
        if (schoolYear != null ? !schoolYear.equals(student.schoolYear) : student.schoolYear != null) return false;

        return true;
    }

    /**
     * For debugging.
     */
    @Override
    public String toString() {
        return "Student instance\n" +
                "id : " + id + "; firstname : " + firstname + "; lastname : " + lastname + "; mail : " + mail
                + ";\npassword : " + pswd + "; birthday : " + birth.toString() + "; school year : " + schoolYear + "\n"
                /*+ studentTimetableToString()*/;
    }

    /**
     * For debugging.<br>
     * Be careful when calling .studentTimetableToString() in .toString() while debugging any operations linked to an entity relation to Student.<br>
     * It will overlap with the other entity .toString() because each instance will print the other one infinitely.<br>
     * For instance, with .courseStudentsToString() from Course entity.<br>
     * Do not call .studentTimetableToString() when debugging IS NOT focused on a student instance.
     */
    public String studentTimetableToString() {
        String studentTimetable = "";
        for (Course studentClass : timetable)
            studentTimetable += "Course instance :\nfield : " + studentClass.getField() + "; date : "
                    + studentClass.getDay() + " " + studentClass.getHour() + "; teacher : " + studentClass.getTeacher().getFirstname()
                    + " " + studentClass.getTeacher().getLastname() + "\n";

        return "Student timetable :\n" + studentTimetable;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (pswd != null ? pswd.hashCode() : 0);
        result = 31 * result + (birth != null ? birth.hashCode() : 0);
        result = 31 * result + (schoolYear != null ? schoolYear.hashCode() : 0);
        return result;
    }

    public Set<Result> getResults() {
        return results;
    }

    public void setResults(Set<Result> results) {
        this.results = results;
    }
}
