package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.DeliveryPointRepository;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCategoryRequest;
import com.hitecart.deliverysystem.dto.deliverypointcategory.DeliveryPointCatResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.DeliveryPointCategoryMapper;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import com.hitecart.deliverysystem.dao.DeliveryPointCategoryRepository;
import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.services.DeliveryPointCategoryService;
import com.hitecart.deliverysystem.services.UserService;
import com.hitecart.deliverysystem.utils.CapitalizeFirstLetter;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
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
public class DeliveryPointCategoryServiceImpl implements DeliveryPointCategoryService {

    private final DeliveryPointCategoryMapper deliveryPointCategoryMapper;
    private final DeliveryPointCategoryRepository deliveryPointCategoryRepository;
    private final UserService userService;
    private final DeliveryPointRepository deliveryPointRepository;

    @Override
    public DeliveryPointCatResponse save(DeliveryPointCategoryRequest request) {
        checkDto(request, "DeliveryPoint Category not valid", "le DeliveryPoint Category n'est pas valid");
        return savingDeliveryPointCategory(request);

    }

    /***
     *
     * @param request
     * @return DeliveryPointCatResponse
     */
    private DeliveryPointCatResponse savingDeliveryPointCategory(DeliveryPointCategoryRequest request) {
        DeliveryPointCategory dpCategoryToSave = deliveryPointCategoryMapper.toEntity(request);
        dpCategoryToSave.setName(CapitalizeFirstLetter.capitalizeFirstLetter(dpCategoryToSave.getName()));
        checkIfExists(dpCategoryToSave);
        return deliveryPointCategoryMapper.toResponse(deliveryPointCategoryRepository.save(dpCategoryToSave));

    }

    private void checkIfExists(DeliveryPointCategory dpCategoryToSave) {
        deliveryPointCategoryRepository
                .findByNameAndAdditionalFees(dpCategoryToSave.getName(), dpCategoryToSave.getAdditionalFees())
                .ifPresent(existing->{
                       throw  new InvalidOperationException("Category already exists",ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_VALID_OPERATION);
                });
    }

    @Override
    public DeliveryPointCatResponse updateDeliveryPointCategoryInfo(Integer deliveryPointCatId,DeliveryPointCategoryRequest request) {

       if(deliveryPointCatId == null || deliveryPointCatId == 0){
           throw new InvalidOperationException("Delivery Point Category id is required",
                   ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_VALID_OPERATION);
       }
       var existingDelivPointCategory=  deliveryPointCategoryRepository.findById(deliveryPointCatId).orElseThrow(
                () -> new EntityNotFoundException("Delivery Point Category with ::ID " + deliveryPointCatId + " not found")
        );
       DeliveryPointCategory dpCategoryToSave = deliveryPointCategoryMapper.toEntity(request);
        checkIfExists(dpCategoryToSave);
       deliveryPointCategoryMapper.updateDeliveryPointCategoryFromRequest(request,existingDelivPointCategory);

       DeliveryPointCategory updatedDelivPointCategory = deliveryPointCategoryRepository.save(existingDelivPointCategory);

        return deliveryPointCategoryMapper.toResponse(updatedDelivPointCategory);
    }

    @Override
    public DeliveryPointCatResponse findById(Integer id) {
        CheckParmasUtil.checkID(id, "Le parametre ID DeliveryPoint Category est null",
                "Impossible de trouver un DeliveryPoint category avec un ID null", ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_FOUND);

        DeliveryPointCategory dpCategory = deliveryPointCategoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Opps! Aucun Category avec l'id :" + id + "n'a ete trouvé dans la base de donnée",
                        ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_FOUND));


        return deliveryPointCategoryMapper.toResponse(dpCategory);
    }

    @Override
    public DeliveryPointCatResponse findByName(String name) {
        CheckParmasUtil.checkStringParma(name, "", "", ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_VALID_OPERATION);
        DeliveryPointCategory dpCategory = deliveryPointCategoryRepository.findByName(name);
        if (dpCategory == null) {
            throw new EntityNotFoundException(
                    "Opps! Aucun Category avec le nom :"
                            + name + "n'a ete trouvé dans la base de donnée",
                    ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_FOUND
            );
        }
        return deliveryPointCategoryMapper.toResponse(dpCategory);

    }

    @Override
    public List<DeliveryPointCatResponse> findAll() {
        return deliveryPointCategoryRepository.findAll()
                .stream()
                .map(deliveryPointCategoryMapper::toResponse).toList();
    }

    @Override
    public void delete(Integer deliveryPointCategoryId) {

        if(deliveryPointCategoryId == null || deliveryPointCategoryId <= 0) {
            throw new InvalidEntityException("::ID not valid.", ErrorCodes.DELIVERY_SERVICE_NOT_VALID_OPERATION);
        }
        this.findById(deliveryPointCategoryId);

        List<DeliveryPoint> deliveryPoints = deliveryPointRepository.findDeliveryPointByDeliveryPointCategoryId(deliveryPointCategoryId);
        if(!deliveryPoints.isEmpty()){
            throw new InvalidOperationException("DeliveryPointCategory has been linked at least to one deliver point, you can't deleted it",
                    ErrorCodes.DELIVERY_DESTINATION_NOT_VALID_OPERATION);
        }
        this.deliveryPointCategoryRepository.deleteById(deliveryPointCategoryId);
    }

    @Override
    public PageResponse<DeliveryPointCatResponse> findAllDeliveryPointCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<DeliveryPointCategory> deliveryPointCategoriesPage = deliveryPointCategoryRepository.findAllDeliveryPointCategories(pageable);
        List<DeliveryPointCatResponse> deliveryPointCategoryDtos
                = deliveryPointCategoriesPage.stream().map(deliveryPointCategoryMapper::toResponse).toList();

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


    private void checkDto(DeliveryPointCategoryRequest dto, String msgLog, String msgException) {
        List<String> errors = DeliveryPointCategoryValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error(msgLog, dto);
            throw new InvalidEntityException(msgException, ErrorCodes.DELIVERY_POINT_CATEGORY_NOT_FOUND, errors);
        }
    }

}
