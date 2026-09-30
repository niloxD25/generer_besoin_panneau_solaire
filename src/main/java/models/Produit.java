package models;

import utils.*;

public class Produit extends ObjectBasic
{
    private Double prix;
    public Produit (int id, String name, String desce, Double prix)
    {
        super(id, name, desce);
        this.prix = prix;
    }

    public Produit (String name, String desce, Double prix)
    {
        super(name, desce);
        this.prix = prix;
    }

    public Produit() {}
}
