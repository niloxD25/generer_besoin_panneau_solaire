package models;

import utils.ObjectBasic;

public class ConfirmationDevis extends ObjectBasic
{
    private int idDevis;
    private String adresse;
    private String nom;

    public ConfirmationDevis(int idDevis, String adresse, String nom)
    {
        this.idDevis = idDevis;
        this.adresse = adresse;
        this.nom = nom;
    }

    public ConfirmationDevis() {}
}
