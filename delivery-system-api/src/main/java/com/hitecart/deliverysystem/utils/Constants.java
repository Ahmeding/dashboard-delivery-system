package com.hitecart.deliverysystem.utils;

public interface Constants {



    String CHAUFFEUR_ENDPOINT= "/chauffeurs";
    String CREATE_CHAUFFEUR_ENDPOINT= "/create";
    String FIND_CHAUFFEUR_BY_ID_ENDPOINT= "/chauffeur/by-id/{id}";
    String FIND_CHAUFFEUR_BY_EMAIL_ENDPOINT= "/chauffeur/by-email/{email}";
    String FIND_ALL_CHAUFFEUR_ENDPOINT= "/all";
    String FIND_ALL_DRIVERS_PAGEABLE_ENDPOINT= "/all/drivers";
    String FIND_ALL_BY_ISACTIVE_CHAUFFEUR_ENDPOINT= "/all/{isactive}";
    String DELETE_CHAUFFEUR_ENDPOINT= "/delete/{id}";
    String UPDATE_ISACTIVE_CHAUFFEUR_ENDPOINT= "/is-active/{id}/{isactive}";
/////////////////////////////////////////////////////////////////////////


    String SERVICE_DELIVERY_ENDPOINT= "/services-delivery";
    String CREATE_SERVICE_DELIVERY_ENDPOINT= "/create";
    String FIND_SERVICE_DELIVERY_BY_ID_ENDPOINT= "/service-d/by-id/{id}";
    String FIND_ALL_SERVICE_DELIVERY_ENDPOINT= "/all";
    String FIND_ALL_BY_ENABLED_SERVICE_DELIVERY_ENDPOINT= "/sd/all/{isenabled}";
    String UPDATE_SERVICE_DELIVERY_ENABLED_ENDPOINT= "/sd/is-enabled/{id}/{isenabled}";
    String FIND_ALL_SERVICE_DELIVERY_PAGEABLE_ENDPOINT= "/";
    /////////////////////////////////////////////////////////////////////

    String CLIENT_ENDPOINT= "/clients";
    String CREATE_CLIENT_ENDPOINT= "/create";
    String FIND_CLIENT_BY_ID_ENDPOINT= "/client/by-id/{id}";
    String FIND_CLIENT_BY_EMAIL_ENDPOINT= "/client/by-email/{email}";
    String FIND_ALL_CLIENT_ENDPOINT= "/all";
    String FIND_ALL_CLIENT_PAGEABLE_ENDPOINT= "/";
    String DELETE_CLIENT_ENDPOINT= "/delete/{id}";
    String UPDATE_ISACTIVE_CLIENT_ENDPOINT= "/is-active/{id}/{isactive}";
    /////////////////////////////////////////////////////////////////////

