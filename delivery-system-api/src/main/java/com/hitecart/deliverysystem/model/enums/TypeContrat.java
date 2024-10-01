package com.hitecart.deliverysystem.model.enums;

public enum TypeContrat {
    CDD("CDD"),CDI("CDI");

    String type;

    TypeContrat(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s",type);
    }
}
