package com.olebokolo.wordstack.core.languages.flags;

import com.olebokolo.wordstack.core.resources.drawables.DrawableService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FlagServiceImplTest {

    @Mock private DrawableService drawableService;
    @Spy @InjectMocks private FlagServiceImpl flagService;

    private String shortName = "en";
    private String fileName = "flag_en.png";

    @Test
    public void should_prepare_filename_and_ask_drawable_service_for_image() throws Exception {
        flagService.getFlagByLanguageShortName(shortName);
        verify(flagService).getFileName(shortName);
        verify(drawableService).getDrawableByName(fileName);
    }

    @Test
    public void should_get_image_name() throws Exception {
        assertEquals(fileName, flagService.getFileName(shortName));
    }
}