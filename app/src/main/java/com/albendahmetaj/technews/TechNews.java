package com.albendahmetaj.technews;

public class TechNews
{
    private String titulli, pershkrimi, fotoUrl, emri;
    public TechNews(String titulli, String pershkrimi, String fotoUrl, String emri)
    {
        this.titulli = titulli;
        this.pershkrimi = pershkrimi;
        this.fotoUrl = fotoUrl;
        this.emri = emri;
    }

    public TechNews()
    {

    }

    public String getTitulli() {
        return titulli;
    }

    public void setTitulli(String titulli) {
        this.titulli = titulli;
    }

    public String getPershkrimi() {
        return pershkrimi;
    }

    public void setPershkrimi(String pershkrimi) {
        this.pershkrimi = pershkrimi;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }
}
