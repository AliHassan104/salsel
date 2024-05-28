package com.salsel.controller;

import com.salsel.dto.AddressBookDto;
import com.salsel.dto.AwbDto;
import com.salsel.service.AddressBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressBookController {
    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @PostMapping("/address-book")
    @PreAuthorize("hasAuthority('CREATE_ADDRESS_BOOK') and hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<AddressBookDto> createAddressBook(@RequestBody AddressBookDto addressBookDto) {
        return ResponseEntity.ok(addressBookService.save(addressBookDto));
    }

    @GetMapping("/address-book")
    @PreAuthorize("hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<List<AddressBookDto>> getAllAddressBook(@RequestParam(value = "status") Boolean status) {
        List<AddressBookDto> addressBookDtoList = addressBookService.getAll(status);
        return ResponseEntity.ok(addressBookDtoList);
    }

    @GetMapping("/address-book/user-type")
    @PreAuthorize("hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<List<AddressBookDto>> getAllAddressBookByUserType(@RequestParam(value = "userType") String userType,@RequestParam(value = "status") Boolean status) {
        List<AddressBookDto> addressBookDtoList = addressBookService.getAllByUserType(userType,status);
        return ResponseEntity.ok(addressBookDtoList);
    }

    @GetMapping("/address-book/logged-in-user")
    @PreAuthorize("hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<List<AddressBookDto>> getAllAddressByLoggedInUser(@RequestParam(value = "status") Boolean status,@RequestParam(value = "userType", required = false) String userType) {
        List<AddressBookDto> addressBookDtoList = addressBookService.getAddressBookByLoggedInUser(status,userType);
        return ResponseEntity.ok(addressBookDtoList);
    }

    @GetMapping("/address-book/account-number")
    @PreAuthorize("hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<List<AddressBookDto>> getAllAddressByAccountNumber(@RequestParam(value = "status") Boolean status,@RequestParam(value = "userType", required = false) String userType,@RequestParam(value = "accountNumber") String accountNumber) {
        List<AddressBookDto> addressBookDtoList = addressBookService.getAllByAccountNumber(accountNumber,status,userType);
        return ResponseEntity.ok(addressBookDtoList);
    }

    @GetMapping("/address-book/check-unique-id")
    @PreAuthorize("hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<String> checkUniqueIdExists(@RequestParam("uniqueId") String uniqueId,@RequestParam("shipperUniqueId") String shipperUniqueId) {
        String res = addressBookService.checkUniqueIdExistsShipperOrRecipient(uniqueId,shipperUniqueId);
        System.out.println(res);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/address-book/{id}")
    @PreAuthorize("hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<AddressBookDto> getAddressBookById(@PathVariable Long id) {
        AddressBookDto addressBookDto = addressBookService.findById(id);
        return ResponseEntity.ok(addressBookDto);
    }

    @GetMapping("/address-book/unique-id/{id}")
    @PreAuthorize("hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<AddressBookDto> getAddressBookByUniqueId(@PathVariable String id) {
        AddressBookDto addressBookDto = addressBookService.findByUniqueId(id);
        return ResponseEntity.ok(addressBookDto);
    }


    @PutMapping("/address-book/status/{id}")
    @PreAuthorize("hasAuthority('DELETE_ADDRESS_BOOK')")
    public ResponseEntity<Void> updateAddressStatusToActive(@PathVariable Long id) {
        addressBookService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/address-book/{id}")
    @PreAuthorize("hasAuthority('DELETE_ADDRESS_BOOK') and hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<Void> deleteAddressBook(@PathVariable Long id) {
        addressBookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/address-book/{id}")
    @PreAuthorize("hasAuthority('CREATE_ADDRESS_BOOK') and hasAuthority('READ_ADDRESS_BOOK')")
    public ResponseEntity<AddressBookDto> updateAddressBook(@PathVariable Long id, @RequestBody AddressBookDto addressBookDto) {
        AddressBookDto updatedAddressBookDto = addressBookService.update(id, addressBookDto);
        return ResponseEntity.ok(updatedAddressBookDto);
    }
}
