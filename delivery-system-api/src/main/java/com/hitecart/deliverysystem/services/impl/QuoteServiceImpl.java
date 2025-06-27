package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.ClientRepository;
import com.hitecart.deliverysystem.dto.quote.QuoteRequest;
import com.hitecart.deliverysystem.dto.quote.QuoteResponse;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.QuoteLineMapper;
import com.hitecart.deliverysystem.mapper.QuoteMapper;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.Quote;
import com.hitecart.deliverysystem.model.QuoteLine;
import com.hitecart.deliverysystem.dao.QuoteRepository;
import com.hitecart.deliverysystem.dao.QuoteLineRepository;
import com.hitecart.deliverysystem.model.enums.QuoteStatus;
import com.hitecart.deliverysystem.services.ClientService;
import com.hitecart.deliverysystem.services.QuoteService;
import com.hitecart.deliverysystem.services.UserService;
import com.hitecart.deliverysystem.utils.CalculeTva;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.QuoteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    private final QuoteLineRepository quoteLineRepository;
    private final ClientService clientService;
    private final UserService userServiceImpl;
    private final ClientRepository clientRepository;
    private final QuoteMapper quoteMapper;
    private final QuoteLineMapper quoteLineMapper;


    @Override
    public QuoteResponse save(QuoteRequest quoteRequest) {

        checkDto(quoteRequest,"Commande Client n'est pas valide",
                "Le devis n'est pas valide",ErrorCodes.QUOTE_NOT_VALID_OPERATION);


        if(quoteRequest.id()!=null && quoteRequest.quoteStatus().equals(QuoteStatus.CONFIRMED)){
            throw new InvalidOperationException("Unable to modify the quotes once it is completed",
                    ErrorCodes.QUOTE_NOT_VALID_OPERATION);
        }

      return   savingQuote(quoteRequest);
    }

    private QuoteResponse savingQuote(QuoteRequest quoteRequest){

        Client client=clientRepository.findById(quoteRequest.clientId()).orElseThrow(
                ()-> new EntityNotFoundException(" Client with ::ID "
                        + quoteRequest.clientId() + " not found",ErrorCodes.CLIENT_NOT_FOUND)
        );

        Quote savedQuote = quoteMapper.toEntity(quoteRequest);
        savedQuote.setClient(client);
        savedQuote = quoteRepository.save(savedQuote);

        BigDecimal[] totalAmountTTC= {BigDecimal.ZERO};
        BigDecimal[] totalAmountHT = {BigDecimal.ZERO};

        if(!quoteRequest.quoteLines().isEmpty()){
            Quote finalSavedQuote = savedQuote;
            quoteRequest.quoteLines().forEach(quoteLine-> {

                BigDecimal prixTTC = CalculeTva.calculeTTC(quoteLine.unitPrice(),quoteLine.tva());
                totalAmountTTC[0] =totalAmountTTC[0].add(prixTTC.multiply(quoteLine.quantity()));
                totalAmountHT[0] =totalAmountHT[0].add(quoteLine.unitPrice().multiply(quoteLine.quantity()));

                QuoteLine devisLineToSave= quoteLineMapper.toEntity(quoteLine);
                devisLineToSave.setQuote(finalSavedQuote);
                devisLineToSave.setTtcPrice(prixTTC);
                quoteLineRepository.save(devisLineToSave);
            });
        }

        savedQuote.setTotalAmountHT(totalAmountHT[0]);
        savedQuote.setTotalAmountTTC(totalAmountTTC[0]);

        savedQuote = quoteRepository.save(savedQuote);

        return quoteMapper.toResponse(savedQuote);

    }
    @Override
    public QuoteResponse updateQuteInfo(Integer quoteId, QuoteRequest quoteRequest) {

 /*       checkDto(quoteRequest,"Commande Client n'est pas valide",
                "Le devis n'est pas valide",ErrorCodes.QUOTE_NOT_VALID_OPERATION);*/

        var existinQuote = quoteRepository.findById(quoteRequest.id()).orElseThrow(
                ()-> new EntityNotFoundException(" Quote with ::ID "+quoteRequest.id()+" not found",
                        ErrorCodes.QUOTE_NOT_FOUND)
        );
        if(quoteRequest.id()!=null && quoteRequest.quoteStatus().equals(QuoteStatus.CONFIRMED)){
            throw new InvalidOperationException("Unable to modify the quotes once it is completed",
                    ErrorCodes.QUOTE_NOT_VALID_OPERATION);
        }

        quoteMapper.updateQuoteFromRequest(quoteRequest, existinQuote);

        Quote updatedQuote = quoteRepository.save(existinQuote);

        return quoteMapper.toResponse(updatedQuote);
    }

    @Override
    public QuoteResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID Commande Client est null",
                "Impossible de trouver un devis avec un ID null",
                ErrorCodes.QUOTE_NOT_VALID_OPERATION);
        Quote quoteById= quoteRepository.findById(id).orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                + id +"n'a ete trouvé dans la base deonnée"
                ,ErrorCodes.ORDER_NOT_FOUND));
        return quoteMapper.toResponse(quoteById);
    }

    @Override
    public QuoteResponse findByCode(String code) {

        CheckParmasUtil.checkCode(code,"ERREUR ! Le parametre Code Commande client  est null ",
                "Impossible de trouver un devis avec un Code null",ErrorCodes.QUOTE_NOT_VALID_OPERATION);

        Quote quoteByCode= quoteRepository.findQuoteByCode(code).orElseThrow(()->new EntityNotFoundException( "Opps! Aucune Commande Client  avec l'id :"
                + code +"n'a ete trouvé dans la base deonnée"
                ,ErrorCodes.QUOTE_NOT_FOUND));

        return quoteMapper.toResponse(quoteByCode);
    }

    @Override
    public List<QuoteResponse> findAll() {
        return quoteRepository.findAll().stream()
                .map(quoteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id,"l'ID du devis  à supprimer est null",
                "Impossible de supprimer un devis  avec un ID null",ErrorCodes.QUOTE_NOT_VALID_OPERATION);

        List<QuoteLine> ligneDevisses = quoteLineRepository.findAllByQuoteId(id);
        if(!ligneDevisses.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un devis qui a deja des lignes de commandes"
                    ,ErrorCodes.QUOTE_NOT_VALID_OPERATION);
        }
        quoteRepository.deleteById(id);
    }

    @Override
    public PageResponse<QuoteResponse> findAllQuotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Quote> devis= quoteRepository.findAllQuotes(pageable);
        List<QuoteResponse> devisDtos = devis.stream()
                //.map(QuoteFullDtoResponse::fromEntity)
                .map(quoteMapper::toResponse)
                .toList();

        return new PageResponse<>(
                devisDtos,
                devis.getNumber(),
                devis.getSize(),
                devis.getTotalElements(),
                devis.getTotalPages(),
                devis.isFirst(),
                devis.isLast()
        );
    }

    private void checkDto(QuoteRequest quoteRequest, String msgLog, String msgException, ErrorCodes commandeclientNotValid) {
        List<String> errors = QuoteValidator.validate(quoteRequest);
        if(!errors.isEmpty()){
            log.error(msgLog,quoteRequest);
            throw new InvalidEntityException(msgException, ErrorCodes.DRIVER_NOT_VALID,errors);
        }
    }


}
