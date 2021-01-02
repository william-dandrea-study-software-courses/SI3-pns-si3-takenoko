package fr.matelots.polytech.core.players.bots;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MathsTestsMockito {

    int add(int a, int b) {
        return a+b;
    }
    int sub(int a, int b) {
        return a-b;
    }
    int mul(int a, int b) {
        return a*b;
    }
    int div(int a, int b) {
        return a/b;
    }
}
public class MockitoTests {

    MathsTestsMockito mathObj;

    @Before
    public void init() {
        mathObj = mock(MathsTestsMockito.class);
        when(mathObj.add(2,2)).thenReturn(3);
    }

    @Test
    public void test() throws Exception{
        assertSame(3, mathObj.add(2,2));

    }
}
