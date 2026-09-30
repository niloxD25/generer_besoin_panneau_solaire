package composants;

import java.io.Serializable;

public class Utilisation implements Serializable
{
    private String materiel;
    private Double puissance;
    private int heureDebut;
    private int heureFin;

    // Getters et setters pour chaque champ
    public String getMateriel() { return materiel; }
    public void setMateriel(String materiel) { this.materiel = materiel; }

    public Double getPuissance() { return puissance; }
    public void setPuissance(Double puissance) { this.puissance = puissance; }

    public int getHeureDebut() { return heureDebut; }
    public void setHeureDebut(int heureDebut) { this.heureDebut = heureDebut; }

    public int getHeureFin() { return heureFin; }
    public void setHeureFin(int heureFin) { this.heureFin = heureFin; }
}
