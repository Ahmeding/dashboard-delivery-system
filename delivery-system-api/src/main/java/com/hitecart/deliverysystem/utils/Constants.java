package com.hitecart.deliverysystem.utils;

public interface Constants {


    String DRIVER_ENDPOINT = "/drivers";
    String CREATE_DRIVER_ENDPOINT = "/create";
    String UPDATE_DRIVER_ENDPOINT = "/update/{driverId}";
    String FIND_DRIVER_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_DRIVER_BY_EMAIL_ENDPOINT = "/by-email/{email}";
    String FIND_ALL_DRIVER_ENDPOINT = "/all";
    String FIND_ALL_DRIVERS_PAGEABLE_ENDPOINT = "/all/drivers";
    String FIND_ALL_BY_ISACTIVE_DRIVER_ENDPOINT = "/all/{isactive}";
    String DELETE_DRIVER_ENDPOINT = "/delete/{id}";
    String UPDATE_ISACTIVE_DRIVER_ENDPOINT = "/is-active/{id}/{isactive}";
/////////////////////////////////////////////////////////////////////////


    String DELIVERY_SERVICE_ENDPOINT = "/delivery-services";
    String CREATE_DELIVERY_SERVICE_ENDPOINT = "/create";
    String UPDATE_DELIVERY_SERVICE_ENDPOINT = "/update/{deliveryServiceId}";
    String FIND_DELIVERY_SERVICE_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_ALL_DELIVERY_SERVICE_ENDPOINT = "/all";
    String FIND_ALL_BY_ENABLED_DELIVERY_SERVICE_ENDPOINT = "/all/{enable}";
    String COUNT_ALL_NEW_DELIVERY_SERVICE_ENDPOINT = "/count/new";
    String UPDATE_DELIVERY_SERVICE_ENABLED_ENDPOINT = "/is-enabled/{id}/{enable}";
    String FIND_ALL_DELIVERY_SERVICE_PAGEABLE_ENDPOINT = "/";
    /////////////////////////////////////////////////////////////////////

    String CLIENT_ENDPOINT = "/clients";
    String CREATE_CLIENT_ENDPOINT = "/create";
    String UPDATE_CLIENT_ENDPOINT = "/update/{clientId}";
    String FIND_CLIENT_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_CLIENT_BY_EMAIL_ENDPOINT = "/by-email/{email}";
    String FIND_ALL_CLIENT_ENDPOINT = "/all";
    String FIND_ALL_CLIENT_PAGEABLE_ENDPOINT = "/";
    String DELETE_CLIENT_ENDPOINT = "/delete/{id}";
    String UPDATE_ISACTIVE_CLIENT_ENDPOINT = "/is-active/{id}/{isactive}";
    /////////////////////////////////////////////////////////////////////

    String DELIVERY_POINT_ENDPOINT = "/delivery-points";
    String CREATE_DELIVERY_POINT_ENDPOINT = "/create";
    String UPDATE_DELIVERY_POINT_ENDPOINT = "/update/{deliveryPointId}";
    String FIND_DELIVERY_POINT_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_DELIVERY_POINT_BY_CITY_ENDPOINT = "/by-city/{city}";
    String FIND_ALL_DELIVERY_POINT_ENDPOINT = "/all";
    String FIND_ALL_DELIVERY_POINT_PAGEABLE_ENDPOINT = "/";
    String DELETE_DELIVERY_POINT_ENDPOINT = "/delete/{id}";
    String UPDATE_ISACTIVE_DELIVERY_POINT_ENDPOINT = "/is-active/{id}/{isactive}";
    /////////////////////////////////////////////////////////////////////

