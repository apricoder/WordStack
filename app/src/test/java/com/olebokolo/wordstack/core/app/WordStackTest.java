package com.olebokolo.wordstack.core.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WordStackTest {

    @Spy
    private WordStack wordStack;

    @Test
    public void should_init_fields_without_exceptions() throws Exception {
        wordStack.initFields();
    }
}