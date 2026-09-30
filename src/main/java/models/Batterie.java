package models;

import utils.ObjectBasic;

public class Batterie extends ObjectBasic
{
    private Double vraiCapacite;
    private Double prixParW;

    private Double capaciteAFournir;
    private Double prixTotal;

    public Double getVraiCapacite() {
        return vraiCapacite;
    }

    public Double getPrixParW() {
        return prixParW;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public Double getCapaciteAFournir() {
        return capaciteAFournir;
    }

    public Batterie(int id, String name, String desce, Double vraiCapacite, Double prixParW)
    {
        super(id, name, desce);
        this.vraiCapacite = vraiCapacite;
        this.prixParW = prixParW;
    }

    public Batterie() {}

    public void checkCapaciteNPrix(Double capacite)
    {
        this.capaciteAFournir = Math.round((capacite / this.vraiCapacite) * 100.0) / 100.0;
        this.prixTotal = Math.round((this.capaciteAFournir * prixParW) * 100.0) / 100.0;
    }

}
