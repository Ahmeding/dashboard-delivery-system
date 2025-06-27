package com.hitecart.deliverysystem.utils;

import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.validator.DriverValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;

@Slf4j
public class CheckParmasUtil {
    public static void checkID(Integer id, String msgLog, String msgException, ErrorCodes erroCode) {
        if (id == null) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    erroCode);
        }
    }

    public static void checkDate(Instant dateParam, String msgLog, String msgException, ErrorCodes erroCode) {
        if (dateParam == null) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    erroCode);
        }
    }

    public static void checkStringParma(String parmaString, String msgLog, String msgException, ErrorCodes errorCode) {
        if (!StringUtils.hasLength(parmaString)) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    errorCode);
        }
    }


    public static void checkDto(DriverRequest dto, String msgLog, String msgException) {
        List<String> errors = DriverValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.DRIVER_NOT_VALID,errors);
        }
    }

    public static void checkCode(String code, String msgLog, String msgException, ErrorCodes errorCode) {
        if (!StringUtils.hasLength(code)) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    errorCode);
        }
    }

    public static void checkDouble(double kmDebut, String msgLog, String msgException, ErrorCodes errorCodes) {
        if (kmDebut==0) {
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    errorCodes);
        }

    }

    public static void checkDoubleGTZero(double kmDebut, String msgLog, String msgException, ErrorCodes errorCodes) {
        if(kmDebut == 0){
            log.error(msgLog);
            throw new InvalidOperationException(msgException,
                    errorCodes);
        }
    }
}
