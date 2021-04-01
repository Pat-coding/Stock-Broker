/**
 * The company class acting as a data set for the stock exchange
 *
 * @author 1909148 Chinnapat Jongthep
 */
public class Company {

    private String name;
    private float totalNumberOfShares;
    private float availableNumberOfShares;
    private float price;

    /**
     *
     * @param name
     * @param totalNumberOfShares
     * @param availableNumberOfShares
     * @param price
     */
    public Company(String name, float totalNumberOfShares, float availableNumberOfShares, float price) {
        this.name = name;
        this.totalNumberOfShares = totalNumberOfShares;
        this.availableNumberOfShares = availableNumberOfShares;
        this.price = price;
    }

    /**
     *
     */
    public Company() {
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public float getTotalShares() {
        return totalNumberOfShares;
    }

    /**
     *
     * @param number
     */
    public void setTotalShares(float number) {
        this.totalNumberOfShares = number;
    }

    /**
     *
     * @return
     */
    public float getAvailableShares() {
        return availableNumberOfShares;
    }

    /**
     *
     * @param availableNumberOfShares
     */
    public void setAvailableShares(float availableNumberOfShares) {
        this.availableNumberOfShares = availableNumberOfShares;
    }

    //set liquidity of company when shares are exchanged

    /**
     *
     * @param shares
     */
    public synchronized void setLiquidity(float shares) {
        if (getAvailableShares() >= 0) { //perhaps make validate when available shares are greater than total shares
            setAvailableShares(getAvailableShares() + shares);
        } else {
            System.out.println("No available shares in the company: " + getName());
        }

    }

    /**
     *
     * @return
     */
    public float getPrice() {
        return price;
    }

    /**
     *
     * @param price
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
