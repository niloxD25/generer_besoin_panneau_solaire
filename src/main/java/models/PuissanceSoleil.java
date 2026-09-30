package models;

import utils.ObjectBasic;

import java.util.List;

public class PuissanceSoleil extends ObjectBasic
{
    private String idSaison;
    private int heureDebut;
    private int heureFin;
    private Double pourcentage;

    public PuissanceSoleil(int i, String idSaison, int heureDebut, int heureFin, Double pourcentage) 
    {
        super(i);
        this.idSaison = idSaison;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.pourcentage = pourcentage;
    }

    public PuissanceSoleil() {}

    public int comparerIntervalles(int debut1, int fin1, int debut2, int fin2) 
    {
        int largeur1 = (debut1 <= fin1) ? (fin1 - debut1) : (24 - debut1 + fin1);
        
        int largeur2 = (debut2 <= fin2) ? (fin2 - debut2) : (24 - debut2 + fin2);

        if (largeur1 > largeur2) 
        {
            return 1;
        } 
        else 
        {
            return 2;
        }
    }

    public Double calculConsoInterval(List<MaterielDevis> listeMateriauxDevis)
    {
        Double reponse = 0.0;

        for (MaterielDevis materielDevis : listeMateriauxDevis) 
        {
            int who = comparerIntervalles(materielDevis.getHeureDebutUtilisation(), materielDevis.getHeureFinUtilisation(), heureDebut, heureFin);
            if (who == 1) 
            {
                if (materielDevis.getHeureDebutUtilisation() <= this.heureDebut && materielDevis.getHeureFinUtilisation() >= this.heureDebut ||
                    materielDevis.getHeureDebutUtilisation() <= this.heureFin && materielDevis.getHeureFinUtilisation() >= this.heureFin) 
                {
                    reponse += materielDevis.getPuissance();
                }
            }
            else
            {
                if (this.heureDebut <= materielDevis.getHeureDebutUtilisation() && this.heureFin >= materielDevis.getHeureDebutUtilisation() ||
                    this.heureDebut <= materielDevis.getHeureFinUtilisation() && this.heureFin >= materielDevis.getHeureFinUtilisation()) 
                {
                    reponse += materielDevis.getPuissance();
                }
            }
        }
        // System.out.println(reponse);
        return reponse;
    }

    public Double calculMaxPuissance(List<MaterielDevis> listeMateriauxDevis)
    {
        Double reponse = 0.0;

        reponse = (calculConsoInterval(listeMateriauxDevis) * 100) / pourcentage;
        reponse = Math.round(reponse * 100.0) / 100.0;

        return reponse * 2;
    }
}
