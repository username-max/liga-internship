package ru.liga;


import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.songtask.domain.Note;
import ru.liga.songtask.domain.NoteSign;
import ru.liga.songtask.util.Analyzer;
import ru.liga.songtask.util.Changer;
import ru.liga.songtask.util.SongUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

public class App {

    public static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        logger.trace("Start program");
        MidiFile midiFile = new MidiFile(new FileInputStream(
                "C:\\Project\\Java\\Стажировка\\Домашниее задания\\liga-internship\\javacore-song-task" +
                        "\\src\\main\\resources\\Belle.mid"));
//        for (String str : args){
//            if (args[1].equals("analyze")){
//                Analyzer.start(midiFile);
//            }
//            if (args[1].equals("change")){
//                Changer.start(midiFile);
//            }
//        }

        Analyzer.start(midiFile);
        MidiTrack track= SongUtils.getVoiceTrack(midiFile);
        System.out.println(track.getSize());


    }

}
