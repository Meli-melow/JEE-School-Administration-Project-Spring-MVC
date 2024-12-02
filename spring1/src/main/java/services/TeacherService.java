package services;

import entities.Teacher;
import entities.Course;
import entities.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.CourseRepository;
import repositories.StudentRepository;
import repositories.TeacherRepository;

@Service
public class TeacherService {

    // All autowired components will be injected to the TeacherService during the app configuration

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public TeacherService(TeacherRepository teacherRepository, CourseRepository courseRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public String createTeacher(Teacher newTeacher) {
        try {
            teacherRepository.save(newTeacher);
        } catch (Exception ex) {
            throw new RuntimeException("Error during teacher creation : " + ex);
        }
        return "Teacher created successfully !\n";
    }

    public Teacher teacherLogin(String teacherMail, String pswd) {

        Teacher logedInTeacher;
        try {
            logedInTeacher = teacherRepository.findTeacherByMailAndPswd(teacherMail, pswd);
        } catch (Throwable ex) {
            throw new RuntimeException("Error during teacher creation : " + ex);
        }
        return logedInTeacher;
    }

    public Teacher getTeacherById(int teacherId) {

        // Return the instance associated to the Optional instance
        return teacherRepository.findById(teacherId).orElse(null);
    }

    public Teacher getTeacherByMail(String teacherMail) {
        return teacherRepository.findTeacherByMail(teacherMail);
    }

    @Transactional
    public String patchTeacherProfile(int teacherId, String newFirstname, String newLastname, String newMail, String newField) {

        String serviceMessage;
        try { // Use the instance associated to the Optional instance
            Teacher teacher = teacherRepository.findById(teacherId).get();
            if (newFirstname != null && newFirstname != "" && !newFirstname.equals(teacher.getFirstname()))
                teacher.setFirstname(newFirstname);

            if (newLastname != null && newLastname != "" && !newLastname.equals(teacher.getLastname()))
                teacher.setLastname(newLastname);

            if (newMail != null && newMail != "" && !newMail.equals(teacher.getMail()))
                teacher.setMail(newMail);

            serviceMessage = "Teacher profile updated successfully !\n";
            if (newField != null && newField != "" && !newField.equals(teacher.getField())) {
                // Erase teacher's classes
                for (Course teacherClass : teacher.getTimetable()) {
                    // Remove the courses since they no longer have a teacher
                    courseRepository.delete(teacherClass);
                }
                teacher.setField(newField);
                serviceMessage += "Teacher timetable successfully reset\n";
            }
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("EntityManagerFactory initialisation failed : " + ex);
        }
        return serviceMessage;
    }

    public String changeTeacherPswd(int teacherId, String newPswd) {
        Teacher teacher = teacherRepository.findById(teacherId).get();
        teacher.setPswd(newPswd);
        return "Teacher password changed successfully !\n";
    }

    @Transactional
    public String deleteTeacher(int teacherId) {
        try {
            Teacher teacher = teacherRepository.findById(teacherId).get();
            // Remove teacher's classes
            if (!teacher.getTimetable().isEmpty())
                courseRepository.deleteAllInBatch(teacher.getTimetable());

            teacherRepository.deleteById(teacherId);
        } catch (Throwable ex) {
            throw new RuntimeException("Error during teacher creation : " + ex);
        }
        return "Teacher deleted successfully !\n";
    }

}
