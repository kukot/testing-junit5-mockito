package guru.springframework.sfgpetclinic;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class InlineMockTest {

    @Test
    public void testMap() {
        Map map = mock(Map.class);
        System.out.println(map.getClass());
        assertEquals(0, map.size());
    }
}
