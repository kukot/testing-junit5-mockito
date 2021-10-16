package guru.springframework.sfgpetclinic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class JunitMockitoExtension {

    @Mock
    Map<String, Object> mockMap;

    @Test
    public void testMock() {
        mockMap.put("Mot", 1);
    }
}
