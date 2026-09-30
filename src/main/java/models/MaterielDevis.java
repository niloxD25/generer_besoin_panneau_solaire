package models;

import utils.ObjectBasic;

public class MaterielDevis extends ObjectBasic
{
    private int idDevis;
    private String materiel;
    private Double puissance;
    private int heureDebutUtilisation;
    private int heureFinUtilisation;

    private Double utilisationMatin = 0.0;
    private Double utilisationSoir = 0.0;

    private Double consommationMatin = 0.0;
    private Double consommationSoir = 0.0;

    public MaterielDevis (int idDevis, String materiel, Double puissance, int heureDebutUtilisation, int heureFinUtilisation)
    {
        this.idDevis = idDevis;
        this.materiel = materiel;
        this.puissance = puissance;
        this.heureDebutUtilisation = heureDebutUtilisation;
        this.heureFinUtilisation = heureFinUtilisation;
    }

    public MaterielDevis () {}

    public int getIdDevis() {
        return idDevis;
    }

    public void setIdDevis(int idDevis) 
    {
        this.idDevis = idDevis;
    }

    public String getMateriel() 
    {
        return materiel;
    }

    public void setMateriel(String materiel) 
    {
        this.materiel = materiel;
    }

    public Double getPuissance() 
    {
        return puissance;
    }

    public void setPuissance(Double puissance) 
    {
        this.puissance = puissance;
    }

    public int getHeureDebutUtilisation() 
    {
        return heureDebutUtilisation;
    }

    public void setHeureDebutUtilisation(int heureDebutUtilisation) 
    {
        this.heureDebutUtilisation = heureDebutUtilisation;
    }

    public int getHeureFinUtilisation() 
    {
        return heureFinUtilisation;
    }

    public void setHeureFinUtilisation(int heureFinUtilisation) 
    {
        this.heureFinUtilisation = heureFinUtilisation;
    }

    public Double getUtilisationMatin() {
        return utilisationMatin;
    }

    public void setUtilisationMatin(Double utilisationMatin) {
        this.utilisationMatin = utilisationMatin;
    }

    public Double getUtilisationSoir() {
        return utilisationSoir;
    }

    public void setUtilisationSoir(Double utilisationSoir) {
        this.utilisationSoir = utilisationSoir;
    }

    public Double getConsommationMatin() {
        return consommationMatin;
    }

    public void setConsommationMatin(Double consommationMatin) {
        this.consommationMatin = consommationMatin;
    }

    public Double getConsommationSoir() {
        return consommationSoir;
    }

    public void setConsommationSoir(Double consommationSoir) {
        this.consommationSoir = consommationSoir;
    }

    public void calculTempsUtilisation()
    {
        int toCheck = this.heureDebutUtilisation;

        for (int i = 1; toCheck != heureFinUtilisation; i++) 
        {
            toCheck ++;
            
            if (toCheck > 6 && toCheck < 18) 
            {
                utilisationMatin += 1;       
            }
            else
            {
                utilisationSoir += 1;
            }

            if (toCheck == 24) 
            {
                toCheck = 0;
            }
        }

    }

    public void calculConsommationTotal()
    {
        this.calculTempsUtilisation();

        consommationMatin = this.puissance * utilisationMatin;
        consommationSoir = this.puissance * utilisationSoir;
    }
}
