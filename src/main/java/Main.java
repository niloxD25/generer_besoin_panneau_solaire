import utils.*;

import java.util.List;

import models.*;

public class Main 
{
    public static void main(String[] args) throws Exception
    {
        Jdbc jdbc = new Jdbc();

        //Test Gestion
        // Gestion ge = new Gestion();
        // List<Produit> produits = ge.getObjectsFromTable("produits", Produit.class);

        // for (Produit produit : produits) 
        // {
        //     System.out.println("nom: " + produit.getName());
        // }

        //Test ObjectBasic
        // Produit pro = new Produit("sucreGlace", "mamyKely", 49.7);

        // pro.insert(jdbc, "produits");

        Devis devis = new Devis(3);

        devis.setListeMateriauxDevisFromBase();

        System.out.println(devis.calculPuissancePanneau());
    }
}
