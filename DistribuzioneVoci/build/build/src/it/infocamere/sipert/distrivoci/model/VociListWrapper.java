package it.infocamere.sipert.distrivoci.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "voci")
public class VociListWrapper {
	
    private List<Voce> voci;

    @XmlElement(name = "voce")
    public List<Voce> getVoci() {
        return voci;
    }

    public void setVoci(List<Voce> voci) {
        this.voci = voci;
    }

}
