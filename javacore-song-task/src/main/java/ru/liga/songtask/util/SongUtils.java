package ru.liga.songtask.util;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.songtask.domain.Note;
import ru.liga.songtask.domain.NoteSign;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SongUtils {

    public static Logger logger = LoggerFactory.getLogger(SongUtils.class);

    /**
     * Перевод тиков в миллисекунды
     * @param bpm - количество ударов в минуту (темп)
     * @param resolution - midiFile.getResolution()
     * @param amountOfTick - то что переводим в миллисекунды
     * @return
     */
    public static int tickToMs(float bpm, int resolution, long amountOfTick) {
        return (int) (((60 * 1000) / (bpm * resolution)) * amountOfTick);
    }

    /**
     * Евенты в ноты
     * @param events
     * @return vbNotes
     */
    public static List<Note> eventsToNotes(TreeSet<MidiEvent> events) {
        List<Note> vbNotes = new ArrayList<>();

        Queue<NoteOn> noteOnQueue = new LinkedBlockingQueue<>();
        for (MidiEvent event : events) {
            if (event instanceof NoteOn || event instanceof NoteOff) {
                if (isEndMarkerNote(event)) {
                    NoteSign noteSign = NoteSign.fromMidiNumber(extractNoteValue(event));
                    if (noteSign != NoteSign.NULL_VALUE) {
                        NoteOn noteOn = noteOnQueue.poll();
                        if (noteOn != null) {
                            long start = noteOn.getTick();
                            long end = event.getTick();
                            vbNotes.add(
                                    new Note(noteSign, start, end - start));
                        }
                    }
                } else {
                    noteOnQueue.offer((NoteOn) event);
                }
            }
        }
        return vbNotes;
    }


    /**
     * Извлечение значения из ноты
     * @param event
     * @return
     */
    private static Integer extractNoteValue(MidiEvent event) {
        if (event instanceof NoteOff) {
            return ((NoteOff) event).getNoteValue();
        } else if (event instanceof NoteOn) {
            return ((NoteOn) event).getNoteValue();
        } else {
            return null;
        }
    }

    /**
     *
     * @param event
     * @return
     */
    private static boolean isEndMarkerNote(MidiEvent event) {
        if (event instanceof NoteOff) {
            return true;
        } else if (event instanceof NoteOn) {
            return ((NoteOn) event).getVelocity() == 0;
        } else {
            return false;
        }

    }

    public static MidiTrack getVoiceTrack(MidiFile midiFile){
        logger.trace("foo getVoiceTrack start");
        List<MidiTrack> midiTrackList = getMidiTracksWithoutATextTrack(midiFile.getTracks());
        MidiTrack textTrack = findTextTrack(midiTrackList);
        return findTrackEqualToThisOneByTick(midiTrackList, textTrack);
    }

    private static boolean trackIsText(MidiTrack midiTrack){
        return midiTrack.getEvents().stream()
                .anyMatch(e -> e instanceof Text);
    }

    private static MidiTrack findTextTrack(List<MidiTrack> tracks){
        for (MidiTrack track : tracks){
            if (trackIsText(track)) {
                return track;
            }
        }
        return null;
    }

    private static List<MidiTrack> getMidiTracksWithoutATextTrack(List<MidiTrack> tracks){
        return tracks.stream()
                .skip(2)
                .filter(track -> !trackIsText(track))
                .collect(Collectors.toList());
    }

    private static List<Long> getTickFromTrack(MidiTrack track){
        return track.getEvents().stream()
                .map(midiEvent -> midiEvent.getTick())
                .filter(tick -> tick != 0)
                .collect(Collectors.toList());
    }

    private static MidiTrack findTrackEqualToThisOneByTick(List<MidiTrack> tracks, MidiTrack elementaryTrack){
        List<Long> tickFromElementaryTrack = getTickFromTrack(elementaryTrack);
        List<Long> tickFromListTracks;

        for (MidiTrack track : tracks){
            tickFromListTracks = getTickFromTrack(track);
            if (equalTicks(tickFromElementaryTrack, tickFromListTracks)){
                return track;
            }
        }
        return null;

    }

    private static boolean equalTicks(List<Long> firstList, List<Long> secondList){
        boolean flag = false;
        for (Long tickFromFirst : firstList){
            for (Long tickFromSecond : secondList){
                if (tickFromFirst == tickFromSecond){
                    flag = true;
                }else {
                    flag = false;
                }
            }
        }

        return flag;
    }


}