    String DELIVERY_POINT_CATEGORY_ENDPOINT = "/dp-categories";
    String CREATE_DELIVERY_POINT_CATEGORY_ENDPOINT = "/create";
    String UPDATE_DELIVERY_POINT_CATEGORY_ENDPOINT = "/update/{deliveryPointCatId}";
    String FIND_DELIVERY_POINT_CATEGORY_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_DELIVERY_POINT_CATEGORY_BY_NAME_ENDPOINT = "/by-name/{name}";
    String FIND_ALL_DELIVERY_POINT_CATEGORY_ENDPOINT = "/all";
    String FIND_ALL_DELIVERY_POINT_CATEGORY_PAGEABLE_ENDPOINT = "/";
    String DELETE_DELIVERY_POINT_CATEGORY_ENDPOINT = "/delete/{id}";
    /////////////////////////////////////////////////////////////////////

    String FEEDBACK_ENDPOINT = "/feedbacks";
    String CREATE_FEEDBACK_ENDPOINT = "/create";
    String UPDATE_FEEDBACK_ENDPOINT = "/update/{feedbackId}";
    String FIND_FEEDBACK_BY_ID_ENDPOINT = "/by-id/{id}";
    String DELETE_FEEDBACK_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_ALL_FEEDBACK_PAGEABLE_BY_DRIVER_ID_ENDPOINT = "/{driverId}";

    ////////////////////////////////////////////////////////////////////

    String DELIVERY_DESTINATION_ENDPOINT = "/delivery-destinations";
    String CREATE_DELIVERY_DESTINATION_ENDPOINT = "/create";
    String UPDATE_DELIVERY_DESTINATION_ENDPOINT = "/update/{deliveryDestinationId}";
    String FIND_DELIVERY_DESTINATION_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_DELIVERY_DESTINATION_BY_CLIENT_ID_ENDPOINT = "/by-client/{clientId}";
    String FIND_DELIVERY_DESTINATION_BY_DELIVERYSERVICE_ID_ENDPOINT = "/by-delivery-serv/{deliveryServId}";
    String FIND_ALL_DELIVERY_DESTINATION_ENDPOINT = "/all";
    String FIND_ALL_DELIVERY_DESTINATION_PAGEABLE_ENDPOINT = "/";
    String DELETE_DELIVERY_DESTINATION_ENDPOINT = "/delete/{id}";

