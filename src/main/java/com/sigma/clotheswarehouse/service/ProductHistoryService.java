package com.sigma.clotheswarehouse.service;

import com.sigma.clotheswarehouse.entity.Client;
import com.sigma.clotheswarehouse.entity.Product;
import com.sigma.clotheswarehouse.entity.ProductHistory;
import com.sigma.clotheswarehouse.mapper.ProductHistoryMapper;
import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.ProductHistoryDTO;
import com.sigma.clotheswarehouse.payload.ProductHistoryGetDTO;
import com.sigma.clotheswarehouse.repository.ClientRepository;
import com.sigma.clotheswarehouse.repository.ProductHistoryRepository;
import com.sigma.clotheswarehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {

    private final ProductHistoryRepository repository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final ProductHistoryMapper mapper;

    public HttpEntity<?> getOneWeek() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        List<ProductHistory> getProductHistoryOneWeek = repository.
                getAllByDateBetween(new Timestamp(timestamp.getTime() - 604800000), timestamp, Sort.by("date").descending());
        List<ProductHistoryGetDTO> productHistoryGetDTOS = mapper.toGetDTOList(getProductHistoryOneWeek);
        return ResponseEntity.ok(productHistoryGetDTOS);
    }

    public HttpEntity<?> getOneMonth() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        List<ProductHistory> getProductHistoryOneMonth = repository.
                getAllByDateBetween(new Timestamp(timestamp.getTime() - 2678400000L), timestamp, Sort.by("date").descending());
        List<ProductHistoryGetDTO> productHistoryGetDTOS = mapper.toGetDTOList(getProductHistoryOneMonth);
        return ResponseEntity.ok(productHistoryGetDTOS);
    }

    public HttpEntity<?> getOneYear() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        List<ProductHistory> getProductHistoryOneYear = repository.
                getAllByDateBetween(new Timestamp(timestamp.getTime() - 31536000000L), timestamp, Sort.by("date").descending());
        List<ProductHistoryGetDTO> productHistoryGetDTOS = mapper.toGetDTOList(getProductHistoryOneYear);
        return ResponseEntity.ok(productHistoryGetDTOS);
    }

    public HttpEntity<?> create(ProductHistoryDTO productHistoryDTO) {
        if (productHistoryDTO.getProductId() != null) {
            Optional<Product> byId = productRepository.findById(productHistoryDTO.getProductId());
            if (byId.isPresent()) {
                Product product = byId.get();
                if (product.getAmount() >= productHistoryDTO.getAmount()) {
                    product.setAmount(product.getAmount() - productHistoryDTO.getAmount());
                    productRepository.save(product);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            (new ApiResponse(false, "NOT_ENOUGH_PRODUCT_QUANTITY")));
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                        (new ApiResponse(false, "NOT_FOUND_PRODUCT"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    (new ApiResponse(false, "PRODUCT_ID_SHOULD_NOT_BE_NULL"));
        }
        ProductHistory productHistory = mapper.toEntity(productHistoryDTO);
        repository.save(productHistory);
        return ResponseEntity.status(HttpStatus.OK).body
                (new ApiResponse(true, "PRODUCT_HISTORY_SAVED"));
    }

    public HttpEntity<?> edit(UUID id, ProductHistoryDTO productHistoryDTO) {
        //productHistory.getAmount va productHistoryDto.getAmount ning ayirmasidan qolgan son
        double remaining;

        if (productHistoryDTO.getProductId() != null) {
            Optional<ProductHistory> productHistoryById = repository.findById(id);

            if (productHistoryById.isPresent()) {
                ProductHistory productHistory = productHistoryById.get();

                Optional<Product> productRepositoryById = productRepository.
                        findByIdAndDeletedFalse(productHistoryDTO.getProductId());

                if (productRepositoryById.isPresent()) {
                    Product product = productRepositoryById.get();

                    if (productHistory.getAmount() > productHistoryDTO.getAmount()) {
                        remaining = productHistory.getAmount() - productHistoryDTO.getAmount();
                        product.setAmount(product.getAmount() + remaining);
                        productHistory.setAmount(productHistoryDTO.getAmount());
                    } else {
                        remaining = productHistoryDTO.getAmount() - productHistory.getAmount();
                        if (product.getAmount() >= remaining) {
                            product.setAmount(product.getAmount() - remaining);
                            productHistory.setAmount(productHistoryDTO.getAmount());
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                    (new ApiResponse(false, "NOT_ENOUGH_PRODUCT_QUANTITY")));
                        }
                    }
                    Optional<Client> clientRepositoryById = clientRepository.findById(productHistoryDTO.getClientId());
                    if (clientRepositoryById.isPresent()) {
                        Client client = clientRepositoryById.get();
                        productHistory.setProduct(product);
                        productHistory.setClient(client);
                        productHistory.setPrice(productHistoryDTO.getPrice());
                        ProductHistoryGetDTO historyDTO = mapper.toGetDTO(repository.save(productHistory));
                        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "EDITED_PRODUCT_HISTORY", historyDTO));
                    }
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "NOT_FOUND_CLIENT"));
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "NOT_FOUND_PRODUCT"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "NOT_FOUND_PRODUCT_HISTORY"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "NOT_FOUND_PRODUCT_HISTORY"));
    }
}
