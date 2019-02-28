package it.infocamere.sipert.distrivoci.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "tabelle")
public class TabelleListWrapper {
	
    private List<Tabella> tabelle;

    @XmlElement(name = "tabella")
    public List<Tabella> getTabelle() {
        return tabelle;
    }

    public void setTabelle(List<Tabella> tabelle) {
        this.tabelle = tabelle;
    }

}
