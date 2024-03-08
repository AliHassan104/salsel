package com.salsel.service;

import com.salsel.dto.AddressBookDto;

import java.util.List;

public interface AddressBookService {
    AddressBookDto save(AddressBookDto addressBookDto);
    List<AddressBookDto> getAll(Boolean status);

    List<AddressBookDto> getAllByUserType(String userType);
    AddressBookDto findById(Long id);
    AddressBookDto findByUniqueId(String uniqueId);
    void deleteById(Long id);
    void setToActiveById(Long id);
    AddressBookDto update(Long id, AddressBookDto addressBookDto);
}
