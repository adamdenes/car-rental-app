package hu.inf.unideb;

import model.Car;
import model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CarDao {
    public void addCar(Car car) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.persist(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateCar(Car car) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.merge(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteCar(Car car) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.remove(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Car getCarById(Long id) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        Car car = null;

        try {
            transaction = session.beginTransaction();
            car = session.get(Car.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return car;
    }
}
