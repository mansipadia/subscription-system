package com.example.subscription.unit;

import com.example.subscription.DTO.AddOnRequest;
import com.example.subscription.entity.AddOns;
import com.example.subscription.exception.ResourceNotFoundException;
import com.example.subscription.repository.AddOnRepository;
import com.example.subscription.service.impl.AddOnServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddOnServiceTest {

    @InjectMocks
    private AddOnServiceImpl addOnService;

    @Mock
    private AddOnRepository addOnRepository;

    @Test
    void shouldCreateAddOn(){
        AddOnRequest request = new AddOnRequest();
        request.setName("Training");
        request.setDescription("Extra Session");
        request.setUnitPrice(BigDecimal.valueOf(100));
        request.setUnitName("Session");

        AddOns savedAddOn = new AddOns();
        savedAddOn.setName("Training");
        savedAddOn.setDescription("Extra Session");
        savedAddOn.setUnitPrice(BigDecimal.valueOf(100));
        savedAddOn.setUnitName("Session");

        when(addOnRepository.save(any(AddOns.class))).thenReturn(savedAddOn);

        AddOns result = addOnService.createAddOn(request);

        assertNotNull(result);
        assertEquals("Training",result.getName());
        assertEquals(BigDecimal.valueOf(100),result.getUnitPrice());

        verify(addOnRepository).save(any(AddOns.class));
    }

    @Test
    void shouldReturnActiveAddOns(){

        AddOns addOns = new AddOns();
        addOns.setId(1L);
        addOns.setActive(true);

        AddOns addOns1 = new AddOns();
        addOns.setId(2L);
        addOns1.setActive(true);

        when(addOnRepository.findByActiveTrue()).thenReturn(List.of(addOns,addOns1));

        List<AddOns> result = addOnService.getActiveAddOns();

        assertEquals(2,result.size());
        verify(addOnRepository).findByActiveTrue();

    }

    @Test
    void shouldReturnAddOnById(){

        AddOns addOns = new AddOns();
        addOns.setId(1L);
        addOns.setUnitName("Session");

        when(addOnRepository.findById(addOns.getId())).thenReturn(Optional.of(addOns));

        AddOns result =addOnService.getById(1L);

        assertNotNull(result);
        assertEquals(1L,result.getId());
        verify(addOnRepository).findById(1L);

    }

    @Test
    void shouldThrowExceptionWhenAddOnNotFound(){

        when(addOnRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->addOnService.getById(2L));

        verify(addOnRepository).findById(2L);

    }



}
