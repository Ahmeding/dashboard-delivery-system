package com.hitecart.deliverysystem.utils;

import com.hitecart.deliverysystem.dao.ClientRepository;
import com.hitecart.deliverysystem.dao.DriverRepository;
import com.hitecart.deliverysystem.dao.UserRepository;
import com.hitecart.deliverysystem.dao.VehicleRepository;
import com.hitecart.deliverysystem.dto.client.ClientRequest;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.dto.user.UserRequest;
import com.hitecart.deliverysystem.dto.vehicle.VehicleRequest;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.model.User;

public class CheckDuplicat {
    public static void checkDuplicatUser(User user, UserRepository userRepository) {
        checkEmailExists(user,userRepository);
        checkPhoneNbrExists(user,userRepository);

    }

    private static void checkPhoneNbrExists(User userRequest, UserRepository userRepository) {
        boolean  isExists=  userRepository.existsByPhoneNbr(userRequest.getPhoneNbr());
        if(isExists) {
            throw new InvalidOperationException(
                    "Phone Number is already in use: " + userRequest.getPhoneNbr(),
                    ErrorCodes.DRIVER_NOT_VALID_OPERATION // Use appropriate error code
            );
        }
    }

    private static void checkEmailExists(User userRequest, UserRepository userRepository) {
        Boolean  isExists=  userRepository.existsByEmailIgnoreCase(userRequest.getEmail());
        if(isExists) {
            throw new InvalidOperationException(
                    "Email address is already in use: " + userRequest.getEmail(),
                    ErrorCodes.DRIVER_NOT_VALID_OPERATION // Use appropriate error code
            );
        }
    }


    public static void checkDuplicatDriver(DriverRequest driverRequest, DriverRepository userRepository) {
        checkSecuNbrExists(driverRequest,userRepository);
        checkRibExists(driverRequest,userRepository);
        checkDrivinglicenseNbr(driverRequest,userRepository);
    }

    private static void checkDrivinglicenseNbr(DriverRequest driverRequest, DriverRepository driverRepository) {
        Boolean  isExists=  driverRepository.existsByDrivinglicenseNbr(driverRequest.drivinglicenseNbr());
        if(isExists) {
            throw new InvalidOperationException(
                    "Driving license Nbr is already in use: " + driverRequest.drivinglicenseNbr(),
                    ErrorCodes.DRIVER_NOT_VALID_OPERATION // Use appropriate error code
            );
        }
    }

    private static void checkRibExists(DriverRequest driverRequest, DriverRepository driverRepository) {
        Boolean  isExists=  driverRepository.existsBySecuNbr(driverRequest.secuNbr());
        if(isExists) {
            throw new InvalidOperationException(
                    "Security Nbr is already in use: " + driverRequest.secuNbr(),
                    ErrorCodes.DRIVER_NOT_VALID_OPERATION // Use appropriate error code
            );
        }
    }

    private static void checkSecuNbrExists(DriverRequest driverRequest, DriverRepository driverRepository) {
        Boolean  isExists=  driverRepository.existsBySecuNbr(driverRequest.secuNbr());
        if(isExists) {
            throw new InvalidOperationException(
                    "Security Nbr is already in use: " + driverRequest.secuNbr(),
                    ErrorCodes.DRIVER_NOT_VALID_OPERATION // Use appropriate error code
            );
        }
    }


    public static void checkDuplicatClient(ClientRequest clientRequest , ClientRepository userRepository) {
            checkSiretExists(clientRequest,userRepository);
            checkSirenExists(clientRequest,userRepository);
    }

    private static void checkSirenExists(ClientRequest clientRequest, ClientRepository clientRepository) {
        Boolean  isExists=  clientRepository.existsBySiren(clientRequest.siren());
        if(isExists) {
            throw new InvalidOperationException(
                    "Siren is already in use: " + clientRequest.siren(),
                    ErrorCodes.CLIENT_NOT_VALID_OPERATION
            );
        }
    }

    private static void checkSiretExists(ClientRequest clientRequest, ClientRepository clientRepository) {
        Boolean  isExists=  clientRepository.existsBySiret(clientRequest.siret());
        if(isExists) {
            throw new InvalidOperationException(
                    "Siret is already in use: " + clientRequest.siret(),
                    ErrorCodes.CLIENT_NOT_VALID_OPERATION
            );
        }
    }

    public static void checkVehicle(VehicleRequest vehicleRequest, VehicleRepository vehicleRepository) {
        checkRegisterNbrExists(vehicleRequest,vehicleRepository);
        checkVinNbrExists(vehicleRequest,vehicleRepository);
    }

    private static void checkVinNbrExists(VehicleRequest vehicleRequest, VehicleRepository vehicleRepository) {
        Boolean  isExists= vehicleRepository.existsByVinNbr(vehicleRequest.vinNbr());
        if(isExists) {
            throw new InvalidOperationException(
                    "Vin Nbr is already in use: " + vehicleRequest.vinNbr(),
                    ErrorCodes.VEHICLE_NOT_VALID_OPERATION
            );
        }
    }

    private static void checkRegisterNbrExists(VehicleRequest vehicleRequest, VehicleRepository vehicleRepository) {
        Boolean  isExists=  vehicleRepository.existsByRegistrationNbr(vehicleRequest.registrationNbr());
        if(isExists) {
            throw new InvalidOperationException(
                    "Registration Nbr is already in use: " + vehicleRequest.registrationNbr(),
                    ErrorCodes.VEHICLE_NOT_VALID_OPERATION
            );
        }
    }
}
