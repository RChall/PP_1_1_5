package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import javax.persistence.Query;
import javax.persistence.EntityManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            session.createSQLQuery("""
                    CREATE TABLE `mydbtest`.`users` (
                    `id` INT NOT NULL AUTO_INCREMENT,
                    `name` VARCHAR(45) NULL,
                    `lastname` VARCHAR(45) NULL,
                    `age` INT NULL,
                    PRIMARY KEY (`id`));
                                        """).executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            dropUsersTable();
            createUsersTable();

        }

    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();

            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            User user = new User(name, lastName, age);
            session.save(user);

            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных.");
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            User user = session.get(User.class, id);
            session.delete(user);

            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        //List<User> userList = new ArrayList<>();
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();

            session.createSQLQuery("TRUNCATE users").executeUpdate();

            session.getTransaction().commit();
        }
    }
}
