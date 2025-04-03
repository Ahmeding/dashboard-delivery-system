package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.basic.CreatedByUserDto;
import com.hitecart.deliverysystem.dto.basic.DeliveryPointCategoryDto;
import com.hitecart.deliverysystem.dto.basic.UtilisateurDto;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import com.hitecart.deliverysystem.dao.IDeliveryPointCategoryRepository;
import com.hitecart.deliverysystem.services.IDeliverPointCategoryService;
import com.hitecart.deliverysystem.services.UtilisateurService;
import com.hitecart.deliverysystem.validator.DeliveryPointCategoryValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliverPointCategoryServiceImpl implements IDeliverPointCategoryService {


    private final IDeliveryPointCategoryRepository deliveryPointCategoryRepository;
    private final UtilisateurService utilisateurService;

    @Override
    public DeliveryPointCategoryDto save(DeliveryPointCategoryDto dto) {
        checkDto(dto,"DeliveryPoint Category not valid","le DeliveryPoint Category n'est pas valid");
        DeliveryPointCategory dpCategoryToSave = DeliveryPointCategoryDto.toEntity(dto);
        return DeliveryPointCategoryDto.fromEntity(deliveryPointCategoryRepository.save(dpCategoryToSave));

    }

    @Override
    public DeliveryPointCategoryDto findById(Integer id) {
        CheckParmasUtil.checkID(id,"Le parametre ID DeliveryPoint Category est null",
                "Impossible de trouver un DeliveryPoint category avec un ID null",ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_FOUND);

        DeliveryPointCategory dpCategory = deliveryPointCategoryRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException( "Opps! Aucun Category avec l'id :"+ id +"n'a ete trouvé dans la base de donnée",
                        ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_FOUND));

        DeliveryPointCategoryDto dpCategoryDto =
                DeliveryPointCategoryDto.fromEntity(dpCategory);

        setCreatedByUser(dpCategoryDto);
        return dpCategoryDto;
    }

    @Override
    public DeliveryPointCategoryDto findByName(String name) {
        CheckParmasUtil.checkStringParma(name,"","",ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_VALID_OPERATION);
        DeliveryPointCategory dpCategory= deliveryPointCategoryRepository.findByName(name);
        if(dpCategory == null){
            throw new EntityNotFoundException(
                    "Opps! Aucun Category avec le nom :"
                            + name +"n'a ete trouvé dans la base de donnée",
                    ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_FOUND
            );
        }
        DeliveryPointCategoryDto deliveryPointCategoryDto = DeliveryPointCategoryDto.fromEntity(dpCategory);
        setCreatedByUser(deliveryPointCategoryDto);
        return deliveryPointCategoryDto;

    }

    @Override
    public List<DeliveryPointCategoryDto> findAll() {
        return deliveryPointCategoryRepository.findAll().stream().map(this::updatedResponse).toList();
    }

    @Override
    public void delete(Integer id) {
        this.deliveryPointCategoryRepository.deleteById(id);
    }

    @Override
    public PageResponse<DeliveryPointCategoryDto> findAllDeliveryPointCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<DeliveryPointCategory> deliveryPointCategoriesPage = deliveryPointCategoryRepository.findAllDeliveryPointCategories(pageable);
        List<DeliveryPointCategoryDto> deliveryPointCategoryDtos
                = deliveryPointCategoriesPage.stream().map(this::updatedResponse).toList();

        return new PageResponse<>(
                deliveryPointCategoryDtos,
                deliveryPointCategoriesPage.getNumber(),
                deliveryPointCategoriesPage.getSize(),
                deliveryPointCategoriesPage.getTotalElements(),
                deliveryPointCategoriesPage.getTotalPages(),
                deliveryPointCategoriesPage.isFirst(),
                deliveryPointCategoriesPage.isLast()
        );
    }



    private void checkDto(DeliveryPointCategoryDto dto, String msgLog, String msgException) {
        List<String> errors = DeliveryPointCategoryValidator.validate(dto);
        if(!errors.isEmpty()){
            log.error(msgLog,dto);
            throw new InvalidEntityException(msgException, ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_FOUND,errors);
        }
    }

    private UtilisateurDto getCreatedBy(Integer userid) {
        return utilisateurService.findById(userid);
    }

    private void setCreatedByUser(DeliveryPointCategoryDto response){
        UtilisateurDto uDto = getCreatedBy(response.getCreatedBy());
        response.setCreatedByUser(CreatedByUserDto.builder().id(uDto.getId()).fullname(uDto.getFullname()).build());
    }

    private DeliveryPointCategoryDto updatedResponse(DeliveryPointCategory dpCategory) {
        DeliveryPointCategoryDto dpCategoryDto =DeliveryPointCategoryDto.fromEntity(dpCategory);
        setCreatedByUser(dpCategoryDto);
        return dpCategoryDto;
    }
}
