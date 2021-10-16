package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.atLeast;
import static org.mockito.BDDMockito.atLeastOnce;
import static org.mockito.BDDMockito.atMost;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.when;
import static org.mockito.BDDMockito.willThrow;

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
        assertEquals(sp, speciality);
    }

    @Test
    void testVerifyByMatcher() {
        Speciality sp = new Speciality(1L, "First");
        specialitySDJpaService.delete(sp);
        verify(specialtyRepository, times(1)).delete(any(Speciality.class));
        specialitySDJpaService.deleteById(2L);
        verify(specialtyRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void usingMockitoBdd() {
        Speciality sp1 = new Speciality(1L, "Mot");
        Set<Speciality> specialities = Set.of(
                sp1,
                new Speciality(2L, "Hai"),
                new Speciality(3L, "Ba")
        );
        given(specialtyRepository.findAll()).willReturn(specialities);
        given(specialtyRepository.findById(1L)).willReturn(specialities.stream().filter(it -> it.getId() == 1L).findFirst());
        Set<Speciality> result = specialitySDJpaService.findAll();
        Speciality sp = specialitySDJpaService.findById(1L);
        assertThat(result).hasSize(3);
//        verify(specialtyRepository, times(1)).findAll();

        then(specialtyRepository).should().findAll();
        then(specialtyRepository).should(times(1)).findById(1L);
        assertEquals(sp, sp1);
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void throwForFun() {
        doThrow(RuntimeException.class).when(specialtyRepository).deleteById(any(Long.class));
        assertThrows(RuntimeException.class, () -> specialitySDJpaService.deleteById(1L));
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
    }

    @Test
    void throwWhenFind() {
        given(specialtyRepository.findById(Long.MAX_VALUE)).willThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> specialitySDJpaService.findById(Long.MAX_VALUE));
        then(specialtyRepository).should().findById(Long.MAX_VALUE);
    }

    @Test
    void throwWithBDD() {
        willThrow(new IllegalArgumentException("BOOM")).given(specialtyRepository).deleteById(anyLong());
        assertThrows(IllegalArgumentException.class, () -> specialitySDJpaService.deleteById(Long.MIN_VALUE));
        then(specialtyRepository).should().deleteById(Long.MIN_VALUE);
    }
}