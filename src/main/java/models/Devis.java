package models;

import java.sql.ResultSet;
import java.util.List;

import utils.Gestion;
import utils.Jdbc;
import utils.ObjectBasic;

public class Devis extends ObjectBasic
{
    private String estConfirme = "N";
    private String adresse;
    private String nom;
    private List<MaterielDevis> listeMateriauxDevis;

    private Double consoTotalJournee = 0.0;
    private Double consoTotalSoiree = 0.0;

    private Double reste = 0.0;

    public Devis (int id, String estConfirme, String adresse, String nom)
    {
        super(id);
        this.estConfirme = estConfirme;
        this.adresse = adresse;
        this.nom = nom;
    }

    public Devis (int id)
    {
        super(id);
    }

    public Devis () {}

    public Devis (String estConfirme) { this.estConfirme = estConfirme; }

    public String getEstConfirme() 
    {
        return estConfirme;
    }
    public void setEstConfirme(String estConfirme) 
    {
        this.estConfirme = estConfirme;
    }
    public String getAdresse() 
    {
        return adresse;
    }
    public void setAdresse(String adresse) 
    {
        this.adresse = adresse;
    }
    public String getNom() 
    {
        return nom;
    }
    public void setNom(String nom) 
    {
        this.nom = nom;
    }
    public List<MaterielDevis> getListeMateriauxDevis() 
    {
        return listeMateriauxDevis;
    }
    public void setListeMateriauxDevis(List<MaterielDevis> listeMateriauxDevis) 
    {
        this.listeMateriauxDevis = listeMateriauxDevis;
    }

    public Double getConsoTotalJournee() 
    {
        return consoTotalJournee;
    }

    public Double getConsoTotalSoiree() 
    {
        return consoTotalSoiree;
    }
    
    //checkpoint
    public Double getReste() {
        return reste;
    }

    public void setListeMateriauxDevisFromBase() 
    {
        Gestion ge = new Gestion();
        this.listeMateriauxDevis = ge.getObjectsFromTable("listeMateriauxDevis", MaterielDevis.class, "idDevis = " + this.getId());
    }
    
    public void calculTotalConso()
    {
        for (MaterielDevis materielDevis : listeMateriauxDevis) 
        {
            materielDevis.calculConsommationTotal();
            this.consoTotalJournee += materielDevis.getConsommationMatin();
            this.consoTotalSoiree += materielDevis.getConsommationSoir();
        }
    }

    public Double getPrixParWPanneau() throws Exception
    {
        String request="SELECT prixParW FROM panneaux";
        Jdbc base=new Jdbc();
        ResultSet ans=null;
        Double resultat = 0.0;
        try {
            ans=base.executeRequest(request);

            if (ans.next()) 
            {
                resultat = ans.getDouble("prixParW");
            } 
            else 
            {
                throw new Exception("Aucune valeur trouvee");
            }
            
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            throw e;
        }
        finally {
            base.disconnect();
        }
        return resultat;
    }

    public Double calculPuissancePanneau()
    {
        Double reponse = 0.0;

        Gestion ge = new Gestion();
        List<PuissanceSoleil> listePuissanceSoleils = ge.getObjectsFromTable("puissanceSoleil", PuissanceSoleil.class, "pourcentage > 0");

        for (PuissanceSoleil puissanceSoleil : listePuissanceSoleils) 
        {
            Double puissance = puissanceSoleil.calculMaxPuissance(listeMateriauxDevis);
            
            if (puissance > reponse) 
            {
                reponse = puissance;
                System.out.println(reponse);
            }
        }

        this.reste = 0.0;
        for (PuissanceSoleil puissanceSoleil : listePuissanceSoleils) 
        {
            this.reste += (reponse - puissanceSoleil.calculMaxPuissance(listeMateriauxDevis)) / 2;
        }

        return reponse;
    }

    public Double calculPrixPanneau() throws Exception
    {
        Double prixParW = this.getPrixParWPanneau();
        Double puissancePanneau = this.calculPuissancePanneau();

        return prixParW * puissancePanneau;
    }

    public List<Batterie> calculPuissancePrixParBatterie()
    {
        Gestion ge = new Gestion();
        List<Batterie> listeBatteries = ge.getObjectsFromTable("typeBatterie", Batterie.class, null);

        for (Batterie batterie : listeBatteries) 
        {
            batterie.checkCapaciteNPrix(this.consoTotalSoiree);
        }

        return listeBatteries;
    }

}