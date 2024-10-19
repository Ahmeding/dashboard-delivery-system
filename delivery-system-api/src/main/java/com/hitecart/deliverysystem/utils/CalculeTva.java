package com.hitecart.deliverysystem.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculeTva {

    public static BigDecimal calculeTTC(BigDecimal prixUnitairHt, BigDecimal tva){
        BigDecimal factor = BigDecimal.ONE.add(tva.divide(BigDecimal.valueOf(100)));
        return prixUnitairHt.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