    /////////////////////////////////////////////////////////////////////////
    String ORDER_ENDPOINT = "/orders";
    String CREATE_ORDER_ENDPOINT = "/create";
    String UPDATE_ORDER_ENDPOINT = "/update/{orderId}";
    String FIND_ORDER_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_ORDER_BY_CODE_ENDPOINT = "/by-code/{code}";
    String FIND_ALL_ORDER_ENDPOINT = "/all";
    String DELETE_ORDER_ENDPOINT = "/delete/{id}";
    /////////////////////////////////////////////////////////////////////////
    String MISSION_EXPENSE_ENDPOINT = "/mission-expenses";
    String CREATE_MISSION_EXPENSE_ENDPOINT = "/create";
    String UPDATE_MISSION_EXPENSE_ENDPOINT = "/update/{expenseId}";
    String UPDATE_QTE_LIGNE_MISSION_EXPENSE_ENDPOINT = "/update/{idFM}/{idLigneFM}/{qte}";
    String DELETE_LIGNE_MISSION_EXPENSE_ENDPOINT = "/delete/{idFM}/{idLigneFM}";
    String FIND_MISSION_EXPENSE_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_MISSION_EXPENSE_BY_IDMISSION_ENDPOINT = "/by-mission-id/{idMission}";
    String FIND_ALL_MISSION_EXPENSE_ENDPOINT = "/all";
    String DELETE_MISSION_EXPENSE_ENDPOINT = "/delete/{id}";
    /////////////////////////////////////////////////////////////////////////
    String EMPLOYMENT_CONTRACT_ENDPOINT = "/contracts";
    String CREATE_EMPLOYMENT_CONTRACT_ENDPOINT = "/create";
    String UPDATE_EMPLOYMENT_CONTRACT_ENDPOINT = "/update/{contractId}";
    String FIND_EMPLOYMENT_CONTRACT_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_EMPLOYMENT_CONTRACT_BY_DRIVER_ID_ENDPOINT = "/by-driver-id/{driverId}";
    String FIND_ALL_EMPLOYMENT_CONTRACT_ENDPOINT = "/all";
    String DELETE_EMPLOYMENT_CONTRACT_ENDPOINT = "/delete/{id}";
    /////////////////////////////////////////////////////////////////////////
    String QUOTE_ENDPOINT = "/quotes";
    String CREATE_QUOTE_ENDPOINT = "/create";
    String UPDATE_QUOTE_ENDPOINT = "/update/{quoteId}";
    String FIND_QUOTE_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_QUOTE_BY_CODE_ENDPOINT = "/by-code/{code}";
    String FIND_ALL_QUOTE_ENDPOINT = "/all";
    String DELETE_QUOTE_ENDPOINT = "/delete/{id}";
    /////////////////////////////////////////////////////////////////////////
    String COMPANY_ENDPOINT = "/company";
    String CREATE_COMPANY_ENDPOINT = "/create";
    String UPDATE_COMPANY_ENDPOINT = "/update/{companyId}";
    String FIND_COMPANY_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_COMPANY_BY_RSOCIAL_ENDPOINT = "/by-name/{name}";
    String FIND_ALL_COMPANY_ENDPOINT = "/all";
    String DELETE_COMPANY_ENDPOINT = "/delete/{id}";
    /////////////////////////////////////////////////////////////////////////
    String MISSION_ENDPOINT = "/missions";
    String CREATE_MISSION_ENDPOINT = "/create";
    String UPDATE_MISSION_ENDPOINT = "/update/{missionId}";
    String UPDATE_EXPENSES_MISSION_ENDPOINT = "/update/expenses/{id}";
    String UPDATE_STOP_POINT_MISSION_ENDPOINT = "/update/stop-point/{id}";
    /////////////////////////////////////////////////////////////////////////
    String UPDATE_MISSION_STATUS_ENDPOINT = "/update/status/{missionId}";
    String UPDATE_MILEAGE_MISSION_ENDPOINT = "/update/mileage/{missionId}";
    //////////////////////////////////////////////////////////////////////////
    String FIND_MISSION_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_MISSION_BY_DATE_ENDPOINT = "/by-date/{dateMission}";
    String FIND_ALL_MISSION_ENDPOINT = "/all";
    String DELETE_MISSION_ENDPOINT = "/delete/{id}";
    String GENER_MISSION_ENDPOINT = "/gener-mission";
    String GENER_ASSIGN_ENDPOINT = "/gener-assign";
    String CHECK_OVERLAPPING_DRIVERS_ENDPOINT = "/drivers-check";
    String CHECK_OVERLAPPING_VEHICLES_ENDPOINT = "/vehicles-check";
    String MANAGING_VEHICLES_DRIVERS_ENDPOINT = "assignment/{missionId}";
    /////////////////////////////////////////////////////////////////////////
    String SPARE_PART_ENDPOINT = "/spare-parts";
    String CREATE_SPARE_PART_ENDPOINT = "/create";
    String UPDATE_SPARE_PART_ENDPOINT = "/update/{sparePartId}";
    String FIND_SPARE_PART_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_SPARE_PART_BY_REF_ENDPOINT = "/by-ref/{ref}";
    String FIND_ALL_SPARE_PART_ENDPOINT = "/all";
    String DELETE_SPARE_PART_ENDPOINT = "/delete/{id}";
/////////////////////////////////////////////////////////////////////////
    String STOP_POINT_ENDPOINT = "/stop-points";
    String CREATE_STOP_POINT_ENDPOINT = "/create";
    String UPDATE_STOP_POINT_ENDPOINT = "/update/{stopPointId}";
    String FIND_STOP_POINT_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_STOP_POINT_BY_MISSION_ID_ENDPOINT = "/by-mission-id/{missionId}";
    String FIND_ALL_STOP_POINT_ENDPOINT = "/all";
    String DELETE_STOP_POINT_ENDPOINT = "/delete/{id}";
/////////////////////////////////////////////////////////////////////////

