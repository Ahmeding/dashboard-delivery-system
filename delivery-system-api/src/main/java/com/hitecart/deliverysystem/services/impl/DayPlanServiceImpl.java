package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.dto.basic.CreatedByUserDto;
import com.hitecart.deliverysystem.dto.basic.DayPlanDto;
import com.hitecart.deliverysystem.dto.basic.UtilisateurDto;
import com.hitecart.deliverysystem.dto.basic.WeeklyPlanningDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.WeeklyPlanning;
import com.hitecart.deliverysystem.services.DayPlanService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.services.WeeklyPlanningService;
import com.hitecart.deliverysystem.validator.DayPlanValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.hitecart.deliverysystem.model.DayPlan;
import com.hitecart.deliverysystem.dao.DayPlanRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DayPlanServiceImpl implements DayPlanService {

    private final DayPlanRepository dayPlanRepository;
    private final WeeklyPlanningService WeeklyPlanRepository;
    private final UtilisateurService utilisateurServiceImpl;

    @Override
    public List<DayPlanDto> findAllByWeeklyPlanningId(Integer weeklyPlanningId) {
      /*  CheckParmasUtil.checkID(
                weeklyPlanningId,
                "ID WeeklyPlanning est null",
                "Immpossible de trouver un WeeklyPlanning avec un id null",
                ErrorCodes.WEEKLYPLANNING_NOT_FOUND);

        return dayPlanRepository.findAllByWeeklyPlanningId(weeklyPlanningId).stream()
                .map(this::updatedResponse)
                .collect(Collectors.toList());*/
        return null;
    }

    @Override
    public List<DayPlanDto> findAll() {
        return dayPlanRepository.findAll().stream()
                .map(this::updatedResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DayPlanDto findById(Integer id) {
        CheckParmasUtil.checkID(
                id,
                "ID DayPlan est null",
                "Immpossible de trouver un DayPlan avec un id null",
                ErrorCodes.DAYPLAN_NOT_VALID_OPERATION);

        DayPlan dayPlan = dayPlanRepository.findById(id).orElseThrow(()->new EntityNotFoundException(
                "Aucun DayPlan avec L'ID "+id+"n'a ete trouvé dans la base des données",
                ErrorCodes.DAYPLAN_NOT_FOUND));

        DayPlanDto dayPlanDto =  (DayPlanDto.fromEntity(dayPlan));

        setCreatedByUser(dayPlanDto);
        return dayPlanDto;


    }

    @Override
    public DayPlanDto save(DayPlanDto dto) {
        checkDto(
                dto,
                "DayPlan n'est pas valid",
                "Le DayPlan n'est pa valide ",
                ErrorCodes.DAYPLAN_NOT_VALID);
        CheckParmasUtil.checkID(
                dto.getIdWeeklyPlanning(),
                "ID WeekPlanning est null",
                "Immpossible de trouver un WeeklyPlanning avec un id null",
                ErrorCodes.WEEKLYPLANNING_NOT_VALID_OPERATION);
        WeeklyPlanning weeklyPlanning= WeeklyPlanningDto.toEntity(
                WeeklyPlanRepository.findById(dto.getIdWeeklyPlanning()));
        if(weeklyPlanning==null){
            throw new EntityNotFoundException("Aucun WeeklyPalnning trouvées",
                    ErrorCodes.WEEKLYPLANNING_NOT_FOUND);
        }


        DayPlan dayPlanToSave = DayPlanDto.toEntity(dto);

        return DayPlanDto.fromEntity(dayPlanRepository.save(dayPlanToSave));



    }

    @Override
    public void deleteById(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID du DayPlan à supprimer est null",
                "Impossible de supprimer un DayPlan avec un ID null",
                ErrorCodes.DAYPLAN_NOT_VALID_OPERATION);

        dayPlanRepository.deleteById(id);
    }


    private void checkDto(DayPlanDto dto, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = DayPlanValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, errorCodes,errors);
        }
    }


    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurServiceImpl.findById(userid);
    }

    private void setCreatedByUser(DayPlanDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private DayPlanDto updatedResponse(DayPlan dayPlan) {
        DayPlanDto dayPlanDto=DayPlanDto.fromEntity(dayPlan);
        setCreatedByUser(dayPlanDto);
        return dayPlanDto;
    }
}
