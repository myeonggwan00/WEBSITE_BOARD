package com.example.firstproject;

import com.example.firstproject.domain.PageHandler;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PageHandlerTest {
    @Test
    public void pagingTest1() {
        PageHandler ph = new PageHandler(250, 1);
        assertThat(ph.getBeginPage()).isEqualTo(1);
        assertThat(ph.getEndPage()).isEqualTo(10);
        ph.print();
    }

    @Test
    public void pagingTest2() {
        PageHandler ph = new PageHandler(250, 15);
        assertThat(ph.getBeginPage()).isEqualTo(11);
        assertThat(ph.getEndPage()).isEqualTo(20);
        ph.print();
    }

    @Test
    public void pagingTest3() {
        PageHandler ph = new PageHandler(255, 23);
        assertThat(ph.getBeginPage()).isEqualTo(21);
        assertThat(ph.getEndPage()).isEqualTo(26);
        ph.print();
    }
}