    String DELIVERY_POINT_ENDPOINT= "/delivery-points";
    String CREATE_DELIVERY_POINT_ENDPOINT= "/create";
    String FIND_DELIVERY_POINT_BY_ID_ENDPOINT= "/dpoint/by-id/{id}";
    String FIND_DELIVERY_POINT_BY_CITY_ENDPOINT= "/dpoint/by-city/{city}";
    String FIND_ALL_DELIVERY_POINT_ENDPOINT= "/all";
    String FIND_ALL_DELIVERY_POINT_PAGEABLE_ENDPOINT= "/";
    String DELETE_DELIVERY_POINT_ENDPOINT= "/delete/{id}";
    String UPDATE_ISACTIVE_DELIVERY_POINT_ENDPOINT= "/is-active/{id}/{isactive}";

/////////////////////////////////////////////////////////////////////////
    String COMMANDE_CLI_ENDPOINT= "/cmdclients";
    String CREATE_COMMANDE_CLI_ENDPOINT= "/create";
    String FIND_COMMANDE_CLI_BY_ID_ENDPOINT= "/cmdclient/by-id/{id}";
    String FIND_COMMANDE_CLI_BY_CODE_ENDPOINT= "/cmdclient/by-code/{code}";
    String FIND_ALL_COMMANDE_CLI_ENDPOINT= "/all";
    String DELETE_COMMANDE_CLI_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////
    String FRAISMISSION_ENDPOINT= "/fraismissions";
    String CREATE_FRAISMISSION_ENDPOINT= "/create";
    String UPDATE_FRAISMISSION_ENDPOINT= "/update";
    String UPDATE_QTE_LIGNE_FRAISMISSION_ENDPOINT= "/update/{idFM}/{idLigneFM}/{qte}";
    String DELETE_LIGNE_FRAISMISSION_ENDPOINT= "/delete/{idFM}/{idLigneFM}";
    String FIND_FRAISMISSION_BY_ID_ENDPOINT= "/fraisMission/{id}";
    String FIND_FRAISMISSION_BY_IDMISSION_ENDPOINT= "/mission/{idMission}";
    String FIND_ALL_LIGNESFRAISMISSION_BY_IDFRAISMISSION_ENDPOINT= "/fraismission/{idFM}";
    String FIND_ALL_FRAISMISSION_ENDPOINT= "/all";
    String DELETE_FRAISMISSION_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////
    String CONTRAT_ENDPOINT= "/contrats";
    String CREATE_CONTRAT_ENDPOINT= "/create";
    String FIND_CONTRAT_BY_ID_ENDPOINT= "/contrat/{id}";
    String FIND_CONTRAT_BY_IDCHAUFFEUR_ENDPOINT= "/chauffeur/{idChauffeur}";
    String FIND_ALL_CONTRAT_ENDPOINT= "/all";
    String DELETE_CONTRAT_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////
    String DEVIS_ENDPOINT= "/devis";
    String CREATE_DEVIS_ENDPOINT= "/create";
    String FIND_DEVIS_BY_ID_ENDPOINT= "/devis/by-id/{id}";
    String FIND_DEVIS_BY_CODE_ENDPOINT= "/devis/by-code/{code}";
    String FIND_ALL_DEVIS_ENDPOINT= "/all";
    String DELETE_DEVIS_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////
String ENTREPRISE_ENDPOINT= "/entreprises";
    String CREATE_ENTREPRISE_ENDPOINT= "/create";
    String FIND_ENTREPRISE_BY_ID_ENDPOINT= "/entreprise/by-id/{id}";
    String FIND_ENTREPRISE_BY_RSOCIAL_ENDPOINT= "/entreprise/by-rsocial/{rsocial}";
    String FIND_ALL_ENTREPRISE_ENDPOINT= "/all";
    String DELETE_ENTREPRISE_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////
String MISSION_ENDPOINT= "/missions";
    String CREATE_MISSION_ENDPOINT= "/create";
    String UPDATE_ETAT_MISSION_ENDPOINT= "/update/{id}/{etatMission}";
    String UPDATE_KILOMETRATGE_MISSION_ENDPOINT= "/update/{flag}/{id}/{kilometrage}";
    String FIND_MISSION_BY_ID_ENDPOINT= "/mission/{id}";
    String FIND_MISSION_BY_DATE_ENDPOINT= "/date/{dateMission}";
    String FIND_ALL_MISSION_ENDPOINT= "/all";
    String DELETE_MISSION_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////
String PIECE_RECHANGE_ENDPOINT= "/piecerenchanges";
    String CREATE_PIECE_RECHANGE_ENDPOINT= "/create";
    String FIND_PIECE_RECHANGE_BY_ID_ENDPOINT= "/piecerenchange/by-id/{id}";
    String FIND_PIECE_RECHANGE_BY_REF_ENDPOINT= "/piecerenchange/by-ref/{ref}";
    String FIND_ALL_PIECE_RECHANGE_ENDPOINT= "/all";
    String DELETE_PIECE_RECHANGE_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////

    String REPARATION_ENDPOINT= "/reparations";
    String CREATE_REPARATION_ENDPOINT= "/create";
    String FIND_REPARATION_BY_ID_ENDPOINT= "/reparation/{id}";
    String FIND_REPARATION_BY_DATE_ENDPOINT= "/date/{dateRep}";
    String FIND_ALL_REPARATION_ENDPOINT= "/all";
    String DELETE_REPARATION_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////

    String UTILISATEUR_ENDPOINT= "/utilisateurs";
    String CREATE_UTILISATEUR_ENDPOINT= "/create";
    String FIND_UTILISATEUR_BY_ID_ENDPOINT= "/user/by-id/{id}";
    String FIND_UTILISATEUR_BY_EMAIL_ENDPOINT= "/user/by-email/{email}";
    String FIND_ALL_UTILISATEUR_ENDPOINT= "/all";
    String FIND_ALL_UTILISATEUR_PAGEABLE_ENDPOINT ="/" ;
    String DELETE_UTILISATEUR_ENDPOINT= "/delete/{id}";
/////////////////////////////////////////////////////////////////////////


    String VEHICULE_ENDPOINT= "/vehicules";
    String CREATE_VEHICULE_ENDPOINT= "/create";
    String FIND_VEHICULE_BY_ID_ENDPOINT= "/vehicule/by-id/{id}";
    String FIND_VEHICULE_BY_IMMAT_ENDPOINT= "/vehicule/by-immat/{immat}";
    String FIND_ALL_VEHICULE_ENDPOINT= "/all";
    String FIND_ALL_VEHICULE_PAGEABLE_ENDPOINT ="/";
    String DELETE_VEHICULE_ENDPOINT= "/delete/{id}";
    String UPDATE_ISACTIVE_VEHICULE_ENDPOINT= "/is-active/{id}/{isactive}";
/////////////////////////////////////////////////////////////////////////
  String DELETE_FLAG= "DELETE";
  String ADD_FLAG= "ADD";


    String FIND_ALL_REPARATION_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_PIECERECHANGE_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_MISSION_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_MISSION_PAGEABLE_BY_DRIVERID_ENDPOINT = "/driver";
    String FIND_ALL_FRAISMISSION_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_ENTREPRISE_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_DEVIS_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_CONTRAT_PAGEABLE_ENDPOINT = "/";
    String FIND_ALL_COMMANDECLIENT_PAGEABLE_ENDPOINT = "/";
    String DELETE_DELIVERY_SERVICE_ENDPOINT = "/delete/{id}";
}
