package com.sigma.clotheswarehouse.service;

import com.sigma.clotheswarehouse.entity.Borrow;
import com.sigma.clotheswarehouse.entity.Client;
import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.ClientGetDto;
import com.sigma.clotheswarehouse.payload.ClientPostDto;
import com.sigma.clotheswarehouse.payload.ClientUpdateDto;
import com.sigma.clotheswarehouse.repository.BorrowRepository;
import com.sigma.clotheswarehouse.repository.ClientRepository;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {


    private final ClientRepository clientRepository;

    private final BorrowRepository borrowRepository;

    //Success post
    public ApiResponse addClient(ClientPostDto clientPostDto) {
        Optional<Client> clientRepositoryByPhoneNumber = clientRepository.findByPhoneNumber(clientPostDto.getPhoneNumber());
        System.out.println(clientRepositoryByPhoneNumber);
        if (clientRepositoryByPhoneNumber.isPresent()) {
            return new ApiResponse(false, "Phone number is exist");
        }
        if (clientPostDto.getBorrowAmount() > AppConstant.borrow) {
            return new ApiResponse(false, "Borrow more than limit");
        } else if (clientPostDto.getBorrowAmount() < 0) {
            return new ApiResponse(false, "Borrow must not minus");
        }
        Client client = new Client(clientPostDto.getFio(), clientPostDto.getPhoneNumber(),
                clientPostDto.getAddress(), false);
        Client saveClient = clientRepository.save(client);
        if (clientPostDto.getBorrowAmount() > 0) {
            Borrow borrow = new Borrow(clientPostDto.getBorrowAmount(), clientPostDto.getBeginDate(), clientPostDto.getEndDate(), false, saveClient);
            borrowRepository.save(borrow);
        }
        return new ApiResponse(true, "Successfully saved", saveClient.getId());
    }


    //Success
    public ApiResponse getById(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isEmpty()) {
            return new ApiResponse(false, "Such a client does not exist");
        }
        List<Borrow> borrow = borrowRepository.getBorrowListByClient_Id(id, false);
        Double sum = 0.0;
        for (Borrow borrow1 : borrow) {
            sum += borrow1.getAmount();
        }
        Client client = clientOptional.get();
        ClientGetDto clientGetDto = new ClientGetDto();
        if (!borrow.isEmpty()) {
            clientGetDto.setId(client.getId());
            clientGetDto.setFio(client.getFio());
            clientGetDto.setPhoneNumber(client.getPhoneNumber());
            clientGetDto.setAddress(client.getAddress());
            clientGetDto.setBorrowAmount(sum);
            clientGetDto.setBeginDate(borrow.get(0).getBeginDate());
            clientGetDto.setEndDate(borrow.get(0).getEndDate());
            clientGetDto.setDeleted(client.isDeleted());
        } else {
            clientGetDto.setId(client.getId());
            clientGetDto.setFio(client.getFio());
            clientGetDto.setPhoneNumber(client.getPhoneNumber());
            clientGetDto.setAddress(client.getAddress());
            clientGetDto.setBorrowAmount(0.0);
            clientGetDto.setBeginDate(null);
            clientGetDto.setEndDate(null);
            clientGetDto.setDeleted(client.isDeleted());
        }

        return new ApiResponse(true, "Success", clientGetDto);
    }

    public ApiResponse getClients() {
        List<Client> clientList = clientRepository.findAll();
        List<ClientGetDto> clientGetDtoList = new LinkedList<>();
        for (Client client : clientList) {
            List<Borrow> borrow = borrowRepository.getBorrowListByClient_Id(client.getId(), false);
            ClientGetDto clientGetDto = new ClientGetDto();
            if (borrow.isEmpty()) {
                clientGetDto.setId(client.getId());
                clientGetDto.setFio(client.getFio());
                clientGetDto.setPhoneNumber(client.getPhoneNumber());
                clientGetDto.setAddress(client.getAddress());
                clientGetDto.setBorrowAmount(0.0);
                clientGetDto.setBeginDate(null);
                clientGetDto.setEndDate(null);
                clientGetDto.setDeleted(client.isDeleted());
            } else {
                Double sum = 0.0;
                for (Borrow borrow1 : borrow) {
                    sum += borrow1.getAmount();
                }
                clientGetDto.setId(client.getId());
                clientGetDto.setFio(client.getFio());
                clientGetDto.setPhoneNumber(client.getPhoneNumber());
                clientGetDto.setAddress(client.getAddress());
                clientGetDto.setBorrowAmount(sum);
                clientGetDto.setBeginDate(borrow.get(0).getBeginDate());
                clientGetDto.setEndDate(borrow.get(0).getEndDate());
                clientGetDto.setDeleted(client.isDeleted());
            }
            clientGetDtoList.add(clientGetDto);
        }
        return new ApiResponse(true, "All Clients", clientGetDtoList);
    }

    public ApiResponse updateClient(long id, ClientUpdateDto clientUpdateDto) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isEmpty()) {
            return new ApiResponse(false, "Such a client does not exist");
        }
        Optional<Client> optionalClient = clientRepository.findByPhoneNumber(clientUpdateDto.getPhoneNumber());
        if (optionalClient.isPresent()) {
            Client clientByPhone = optionalClient.get();
            if (id != clientByPhone.getId()) {
                return new ApiResponse(false, "Phone number is exist");
            }
        }

        Client client = clientOptional.get();
        client.setFio(clientUpdateDto.getFio());
        client.setPhoneNumber(clientUpdateDto.getPhoneNumber());
        client.setAddress(clientUpdateDto.getAddress());
        clientRepository.save(client);
        return new ApiResponse(true, "Client updated successfully");
    }

    public ApiResponse deleteClient(Long id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isEmpty()) {
            return new ApiResponse(false, "Such a material does not exist");
        }
        Client client = clientOptional.get();
        client.setDeleted(true);
        clientRepository.save(client);
        return new ApiResponse(true, "Client successfully deleted");
    }


}
