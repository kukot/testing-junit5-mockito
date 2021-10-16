package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService specialitySDJpaService;

    @Test
    void delete() {
        Speciality sp = new Speciality();
        specialitySDJpaService.delete(sp);
        specialitySDJpaService.delete(sp);
        verify(specialtyRepository, times(2)).delete(sp);
        verify(specialtyRepository, never()).deleteById(1L);
    }

    @Test
    void deleteById() {
        specialitySDJpaService.deleteById(1L);
    }

    @Test
    void deleteByIdAtLeast() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, atLeast(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtMost() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, atMost(3)).deleteById(1L);
        verify(specialtyRepository, never()).delete(new Speciality());
    }

    @Test
    void testFindById() {
        Speciality speciality = new Speciality(1L, "One");
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(speciality));
        Speciality sp = specialitySDJpaService.findById(1L);
        verify(specialtyRepository, times(1)).findById(1L);
        assertEquals(sp,speciality);
    }

    @Test
    void testVerifyByMatcher() {
        Speciality sp = new Speciality(1L, "First");
        specialitySDJpaService.delete(sp);
        verify(specialtyRepository, times(1)).delete(any(Speciality.class));
        specialitySDJpaService.deleteById(2L);
        verify(specialtyRepository, times(1)).deleteById(anyLong());
    }
}