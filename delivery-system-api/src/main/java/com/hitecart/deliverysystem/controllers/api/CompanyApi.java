package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.company.CompanyRequest;
import com.hitecart.deliverysystem.dto.company.CompanyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(COMPANY_ENDPOINT)
public interface CompanyApi {

    @PostMapping(value = CREATE_COMPANY_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveCompany",

            description = "Ajouter/Modifier une entreprise, cette methode permet " +
                    "d'enregistrer ou modifier une entreprise",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Company a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Company n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<CompanyResponse> saveCompany(@RequestBody CompanyRequest companyRequest);

    @PatchMapping(value = UPDATE_COMPANY_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "updateCompanyInfo",

            description = "Modifier une entreprise, cette methode permet " +
                    "de modifier une entreprise",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Company a été modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Company n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<CompanyResponse> updateCompanyInfo(
            @PathVariable("companyId")Integer companyId,@RequestBody CompanyRequest companyRequest);

    @GetMapping(value = FIND_COMPANY_BY_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findCompanyById",

            description = "Rechercher une Company par ID! Cette methode permet de chercher une entreprise par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le entreprise a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le entreprise avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<CompanyResponse> findCompanyById(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_COMPANY_BY_RSOCIAL_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findCompanyByName",

            description = "Rechercher une Company par nom! Cette methode permet de chercher une entreprise par son nom",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le entreprise a été trouvée dans la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le entreprise avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<CompanyResponse> findCompanyByName(@PathVariable("name") String name);

    @GetMapping(value = FIND_ALL_COMPANY_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllCompany",

            description = "Rechercher tout les Company! Cette methode permet de retouner tous les entreprises",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Entreprises retrouvés ",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<List<CompanyResponse>> findAllCompany();

    @DeleteMapping(value = DELETE_COMPANY_ENDPOINT)
    @Operation(operationId = "deleteCompany",

            description = "Supprimer une Company par ID! Cette methode permet de suprimmer une entreprise par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le entreprise a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le entreprise avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<?> deleteCompany(@PathVariable("id") Integer id);

    @GetMapping(value = FIND_ALL_COMPANY_PAGEABLE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllCompaniesPageable"
    )
    ResponseEntity<PageResponse<CompanyResponse>> findAllCompaniesPageable(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    );
}
