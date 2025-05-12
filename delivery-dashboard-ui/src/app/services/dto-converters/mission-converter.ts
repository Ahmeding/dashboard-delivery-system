import {MissionResponse} from "../../../ds-api/src/models/mission-response";
import {MissionDto} from "../../../ds-api/src/models/mission-dto";
import {CreatedByUserDto} from "../../../ds-api/src/models/created-by-user-dto";
import {EtapeDto} from "../../../ds-api/src/models/etape-dto";
import {MissionVehicleDto} from "../../../ds-api/src/models/mission-vehicle-dto";

export function convertResponseToDto(response: MissionResponse): MissionDto{
  return {
    id: response.id,
    titre: response.titre,
    volume: response.volume,
    dateDebut: response.dateDebut,
    dateFin: response.dateFin,
    missionVehiclesDtos: response.missionVehiclesDtos,
    commentaire: response.commentaire,
    etapeDtos: response.etapeDtos,
    etatMission: response.etatMission,
    idDeliveryDestination: response.deliveryDestinationResponse?.id,
    kmDebut: response.kmDebut,
    kmFin: response.kmFin,
    missionAccomplished: response.missionAccomplished,
    reference: response.reference,
  };
}
