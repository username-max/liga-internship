package ru.liga;

import com.leff.midi.MidiFile;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.liga.songtask.util.Analyzer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class AnalyzerTest {
    public static Analyzer analyzer;
    public static MidiFile midiFile;

    @BeforeClass
    public static void setup() throws IOException {
        midiFile = new MidiFile(new FileInputStream("C:\\Project\\Java\\liga-internship\\javacore-song-task\\" +
                "src\\main\\resources\\Wrecking Ball.mid"));
        analyzer = new Analyzer(midiFile);
    }

    @Test
    public void testAnalyzeDiapozon() {
        Assert.assertEquals(analyzer.analyzisDiapozon().toString(), "{0=A#4, 1=F3, 2=17}");
    }

    @Test
    public void testAnalyzeDuration() {
        Assert.assertEquals(analyzer.analyzisDuration().toString(), "{56640000=218, 86400000=4, 28800000=9, 85440000=33, 115200000=40, 57600000=10, 171840000=16, 114240000=72}");
    }

    @Test
    public void testAnalyzeHeigh() {
        Assert.assertEquals(analyzer.analyzisHeigh().toString(), "{D4=48, C4=33, A#3=5, A3=15, A#4=79, A4=82, F3=5, G4=24, F4=88, E4=23}");
    }

}
