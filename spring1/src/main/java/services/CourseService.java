package services;

import entities.Course;
import entities.Student;
import entities.Teacher;
import entityManagerSetUp.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.CourseRepository;
import repositories.StudentRepository;
import repositories.TeacherRepository;

import java.sql.Date;
import java.util.List;

@Service
public class CourseService {

    // All autowired components will be injected to the CourseService during the app configuration

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public String createCourse(Date courseDay, String courseHour, String courseDuration, String courseSchoolYear,
    String teacherMail)
    {
        String serviceMessage = "Course created successfully !\n";
        try {
            Teacher teacher = teacherRepository.findTeacherByMail(teacherMail);
            if (teacher != null) {
                // Attach again the teacher instance to the persistence context
                Course course = new Course(teacher.getField(), courseDay, courseHour, courseDuration, courseSchoolYear, teacher);
                teacher.getTimetable().add(course);
                course.setTeacher(teacher);
                // Add the course to the student having the corresponding school year
                String hql = "SELECT s FROM Student s WHERE s.schoolYear = :school_year";
                // Dynamic HQL request to handle the existing students
                EntityManager em = EntityManagerUtil.getEntityManager();
                TypedQuery<Student> query = em.createQuery(hql, Student.class);
                query.setParameter("school_year", courseSchoolYear);
                try {
                    List<Student> classStudents = query.getResultList();
                    course.setStudents(classStudents);
                    for (Student classStudent : course.getStudents()) {
                        classStudent.getTimetable().add(course);
                        // If the student instance is not managed
                        if (!em.contains(classStudent))
                            return "Course creation failed. Could not sign up existing students to the course during course creation\n";
                    }
                    // Persist updates in the database
                    studentRepository.saveAll(classStudents);
                } catch (NoResultException nrex) {
                    // There are no students in the given school year
                    courseRepository.save(course);
                    return serviceMessage;
                }
                courseRepository.save(course);
                serviceMessage += "Students were automatically signed up\n";
                // Force Hibernate to synchronise changes
                em.flush();
            } else {
                return "Course creation failed. Please make sure teacher mail exists\n";
            }
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("EntityManagerFactory initialisation failed");
        }
        return serviceMessage;
    }

    public Course getCourseById(int courseId) {
        // Return the instance associated to the Optional instance
        return courseRepository.findById(courseId).orElse(null);
    }

    @Transactional
    public String patchCourse(int courseId, Date newCourseDay, String newCourseHour,
    String newCourseDuration, String newCourseSchoolYear, String newTeacherMail) {

        String serviceMessage = "";
        try {
            // Use entity manager to make sure entities instances are managed
            EntityManager em = EntityManagerUtil.getEntityManager();
            // Use the instance associated to the Optional instance
            Course course = courseRepository.findById(courseId).get();
            // Update information related to the new teacher
            if (newTeacherMail != null && newTeacherMail != "") {
                Teacher newTeacher = teacherRepository.findTeacherByMail(newTeacherMail);
                // Update both new and previous teacher instances
                Teacher previousTeacher = teacherRepository.findById(course.getTeacher().getId()).get();
                previousTeacher.getTimetable().remove(course);
                newTeacher.getTimetable().add(course);
                course.setTeacher(newTeacher);
                // If none of the teacher instances are managed
                if (!em.contains(newTeacher) || !em.contains(course.getTeacher()))
                    return "Course update failed. Could not change course teacher\n";

                // Change course field to the field of the new teacher
                if (!course.getField().equals(newTeacher.getField()))
                    course.setField(newTeacher.getField());

                teacherRepository.save(previousTeacher);
                teacherRepository.save(newTeacher);
                serviceMessage += "Updated course teacher.";
            }
            if (!newCourseDay.equals(java.sql.Date.valueOf("1900-01-01")) && newCourseDay != course.getDay())
                course.setDay(newCourseDay);

            if (newCourseHour != null && newCourseHour != "" && !newCourseHour.equals(course.getHour()))
                course.setHour(newCourseHour);

            if (newCourseDuration != null && newCourseDuration != "" && !newCourseDuration.equals(course.getDuration()))
                course.setDuration(newCourseDuration);

            serviceMessage = "Course updated successfully ! " + serviceMessage;
            if (newCourseSchoolYear != null && newCourseSchoolYear != "" && !newCourseSchoolYear.equals(course.getSchoolYear())) {
                if (!course.getStudents().isEmpty()) {
                    // Remove students from the former prom
                    for (Student formerStudent : course.getStudents()) {
                        // Manage changes brought to students from the former prom
                        course.getStudents().remove(formerStudent);
                        formerStudent.getTimetable().remove(course);
                        // If students instances are not managed
                        if (!em.contains(formerStudent))
                            return "Could not propagate modifications correctly. Could not unsubscribe up previous students" +
                                    "to course\n";

                        studentRepository.save(formerStudent);
                    }
                    serviceMessage += " Removed students from the previous school year.";
                }
                // Add the course to the student having the corresponding school year
                String hql = "SELECT s FROM Student s WHERE s.schoolYear = :school_year";
                TypedQuery<Student> query = em.createQuery(hql, Student.class);
                query.setParameter("school_year", newCourseSchoolYear);
                try {
                    List<Student> classStudents = query.getResultList();
                    course.setStudents(classStudents);
                    for (Student classStudent : classStudents) {
                        // Manage changes brought to student instances
                        em.merge(classStudent);
                        classStudent.getTimetable().add(course);
                        // If students instances are not managed
                        if (!em.contains(classStudent))
                            return "Course update failed. Students with the same school year new could not" +
                                    "be signed up to course\n";

                        studentRepository.save(classStudent);
                    }
                } catch (NoResultException nrex) {
                    // There are no students having the new school year
                    return serviceMessage;
                }
                course.setSchoolYear(newCourseSchoolYear);
                serviceMessage += " Students from the new school year were automatically signed up\n";
            }
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("EntityManagerFactory initialisation failed : " + ex);
        }
        return serviceMessage;
    }

    @Transactional
    public String deleteCourseById(int courseId) {

        try {
            // Use entity manager to make sure entities instances are managed
            EntityManager em = EntityManagerUtil.getEntityManager();
            Course course = courseRepository.findById(courseId).get();
            Teacher teacher = course.getTeacher();
            // Retrieve the course from its teacher's timetable
            teacher.getTimetable().remove(course);
            // If the teacher instance is not managed
            if (!em.contains(teacher))
                return "Could not remove course from teacher timetable\n";

            // Remove the course from students' timetable
            if (!course.getStudents().isEmpty()){
                for (Student courseStudents : course.getStudents()) {
                    courseStudents.getTimetable().remove(course);
                    studentRepository.save(courseStudents);
                }
            }
            courseRepository.delete(course);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("EntityManagerFactory initialisation failed : " + ex);
        }
        return "Course deleted successfully !\n";
    }
}
