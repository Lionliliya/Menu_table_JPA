package menupackage;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by lionliliya on 17.09.15.
 */
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("MenuJPA");
        EntityManager entityManager = factory.createEntityManager();

        try {
            Meal meal = null;

            String name = "";
            double price = 0.0, weight = 0.0;
            String promot = "";
            //add new meals
            for (int i = 0; i < 7; i++) {
                name = getRandomName(i);
                price = randomPrice();
                weight = getRandomWeight();
                promot = randomPromotion();
                meal = new Meal(name, price, weight, promot);

                addMeal(meal, entityManager);
            }
            //find by price
            List <Meal> list = findByPriceCriteria(50, 80, entityManager);
            System.out.println("All meals between 50 and 80 UAH");
            System.out.println("-------------------------------");
            for (Meal m : list) {
                System.out.println("Id: "+m.getId() + ", Name: " + m.getMealName() +
                        ", Price: " + m.getPrice() + ", Discount: " + m.getPromotion() + ", Weight: " + m.getWeight()+" g.");
            }

            //only with discount
            List<Meal> discountList = withDiscount(entityManager);
            System.out.println("All meals with discount");
            System.out.println("-----------------------");
            for (Meal m : discountList) {
                System.out.println("Id: "+m.getId() + ", Name: " + m.getMealName() +
                        ", Price: " + m.getPrice() + ", Discount: " + m.getPromotion() + ", Weight: " + m.getWeight()+" g.");
            }

            //under 800 gr.
            List <Meal> meals = under(800, entityManager);
            System.out.println("All meals less then 800 gr");
            System.out.println("-----------------------");
            for (Meal m : meals) {
                System.out.println("Id: "+m.getId() + ", Name: " + m.getMealName() +
                        ", Price: " + m.getPrice() + ", Discount: " + m.getPromotion() + ", Weight: " + m.getWeight()+" g.");
            }



        } finally {
            entityManager.close();
            factory.close();
        }
    }

    // adding one more Meal to the Menu
    static public void addMeal(Meal a, EntityManager manager) {
        try {
            manager.getTransaction().begin();
            manager.persist(a);
            manager.getTransaction().commit();
        } catch (Exception ex) {
            manager.getTransaction().rollback();
            ex.printStackTrace();
        }
    }

    // selection by the criteria of "price from-to"
    static public List<Meal> findByPriceCriteria(double priceLow, double priceHigh, EntityManager manager) {
        try {
            Query query = manager.createQuery("SELECT m FROM Meal m " +
                    "WHERE m.price <:priceMax and m.price >:priceMin", Meal.class);
            query.setParameter("priceMax", priceHigh);
            query.setParameter("priceMin", priceLow);
            List<Meal> list = (List<Meal>) query.getResultList();
            return list;
        } catch (NoResultException ex) {
            System.out.println(">>>Not found!");
            ex.printStackTrace();
            return null;
        } catch (NonUniqueResultException e) {
            System.out.println(">>>No unique result is found!");
            return null;
        }
    }

    //Only with discount
    static public List<Meal> withDiscount(EntityManager manager) {
        try {
            Query query = manager.createQuery("SELECT m FROM Meal m " +
                "WHERE m.promotion =:disc OR m.promotion =:disc2", Meal.class);
            query.setParameter("disc", "15%");
            query.setParameter("disc2", "30%");
            List<Meal> list2 = (List<Meal>) query.getResultList();
            return list2;
            } catch (NoResultException ex) {
                System.out.println(">>>Not found!");
                ex.printStackTrace();
                return null;
            } catch (NonUniqueResultException e) {
                System.out.println(">>>No unique result is found!");
                return null;
            }
    }

    //select a set of meals so that their total weight was not exceeding 1 kg.
    static public List<Meal> under(double weight, EntityManager manager) {
        try {
            Query query = manager.createQuery("SELECT m FROM Meal m ", Meal.class);

            List<Meal> list = query.getResultList();
            List<Meal> finish = new ArrayList<>();
            double sumWeight = 0;

            for (Meal m : list) {
                sumWeight += m.getWeight();
                if (sumWeight <= weight) {
                    finish.add(m);

                }

            }
            return finish;
        } catch (NoResultException ex) {
            System.out.println(">>>Not found!");
            ex.printStackTrace();
            return null;
        } catch (NonUniqueResultException e) {
            System.out.println(">>>No unique result is found!");
            return null;
        }
    }

    static final String[] Names = {"Cesar Salad", "Greek Salad", "Green Salad", "Tomato Soup", "Borshch",
            "Grilled Dorado", "Smokey Beef", "AntiPasty"};

    static final String[] Promotions = {"15%", "30%", "no"};

    static final Random rnd = new Random();

    static public String getRandomName(int i) {
        return Names[i];
    }

    static public double getRandomWeight() {
        double w = 0;
        w = (300 - 150) * rnd.nextDouble() + 150;
        w = new BigDecimal(w).setScale(2, RoundingMode.UP).doubleValue();
        return w;
    }

    static public double randomPrice() {
        double p = 0;
        p = (95 - 40) * rnd.nextDouble() + 40;
        p = new BigDecimal(p).setScale(2, RoundingMode.UP).doubleValue();
        return p;
    }

    static public String randomPromotion() {
        return Promotions[rnd.nextInt(Promotions.length)];
    }
}
