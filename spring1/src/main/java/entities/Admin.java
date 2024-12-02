package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "admin", schema = "cy_e_admin")
/**
 * Entity linked to the admin table.
 */
public class Admin {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "mail", nullable = false)
    private String mail;

    @Column(name = "pswd", nullable = false)
    private String pswd;

    public Admin(String firstname, String lastname, String mail, String pswd) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.pswd = pswd;
    }

    public Admin() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Admin that = (Admin) o;

        if (id != that.id) return false;
        if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
        if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
        if (mail != null ? !mail.equals(that.mail) : that.mail != null) return false;
        if (pswd != null ? !pswd.equals(that.pswd) : that.pswd != null) return false;

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

        Admin admin = (Admin) o;

        if (this.id != id) return false;
        if (firstname != null ? !firstname.equals(admin.firstname) : admin.firstname != null) return false;
        if (lastname != null ? !lastname.equals(admin.lastname) : admin.lastname != null) return false;
        if (mail != null ? !mail.equals(admin.mail) : admin.mail != null) return false;
        if (pswd != null ? !pswd.equals(admin.pswd) : admin.pswd != null) return false;

        return true;
    }

    /**
     * For debugging.
     */
    @Override
    public String toString() {
        return "Admin instance\n" +
                "id : " + id + "; firstname : " + firstname + "; lastname : " + lastname + "; mail : " + mail
                + ";\npassword : " + pswd;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (mail != null ? mail.hashCode() : 0);
        result = 31 * result + (pswd != null ? pswd.hashCode() : 0);
        return result;
    }
}
