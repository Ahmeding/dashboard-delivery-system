package com.hitecart.deliverysystem.controllers.api;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.order.OrderRequest;
import com.hitecart.deliverysystem.dto.order.OrderResponse;
import com.hitecart.deliverysystem.dto.quote.QuoteRequest;
import com.hitecart.deliverysystem.dto.quote.QuoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hitecart.deliverysystem.utils.Constants.*;

@RequestMapping(QUOTE_ENDPOINT)
@Tag(name = "Quote")
public interface QuoteApi {


    @PostMapping(value = CREATE_QUOTE_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "saveQuote",
            description = "Ajouter/Modifier un devis, cette methode permet " +
                    "d'enregistrer ou modifier un devis",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet Quote a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = QuoteResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Quote n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<QuoteResponse> saveQuote(@RequestBody QuoteRequest quoteRequest);


    @PatchMapping(value = UPDATE_QUOTE_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(operationId = "updateQuoteInfo",
            description = "Ajouter/Modifier devis , cette methode permet " +
                    "d'enregistrer ou modifier un devis",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "L'objet devis  a été créé / modifié",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "L'objet Order  n'est pas valide",
                            content = @Content(mediaType = "application/json"))
            })
    ResponseEntity<QuoteResponse> updateQuoteInfo(@PathVariable("quoteId")Integer quoteId, @RequestBody QuoteRequest quoteRequest);


    @GetMapping(value = FIND_QUOTE_BY_ID_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findQuoteById",
            description = "Rechercher un Quote par ID! Cette methode permet de chercher un devis par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le devis a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = QuoteResponse.class))
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le devis avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<QuoteResponse> findQuoteById(@PathVariable("id") Integer id);



    @GetMapping(value = FIND_QUOTE_BY_CODE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findQuoteByCode",
            description = "Rechercher un Quote par code! Cette methode permet de chercher un devis par son code",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le devis a été trouvé dans la BBD",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = QuoteResponse.class)
                            )
                    ),

                    @ApiResponse(
                            responseCode = "400",
                            description = "Le devis avec L'email frouni n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    ResponseEntity<QuoteResponse> findQuoteByCode(@PathVariable("code") String code);



    @GetMapping(value = FIND_ALL_QUOTE_ENDPOINT,produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "findAllQuote",
            description = "Rechercher tout les Quote! Cette methode permet de retouner tous les devis",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deviss retrouvés ",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = QuoteResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<List<QuoteResponse>> findAllQuote();


    @DeleteMapping(value = DELETE_QUOTE_ENDPOINT)
    @Operation(operationId = "deleteQuote",
            description = "Supprimer un Quote par ID! Cette methode permet de suprimmer un devis par son ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Le devis a été  supprimer de la BBD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Le devis avec L'ID frounie n'esxiste pas dans la BDD",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
     ResponseEntity<?> deleteQuote(@PathVariable("id") Integer id);

    @GetMapping(value =FIND_ALL_QUOTE_PAGEABLE_ENDPOINT,
            produces =MediaType.APPLICATION_JSON_VALUE  )
    @Operation(operationId = "findAllQuotesPageable")
    ResponseEntity<PageResponse<QuoteResponse>> findAllQuotesPageable(
            @RequestParam(name = "page",defaultValue = "1", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size
    );
}
