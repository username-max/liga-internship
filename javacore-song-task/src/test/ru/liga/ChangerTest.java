package ru.liga;

import com.leff.midi.MidiFile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.liga.songtask.util.Changer;
import ru.liga.songtask.util.SongUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class ChangerTest {

    private static MidiFile midiFile;
    @BeforeClass
    public static void setup() throws IOException {
        midiFile = new MidiFile(new FileInputStream("C:\\Project\\Java\\liga-internship\\javacore-song-task\\src\\main\\resources\\Wrecking Ball.mid"));
    }

    @Test
    public void testChangeTempo() {
        double oldBpm = SongUtils.getTempo(midiFile).getBpm();
        double newBpm = SongUtils.getTempo(Changer.changeTempo(midiFile, 1)).getBpm();
        Assert.assertEquals(String.valueOf(newBpm), String.valueOf(oldBpm));
    }

    @Test
    public void testChangeMidi() {
        double newBpm = SongUtils.getTempo(Changer.changeMidi(midiFile, 10, 10)).getBpm();
        Assert.assertEquals(String.valueOf(newBpm), "132.0");


    }

    public void testTransposeMidi() {
    }
}
