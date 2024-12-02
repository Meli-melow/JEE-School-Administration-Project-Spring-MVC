package entityManagerSetUp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerUtil {
    private static final EntityManagerFactory emf;

    // Loading the configuration from the persistence file
    static {
        try {
            emf = Persistence.createEntityManagerFactory("default");
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("EntityManagerFactory initialisation failed : " + ex);
        }
    }

    // Provide an EntityManager instance
    public static EntityManager getEntityManager() { return emf.createEntityManager(); }

    // Close the EntityManagerFactory (call when the application is finished)
    public static void close() { emf.close(); }

}
