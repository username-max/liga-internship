package ru.liga;

import com.leff.midi.MidiFile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.liga.songtask.util.SongUtils;

import java.io.FileInputStream;
import java.io.IOException;


public class SongUtilsTest {

    private static MidiFile midiFile;

    @BeforeClass
    public static void setup() throws IOException {
        midiFile = new MidiFile(new FileInputStream("C:\\Project\\Java\\liga-internship\\javacore-song-task\\src\\main\\resources\\Wrecking Ball.mid"));
    }

    @Test
    public void testEventsToNotes() {
        int count = SongUtils.eventsToNotes(midiFile.getTracks().get(3).getEvents()).size();
        Assert.assertEquals(count,71);
    }

    @Test
    public void testTickToMs() {
        int count = SongUtils.tickToMs(100f, 10,1000);
        Assert.assertEquals(count,60000);
    }

    @Test
    public void testGetCountVoiceTrack() {
        int count = SongUtils.getVoiceTrack(midiFile).size();
        Assert.assertEquals(count,402);
    }

    @Test
    public void testGetCountVoiceTracks() {
        int count = SongUtils.getVoiceTracks(midiFile).size();
        Assert.assertEquals(count,2);
    }

    @Test
    public void testGetTempoBPM() {
        float bpm = SongUtils.getTempo(midiFile).getBpm();
        Assert.assertEquals(String.valueOf(bpm),"120.0");
    }
}