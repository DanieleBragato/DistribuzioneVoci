package it.infocamere.sipert.distrivoci.util;

import javafx.beans.property.SimpleStringProperty;

public class DistributionStep {

    private final SimpleStringProperty name;
    private final SimpleStringProperty step;

    public DistributionStep(String name, String step) {
        this.name = new SimpleStringProperty(name);
        this.step = new SimpleStringProperty(step);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }

    public String getStep() {
        return step.get();
    }

    public void setStep(String fStep) {
        name.set(fStep);
    }
}
