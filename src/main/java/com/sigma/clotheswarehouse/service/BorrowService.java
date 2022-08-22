package com.sigma.clotheswarehouse.service;

import com.sigma.clotheswarehouse.entity.Borrow;
import com.sigma.clotheswarehouse.entity.Client;
import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.BorrowGetDto;
import com.sigma.clotheswarehouse.payload.BorrowPostDto;
import com.sigma.clotheswarehouse.payload.BorrowUpdateDto;
import com.sigma.clotheswarehouse.repository.BorrowRepository;
import com.sigma.clotheswarehouse.repository.ClientRepository;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BorrowService {

    final private BorrowRepository borrowRepository;

    final private ClientRepository clientRepository;


    public ApiResponse addBorrow(BorrowPostDto borrowPostDto) {
        Double sum = 0.0;
        Optional<Client> client = clientRepository.findById(borrowPostDto.getClientId());
        if (client.isEmpty()) {
            return new ApiResponse(false, "Such a client does not exist");
        }
        List<Borrow> borrowList = borrowRepository.findByClient_IdAndDeletedIsFalse(client.get().getId());
        for (Borrow borrow : borrowList) {
            sum += borrow.getAmount();
        }
        if ((sum + borrowPostDto.getAmount() > AppConstant.borrow)) {
            return new ApiResponse(false, "Clientni qarzi belgilangan " +
                    "miqdordan oshib ketdi. Saqlash mumkin emas");
        }
        if (borrowPostDto.getBeginDate().after(borrowPostDto.getEndDate())) {
            return new ApiResponse(false, "Date is not right");
        }
        Borrow borrow = new Borrow();
        borrow.setClient(client.get());
        borrow.setAmount(borrowPostDto.getAmount());
        borrow.setBeginDate(borrowPostDto.getBeginDate());
        borrow.setEndDate(borrowPostDto.getEndDate());
        borrow.setDeleted(false);
        Borrow savedBorrow = borrowRepository.save(borrow);
        return new ApiResponse(true, "Successfully saved", savedBorrow.getId());
    }

    public ApiResponse getById(UUID id) {
        Optional<Borrow> optionalBorrow = borrowRepository.findById(id);
        if (optionalBorrow.isEmpty()) {
            return new ApiResponse(false, "Such a borrow does not exist");
        }
        Borrow borrow = optionalBorrow.get();
        BorrowGetDto borrowGetDto = new BorrowGetDto();
        borrowGetDto.setId(borrow.getId());
        borrowGetDto.setClientId(borrow.getClient().getId());
        borrowGetDto.setClientFIO(borrow.getClient().getFio());
        borrowGetDto.setAmount(borrow.getAmount());
        borrowGetDto.setBeginDate(borrow.getBeginDate());
        borrowGetDto.setEndDate(borrow.getEndDate());
        borrowGetDto.setDeleted(borrow.isDeleted());
        return new ApiResponse(true, "Success", borrowGetDto);
    }

    public ApiResponse getBorrows() {
        List<Borrow> borrowList = borrowRepository.findByDeletedFalse();
        List<BorrowGetDto> borrowGetDtoList = new LinkedList<>();
        if (borrowList.isEmpty()) {
            return new ApiResponse(false, "There isn't borrow");
        }
        for (Borrow borrow : borrowList) {
            if (!borrow.getClient().isDeleted()) {
                BorrowGetDto borrowGetDto = new BorrowGetDto();
                borrowGetDto.setId(borrow.getId());
                borrowGetDto.setClientId(borrow.getClient().getId());
                borrowGetDto.setClientFIO(borrow.getClient().getFio());
                borrowGetDto.setAmount(borrow.getAmount());
                borrowGetDto.setBeginDate(borrow.getBeginDate());
                borrowGetDto.setEndDate(borrow.getEndDate());
                borrowGetDtoList.add(borrowGetDto);
            }
        }
        return new ApiResponse(true, "Success", borrowGetDtoList);
    }

    public ApiResponse updateBorrow(UUID id, BorrowUpdateDto borrowUpdateDto) {
        Optional<Borrow> optionalBorrow = borrowRepository.findById(id);
        if (optionalBorrow.isEmpty()) {
            return new ApiResponse(false, "Such a borrow does not exist");
        }
        Borrow borrow = optionalBorrow.get();
        Double sum = 0.0;
        Optional<Client> client = clientRepository.findById(borrowUpdateDto.getClientId());
        if (client.isEmpty()) {
            return new ApiResponse(false, "Such a client does not exist");
        }
        List<Borrow> borrowList = borrowRepository.findByClient_IdAndDeletedIsFalse(client.get().getId());
        for (Borrow borrow1 : borrowList) {
            sum += borrow1.getAmount();
        }
        if ((sum + borrowUpdateDto.getAmount() > AppConstant.borrow)) {
            return new ApiResponse(false, "Clientni qarzi belgilangan " +
                    "miqdordan oshib ketdi. O'zgartirish amalga oshmadi");
        }
        if (borrowUpdateDto.getBeginDate().after(borrowUpdateDto.getEndDate())) {
            return new ApiResponse(false, "Date is not right");
        }
        borrow.setClient(client.get());
        borrow.setAmount(borrowUpdateDto.getAmount());
        borrow.setBeginDate(borrowUpdateDto.getBeginDate());
        borrow.setEndDate(borrowUpdateDto.getEndDate());
        borrowRepository.save(borrow);
        return new ApiResponse(true, "Successfully updated");
    }


    public ApiResponse deleteClientBorrow(Long id) {
        List<Borrow> borrow = borrowRepository.findByClient_IdAndDeletedIsFalseOrderByBeginDateAsc(id);
        if (borrow == null) {
            return new ApiResponse(false, "Client doesn't have borrow");
        }
        for (Borrow borrow1 : borrow) {
            borrow1.setDeleted(true);
            borrowRepository.save(borrow1);
        }
        return new ApiResponse(true, "Clients borrow successfully deleted");
    }
}
