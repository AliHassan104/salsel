package com.salsel.service.impl;

import com.salsel.dto.AddressBookDto;
import com.salsel.dto.AwbDto;
import com.salsel.dto.CustomUserDetail;
import com.salsel.exception.AddressBookDetailsAlreadyExist;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.AddressBook;
import com.salsel.model.Awb;
import com.salsel.model.User;
import com.salsel.repository.AddressBookRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.AddressBookService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    private final AddressBookRepository addressBookRepository;
    private final UserRepository userRepository;

    public AddressBookServiceImpl(AddressBookRepository addressBookRepository, UserRepository userRepository) {
        this.addressBookRepository = addressBookRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AddressBookDto save(AddressBookDto addressBookDto) {
        AddressBook addressBook = toEntity(addressBookDto);
        addressBook.setStatus(true);
        if (!addressBook.getName().isEmpty() || !addressBook.getContactNumber().isEmpty() || !addressBook.getUserType().isEmpty()) {
            String uniqueKey = addressBook.getName() + "-" + addressBook.getContactNumber() + "-" + addressBook.getUserType();
            addressBook.setUniqueId(uniqueKey);
        } else {
            throw new RecordNotFoundException("Name, ContactNumber and UserType can not be null");
        }

        if(addressBookRepository.existsByUniqueId(addressBook.getUniqueId())){
            throw new AddressBookDetailsAlreadyExist("Address with that details already exist");
        }
        else{
            AddressBook savedAddressBook = addressBookRepository.save(addressBook);
            return toDto(savedAddressBook);
        }
    }

    @Override
    public List<AddressBookDto> getAll(Boolean status) {
        List<AddressBook> addressBooks = addressBookRepository.findAllInDesOrderByIdAndStatus(status);
        return addressBooks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressBookDto> getAddressBookByLoggedInUser(Boolean status, String userType) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetail) {
            String email = ((CustomUserDetail) principal).getEmail();
            User user = userRepository.findByEmailAndStatusIsTrue(email)
                    .orElseThrow(() -> new RecordNotFoundException("User not found"));

            List<AddressBook> addressBookList = addressBookRepository.findAllInDesOrderByCreatedByAndStatus(status, userType, user.getEmail());
            List<AddressBookDto> addressBookDtoList = new ArrayList<>();

            for (AddressBook addressBook : addressBookList) {
                AddressBookDto addressBookDto = toDto(addressBook);
                addressBookDtoList.add(addressBookDto);
            }
            return addressBookDtoList;
        }
        return null;
    }

    @Override
    public String checkUniqueIdExistsShipperOrRecipient(String uniqueId,String shipperUniqueId) {
        boolean existsForShipper = addressBookRepository.existsByUniqueIdShipperOrRecipient(shipperUniqueId, "Shipper");
        boolean existsForRecipient = addressBookRepository.existsByUniqueIdShipperOrRecipient(uniqueId, "Recipient");

        if (existsForShipper && existsForRecipient) {
            return "both";
        } else if (existsForShipper) {
            return "shipper";
        } else if (existsForRecipient) {
            return "recipient";
        } else {
            return "Not Exist";
        }
    }

    @Override
    public List<AddressBookDto> getAllByUserType(String userType,Boolean status) {
        List<AddressBook> addressBooks = addressBookRepository.findAllInDesOrderByUserTypeAndStatus(userType,status);
        return addressBooks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressBookDto> getAllByAccountNumber(String accountNumber, Boolean status, String userType) {
        List<AddressBook> addressBooks = addressBookRepository.findAllInDesOrderByAccountNumberAndStatus(status,userType,accountNumber);
        return addressBooks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressBookDto findById(Long id) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Record not found for id => %d", id)));
        return toDto(addressBook);
    }

    @Override
    public AddressBookDto findByUniqueId(String uniqueId) {
        AddressBook addressBook = addressBookRepository.findByUniqueId(uniqueId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Record not found for uniqueId => %s", uniqueId)));
        return toDto(addressBook);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Record not found for id => %d", id)));
        addressBookRepository.setStatusInactive(addressBook.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        AddressBook addressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Record not found for id => %d", id)));
        addressBookRepository.setStatusActive(addressBook.getId());
    }

    @Override
    @Transactional
    public AddressBookDto update(Long id, AddressBookDto addressBookDto) {
        AddressBook existingAddressBook = addressBookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Record not found for id => %d", id)));

        boolean shouldUpdateUniqueId = !existingAddressBook.getName().equalsIgnoreCase(addressBookDto.getName()) ||
                !existingAddressBook.getContactNumber().equalsIgnoreCase(addressBookDto.getContactNumber()) ||
                !existingAddressBook.getUserType().equalsIgnoreCase(addressBookDto.getUserType());

        if (shouldUpdateUniqueId) {
            String uniqueKey = addressBookDto.getName() + "-" + addressBookDto.getContactNumber() + "-" + addressBookDto.getUserType();
            if (addressBookRepository.existsByUniqueId(uniqueKey)) {
                throw new AddressBookDetailsAlreadyExist("Address with that details already exists");
            }
            existingAddressBook.setUniqueId(uniqueKey);
        }

        existingAddressBook.setName(addressBookDto.getName());
        existingAddressBook.setContactNumber(addressBookDto.getContactNumber());
        existingAddressBook.setCountry(addressBookDto.getCountry());
        existingAddressBook.setCity(addressBookDto.getCity());
        existingAddressBook.setAddress(addressBookDto.getAddress());
        existingAddressBook.setStreetName(addressBookDto.getStreetName());
        existingAddressBook.setDistrict(addressBookDto.getDistrict());
        existingAddressBook.setRefNumber(addressBookDto.getRefNumber());
        existingAddressBook.setUserType(addressBookDto.getUserType());
        existingAddressBook.setAccountNumber(addressBookDto.getAccountNumber());

        AddressBook updatedAddressBook = addressBookRepository.save(existingAddressBook);
        return toDto(updatedAddressBook);
    }




    public AddressBookDto toDto(AddressBook addressBook) {
        return AddressBookDto.builder()
                .id(addressBook.getId())
                .createdAt(addressBook.getCreatedAt())
                .name(addressBook.getName())
                .city(addressBook.getCity())
                .country(addressBook.getCountry())
                .address(addressBook.getAddress())
                .contactNumber(addressBook.getContactNumber())
                .refNumber(addressBook.getRefNumber())
                .streetName(addressBook.getStreetName())
                .uniqueId(addressBook.getUniqueId())
                .userType(addressBook.getUserType())
                .district(addressBook.getDistrict())
                .status(addressBook.getStatus())
                .createdBy(addressBook.getCreatedBy())
                .accountNumber(addressBook.getAccountNumber())
                .build();
    }

    public AddressBook toEntity(AddressBookDto addressBookDto) {
        return AddressBook.builder()
                .id(addressBookDto.getId())
                .createdAt(addressBookDto.getCreatedAt())
                .name(addressBookDto.getName())
                .city(addressBookDto.getCity())
                .country(addressBookDto.getCountry())
                .address(addressBookDto.getAddress())
                .contactNumber(addressBookDto.getContactNumber())
                .refNumber(addressBookDto.getRefNumber())
                .streetName(addressBookDto.getStreetName())
                .uniqueId(addressBookDto.getUniqueId())
                .userType(addressBookDto.getUserType())
                .district(addressBookDto.getDistrict())
                .status(addressBookDto.getStatus())
                .createdBy(addressBookDto.getCreatedBy())
                .accountNumber(addressBookDto.getAccountNumber())
                .build();
    }
}
