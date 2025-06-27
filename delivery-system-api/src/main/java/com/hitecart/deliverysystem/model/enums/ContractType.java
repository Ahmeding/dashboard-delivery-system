package com.hitecart.deliverysystem.model.enums;

public enum ContractType {
    CDD("CDD"),CDI("CDI");

    String type;

    ContractType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s",type);
    }
}