    String REPAIR_ENDPOINT = "/repairs";
    String CREATE_REPAIR_ENDPOINT = "/create";
    String UPDATE_REPAIR_ENDPOINT = "/update/{repairId}";
    String FIND_REPAIR_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_REPAIR_BY_DATE_ENDPOINT = "/by-date/{repDate}";
    String FIND_ALL_REPAIR_ENDPOINT = "/all";
    String DELETE_REPAIR_ENDPOINT = "/delete/{id}";
/////////////////////////////////////////////////////////////////////////

    String ROLE_ENDPOINT = "/roles";
    String CREATE_ROLE_ENDPOINT = "/create";
    String UPDATE_ROLE_ENDPOINT = "/update/{roleId}";
    String FIND_ROLE_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_ALL_ROLE_ENDPOINT = "/all";
    String DELETE_ROLE_ENDPOINT = "/delete/{id}";
/////////////////////////////////////////////////////////////////////////

    String NOTIFICATION_ENDPOINT = "/notifications";
    String UPDATE_NOTIFICATION_ENDPOINT = "/update";
    String FIND_ALL_NOTIFICATION_BY_USERID_ENDPOINT = "/all/by-userid/{userId}";
/////////////////////////////////////////////////////////////////////////

    String USER_ENDPOINT = "/users";
    String CREATE_USER_ENDPOINT = "/create";
    String UPDATE_USER_ENDPOINT = "/update/{userId}";
    String FIND_USER_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_USER_BY_EMAIL_ENDPOINT = "/by-email/{email}";
    String FIND_ALL_USER_ENDPOINT = "/all";
    String FIND_ALL_USER_PAGEABLE_ENDPOINT = "/";
    String DELETE_USER_ENDPOINT = "/delete/{id}";
    String UPDATE_USER_ROLES_ENDPOINT = "/{id}/roles";
    String UPDATE_USER_PASS_ENDPOINT = "/{id}/pass";
/////////////////////////////////////////////////////////////////////////


    String VEHICLE_ENDPOINT = "/vehicles";
    String CREATE_VEHICLE_ENDPOINT = "/create";
    String UPDATE_VEHICLE_ENDPOINT = "/update/{vehicleId}";
    String FIND_VEHICLE_BY_ID_ENDPOINT = "/by-id/{id}";
    String FIND_VEHICLE_BY_REGIS_NBR_ENDPOINT = "/by-regis-nbr/{regisNbr}";
    String FIND_ALL_VEHICLE_ENDPOINT = "/all";
    String FIND_ALL_VEHICLE_PAGEABLE_ENDPOINT = "/";
    String DELETE_VEHICLE_ENDPOINT = "/delete/{id}";
    String UPDATE_ISACTIVE_VEHICLE_ENDPOINT = "/is-active/{id}/{isactive}";
    /////////////////////////////////////////////////////////////////////////
    String DELETE_FLAG = "DELETE";
    String ADD_FLAG = "ADD";
    ////////////////////////////////////////////////////////////////////
    String FIND_ALL_REPAIR_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_SPARE_PART_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_MISSION_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_MISSION_PAGEABLE_BY_DRIVERID_ENDPOINT = "/driver";
    String FIND_ALL_MISSION_BY_DRIVER_ENDPOINT = "/logged/driver";
    String FIND_ALL_MISSION_EXPENSE_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_COMPANY_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_QUOTE_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_EMPLOYMENT_CONTRACT_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_ORDER_PAGEABLE_ENDPOINT = "/";
    String DELETE_DELIVERY_SERVICE_ENDPOINT = "/delete/{id}";
}
