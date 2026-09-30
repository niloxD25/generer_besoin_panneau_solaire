package models;

import utils.ObjectBasic;

public class Product extends ObjectBasic
{
    private Double price;
    private String imagePath;

    public Product (String name, String decse, Double price, String img) 
    {
        super(name, decse);
        this.price = price;
        this.imagePath = img;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
