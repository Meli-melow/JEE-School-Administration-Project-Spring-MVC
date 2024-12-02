package repositories;

import entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    @Query("SELECT a FROM Admin a WHERE a.mail = :mail AND a.pswd = :pswd")
    /***/
    Admin findAdminByMailAndPswd(String mail, String pswd);

    @Query("SELECT a FROM Admin a WHERE a.mail = :mail")
    /***/
    Admin findAdminByMail(String mail);


}
