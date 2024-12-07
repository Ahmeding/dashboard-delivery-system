package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.CreatedByUserDto;
import com.hitecart.deliverysystem.dto.DayPlanDto;
import com.hitecart.deliverysystem.dto.UtilisateurDto;
import com.hitecart.deliverysystem.dto.WeeklyPlanningDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.services.WeeklyPlanningService;
import com.hitecart.deliverysystem.validator.DayPlanValidator;
import com.hitecart.deliverysystem.validator.WeeklyPlanningValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.hitecart.deliverysystem.model.WeeklyPlanning;
import com.hitecart.deliverysystem.repository.WeeklyPlanningRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklyPlanningServiceImpl implements WeeklyPlanningService {

    private final WeeklyPlanningRepository weeklyPlanningRepository;
    private final UtilisateurService utilisateurServiceImpl;

    @Override
    public List<WeeklyPlanningDto> findAll() {

        return weeklyPlanningRepository.findAll().stream()
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WeeklyPlanningDto findById(Integer id) {
        CheckParmasUtil.checkID(
                id,
                "ID WeeklyPlanning est null",
                "Immpossible de trouver un WeeklyPlanning avec un id null",
                ErrorCodes.WEEKLYPLANNING_NOT_VALID_OPERATION);

        Optional<WeeklyPlanning> weeklyPlanning = weeklyPlanningRepository.findById(id);

        WeeklyPlanningDto weeklyPlanningDto =  Optional.of(WeeklyPlanningDto.fromEntity(weeklyPlanning.get()))
                .orElseThrow(
                        ()->new EntityNotFoundException(
                                "Aucun weeklyPlanning avec L'ID "+id+"n'a ete trouvé dans la base des données",
                                ErrorCodes.WEEKLYPLANNING_NOT_FOUND)
                );

        setCreatedByUser(weeklyPlanningDto);
        return weeklyPlanningDto;
    }

    @Override
    public WeeklyPlanningDto save(WeeklyPlanningDto dto) {
        checkDto(
                dto,
                "WeeklyPlanning n'est pas valid",
                "Le WeeklyPlanning n'est pa valide ",
                ErrorCodes.DAYPLAN_NOT_VALID);
        return WeeklyPlanningDto.fromEntity(weeklyPlanningRepository.save(WeeklyPlanningDto.toEntity(dto)));

    }

    @Override
    public void deleteById(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID du WeeklyPlanning à supprimer est null",
                "Impossible de supprimer un WeeklyPlanning avec un ID null",ErrorCodes.WEEKLYPLANNING_NOT_VALID_OPERATION);

        weeklyPlanningRepository.deleteById(id);
    }


    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }

    private void setCreatedByUser(WeeklyPlanningDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private WeeklyPlanningDto updatedResponse(WeeklyPlanning weeklyPlanning) {
        WeeklyPlanningDto weeklyPlanningDto=WeeklyPlanningDto.fromEntity(weeklyPlanning);
        setCreatedByUser(weeklyPlanningDto);
        return weeklyPlanningDto;
    }


    private void checkDto(WeeklyPlanningDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = WeeklyPlanningValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }
}
