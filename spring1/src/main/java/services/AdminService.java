package services;

import entities.Admin;
import entities.Course;
import entities.Student;
import entities.Teacher;
import entityManagerSetUp.EntityManagerUtil;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.AdminRepository;
import repositories.CourseRepository;
import repositories.StudentRepository;
import repositories.TeacherRepository;

import java.util.Date;
import java.util.List;

@Service
public class AdminService {

    // All autowired components will be injected to the AdminService during the app configuration

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public AdminService(AdminRepository adminRepository, TeacherRepository teacherRepository, CourseRepository courseRepository, StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public String createAdmin(Admin admin) {
        try {
            adminRepository.save(admin);
        } catch (Exception ex) {
            throw new RuntimeException("Error during admin creation : " + ex);
        }
        return "Admin created successfully !\n";
    }

    public Admin adminLogin(String adminMail, String pswd) {

        Admin logedInAdmin;
        try {
            logedInAdmin = adminRepository.findAdminByMailAndPswd(adminMail, pswd);
        } catch (Throwable ex) {
            throw new RuntimeException("Error during admin creation : " + ex);
        }
        return logedInAdmin;
    }

    public Admin getAdminById(int adminId) {
        // Return the instance associated to the Optional instance
        return adminRepository.findById(adminId).orElse(null);
    }

    public Admin getAdminByMail(String adminMail) {
        return adminRepository.findAdminByMail(adminMail);
    }

    public String patchAdminProfile(int adminId, String newFirstname, String newLastname, String newMail) {

        try {
            // Use the instance associated to the Optional instance
            Admin admin = adminRepository.findById(adminId).get();
            if (newFirstname != null && newFirstname != "" && !newFirstname.equals(admin.getFirstname()))
                admin.setFirstname(newFirstname);

            if (newLastname != null && newLastname != "" && !newLastname.equals(admin.getLastname()))
                admin.setLastname(newLastname);

            if (newMail != null && newMail != "" && !newMail.equals(admin.getMail()))
                admin.setMail(newMail);

        } catch (Throwable ex) {
            throw new RuntimeException("Error during admin creation : " + ex);
        }
        return "Admin profile updated successfully !\n";
    }

    public String changeAdminPswd(int adminId, String newPswd) {
        Admin admin = adminRepository.findById(adminId).get();
        admin.setPswd(newPswd);
        return "Admin password changed successfully !\n";
    }

    public String deleteAdmin(int adminId) {
        try {
            adminRepository.deleteById(adminId);
        } catch (Throwable ex) {
            throw new RuntimeException("Error during admin creation : " + ex);
        }
        return "Admin deleted successfully !\n";
    }

    public List<Admin> getAllAdmins() { return adminRepository.findAll(); }

    public List<Teacher> getAllTeachers() { return teacherRepository.findAll(); }

    public List<Teacher> getAllTeachers(String field) { return teacherRepository.findTeachersByField(field); }

    public List<Course> getAllCourses() { return courseRepository.findAll(); }

    public List<Course> getAllCourses(Date day, String hour, String schoolYear, String field, String duration) {

        String hql = "SELECT c FROM Course c WHERE c.schoolYear = :school_year";
        if (hour != null && !hour.equals(""))
            hql += " AND c.hour = :hour";
        if (day != null && !day.equals(""))
            hql += " AND c.day = :day";
        if (field != null && !field.equals(""))
            hql += " AND c.field = :field";
        if (duration != null && !duration.equals(""))
            hql += " AND c.duration = :duration";

        EntityManager em = EntityManagerUtil.getEntityManager();
        return em.createQuery(hql)
                .setParameter("day", day)
                .setParameter("hour", hour)
                .setParameter("field", field)
                .setParameter("duration", duration)
                .getResultList();
    }

    public List<Student> getAllStudents() { return studentRepository.findAll(); }

    public List<Student> getAllStudents(String schoolYear) { return studentRepository.findStudentsBySchoolYear(schoolYear); }
}
