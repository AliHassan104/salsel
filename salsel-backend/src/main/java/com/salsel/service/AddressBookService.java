package com.salsel.service;

import com.salsel.dto.AddressBookDto;
import com.salsel.dto.AwbDto;

import java.util.List;

public interface AddressBookService {
    AddressBookDto save(AddressBookDto addressBookDto);
    List<AddressBookDto> getAll(Boolean status);

    List<AddressBookDto> getAddressBookByLoggedInUser(Boolean status,String userType);

    String checkUniqueIdExistsShipperOrRecipient(String uniqueId,String shipperUniqueId);

    List<AddressBookDto> getAllByUserType(String userType,Boolean status);

    List<AddressBookDto> getAllByAccountNumber(String accountNumber,Boolean status,String userType);
    AddressBookDto findById(Long id);
    AddressBookDto findByUniqueId(String uniqueId);
    void deleteById(Long id);
    void setToActiveById(Long id);
    AddressBookDto update(Long id, AddressBookDto addressBookDto);

}
