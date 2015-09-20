package menupackage;

import javax.persistence.*;

/**
 * Created by lionliliya on 17.09.15.
 */

@Entity
@Table(name="Menu")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="MealName", nullable = false)
    private String MealName;
    @Column(precision=2, scale = 2, name="Price", nullable = false)
    private double price;
    @Column(precision=2, name="Weight", nullable = false)
    private double weight;
    @Column(name="Promotion")
    private String promotion;

    public Meal() {}

    public Meal(String mealName, double price, double weight,String promotion) {
        MealName = mealName;
        this.price = price;
        this.weight = weight;
        this.promotion = promotion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMealName() {
        return MealName;
    }

    public void setMealName(String mealName) {
        MealName = mealName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }
}
