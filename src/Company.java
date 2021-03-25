public class Company {

    private String name;
    private float totalNumberOfShares;
    private float availableNumberOfShares;
    private float price;

    public Company(String name, float totalNumberOfShares, float availableNumberOfShares, float price) {
        this.name = name;
        this.totalNumberOfShares = totalNumberOfShares;
        this.availableNumberOfShares = availableNumberOfShares;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTotalShares() {
        return totalNumberOfShares;
    }

    public void setTotalShares(float number) {
        this.totalNumberOfShares = number;
    }

    public float getAvailableShares() {
        return availableNumberOfShares;
    }

    public void setAvailableShares(float availableNumberOfShares) {
        this.availableNumberOfShares = availableNumberOfShares;
    }

    //set liquidity of company when shares are exchanged
    public synchronized void setLiquidity(float shares) {
        if (getAvailableShares() > 0) { //perhaps make validate when available shares are greater than total shares
            setAvailableShares(getAvailableShares() + shares);
        } else {
            System.out.println("No available shares in the company: " + getName());
        }

    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
