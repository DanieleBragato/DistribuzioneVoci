package it.infocamere.sipert.distrivoci.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "distribuzioni")
public class StoricoDistribuzioniListWrapper {

    private List<StoricoDistribuzione> distribuzioni;
	
    @XmlElement(name = "distribuzione")
    public List<StoricoDistribuzione> getDistribuzioni() {
        return distribuzioni;
    }

    public void setDistribuzioni(List<StoricoDistribuzione> distribuzioni) {
        this.distribuzioni = distribuzioni;
    }
    
}
