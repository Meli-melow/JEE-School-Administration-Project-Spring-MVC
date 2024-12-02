package services;

import entities.Student;
import entities.Course;
import entityManagerSetUp.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.CourseRepository;
import repositories.StudentRepository;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public String createStudent(Student student) {

        // Check if school year entry is valid
        if (!Arrays.asList(new String[] {"ING1 GI GR1", "ING1 GI GR2", "ING1 GI GR3", "ING1 GI GR4", "ING2 GSI GR1", "ING2 GSI GR1",
                "ING2 GSI GR1", "ING3 Artificial Intelligence", "ING3 Business Intelligence", "ING3 Cloud Computing",
                "ING3 Cybersecurity", "ING3 Embedded Systems"}).contains(student.getSchoolYear()))

            return "Student creation failed. Please make sure school year is valid\n";

        try {
            EntityManager em = EntityManagerUtil.getEntityManager();
            // Add the existing courses to the student timetable
            String hql = "SELECT c FROM Course c WHERE c.schoolYear = :school_year";
            TypedQuery<Course> query = em.createQuery(hql, Course.class);
            query.setParameter("school_year", student.getSchoolYear());
            try {
                List<Course> newTimetable = query.getResultList();
                if (!newTimetable.isEmpty()) {
                    student.setTimetable(newTimetable);
                    for (Course studentClass : student.getTimetable()) {
                        studentClass.getStudents().add(student);
                        // If the course instances are not managed
                        if (!em.contains(studentClass))
                            return "Student creation failed. Could not sign up student to existing courses during student creation\n";

                        courseRepository.save(studentClass);
                    }
                }
            } catch (NoResultException nrex) {
                // There are no courses having the corresponding school year
                studentRepository.save(student);
                return "Student created successfully !\n";
            }
            studentRepository.save(student);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("EntityManagerFactory initialisation failed : " + ex);
        }
        return "Student creation and sign up to existing courses successfull !\n";
    }

    public Student studentLogin(String studentMail, String pswd) {

        Student logedInStudent;
        try {
            logedInStudent = studentRepository.findStudentByMailAndPswd(studentMail, pswd);
        } catch (Throwable ex) {
            throw new RuntimeException("Error during student creation : " + ex);
        }
        return logedInStudent;
    }

    public Student getStudentById(int studentId) {
        // Return the instance associated to the Optional instance
        return studentRepository.findById(studentId).orElse(null);
    }

    public Student getStudentByMail(String studentMail) {
        return studentRepository.findStudentByMail(studentMail);
    }

    @Transactional
    public String patchStudentProfile(int studentId, String newFirstname, String newLastname, String newMail, Date newBirth,
    String newProm) {

        String serviceMessage;
        try {
            EntityManager em = EntityManagerUtil.getEntityManager();
            Student student = studentRepository.findById(studentId).get();
            if (newFirstname != null && newFirstname != "" && !newFirstname.equals(student.getFirstname()))
                student.setFirstname(newFirstname);

            if (newLastname != null && newLastname != "" && !newLastname.equals(student.getLastname()))
                student.setLastname(newLastname);

            if (newMail != null && newMail != "" && !newMail.equals(student.getMail()))
                student.setMail(newMail);

            if (!newBirth.equals(java.sql.Date.valueOf("1900-01-01")) && !newBirth.equals(student.getBirth()))
                student.setBirth(newBirth);

            serviceMessage = "Student profile updated successfully !\n";
            if (newProm != null && newProm != "" && !newProm.equals(student.getSchoolYear())) {
                // If changing student's school year, reset their timetable
                for (Course previousCourses : student.getTimetable()) {
                    // Resets students timetable
                    previousCourses.getStudents().remove(student);
                    // If the courses instances are not managed
                    if (!em.contains(previousCourses))
                        return "Could not sign up student to existing courses\n";

                    courseRepository.save(previousCourses);

                    serviceMessage += "Removed the previous classes from student timetable\n";
                }
                student.setSchoolYear(newProm);
                String hql = "SELECT c FROM Course c WHERE c.schoolYear = :school_year";
                TypedQuery<Course> query = em.createQuery(hql, Course.class);
                query.setParameter("school_year", student.getSchoolYear());
                try {
                    List<Course> newTimetable = query.getResultList();
                    student.setTimetable(newTimetable);
                    for (Course existingCourse: newTimetable) {
                        em.merge(existingCourse);
                        existingCourse.getStudents().add(student);
                        if (!em.contains(existingCourse))
                            return "Student update failed. Could not sign up student to existing courses during" +
                                    "student update\n";

                        serviceMessage += "Added the existing courses of the new school year\n";
                    }
                } catch (NoResultException nrex) {
                    // There are no courses having the new prom
                    return serviceMessage;
                }
                serviceMessage += "Timetable updated successfully\n";
            }
            em.getTransaction().commit();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("EntityManagerFactory initialisation failed : " + ex);
        }
        return serviceMessage;
    }

    public String changeStudentPswd(int studentId, String newPswd) {
        Student student = studentRepository.findById(studentId).get();
        student.setPswd(newPswd);
        return "Student password changed successfully !\n";
    }

    @Transactional
    public String deleteStudent(int studentId) {
        try {
            Student student = studentRepository.findById(studentId).get();
            // Remove the student from courses academic audiences
            if (!student.getTimetable().isEmpty()) {
                for (Course studentClass : student.getTimetable()) {
                    studentClass.getStudents().remove(student);
                    courseRepository.save(studentClass);
                }
            }
            studentRepository.deleteById(studentId);
        } catch (Throwable ex) {
            throw new RuntimeException("Error during student creation : " + ex);
        }
        return "Student deleted successfully !\n";
    }
}
