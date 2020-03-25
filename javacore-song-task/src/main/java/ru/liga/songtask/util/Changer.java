package ru.liga.songtask.util;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.meta.Tempo;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Changer {
    private static final Logger log = LoggerFactory.getLogger(Changer.class);

    public static MidiFile changeMidi(MidiFile midiFile, int trans, float tempo) {
        float percentTempo = 1.0F + tempo / 100.0F;
        log.trace("Pace multiplier = {}", Float.valueOf(percentTempo));
        MidiFile newMidi = changeTempo(midiFile, percentTempo);
        newMidi = transposeMidi(newMidi, trans);
        log.trace("Change completed");
        return newMidi;
    }

    public static MidiFile changeTempo(MidiFile midiFile, float percentTempo) {
        MidiFile midiFile1 = new MidiFile();
        log.debug("Old Bpm = {}", Float.valueOf(SongUtils.getTempo(midiFile).getBpm()));
        midiFile.getTracks().forEach(x -> midiFile1.addTrack(changeTempMidi(percentTempo, x)));
        log.debug("New Bpm = {}", Float.valueOf(SongUtils.getTempo(midiFile1).getBpm()));
        return midiFile1;
    }

    private static MidiTrack changeTempMidi(float percentTempo, MidiTrack midiTrack) {
        MidiTrack midiTrack1 = new MidiTrack();
        for (MidiEvent midiEvent : midiTrack.getEvents()) {
            if (midiEvent.getClass().equals(Tempo.class)) {
                Tempo tempo = getTempo(percentTempo, (Tempo)midiEvent);
                midiTrack1.getEvents().add(tempo);
                continue;
            }
            midiTrack1.getEvents().add(midiEvent);
        }
        return midiTrack1;
    }

    private static Tempo getTempo(float percentTempo, Tempo midiEvent) {
        Tempo tempo = new Tempo(midiEvent.getTick(), midiEvent.getDelta(), midiEvent.getMpqn());
        tempo.setBpm(tempo.getBpm() * percentTempo);
        return tempo;
    }

    public static MidiFile transposeMidi(MidiFile midiFile, int trans) {
        MidiFile midiFile1 = new MidiFile();
        midiFile.getTracks().forEach(miditrack -> midiFile1.addTrack(transposeMidiTrack(trans, miditrack)));
        log.debug("Transpose into {} halftones. The first note of the old track: {} -> The first note of the new track {}", new Object[] { Integer.valueOf(trans), ((List)SongUtils.getAllTracksAsNoteLists(midiFile).get(0)).get(0), ((List)SongUtils.getAllTracksAsNoteLists(midiFile1).get(0)).get(0) });
        return midiFile1;
    }

    private static MidiTrack transposeMidiTrack(int trans, MidiTrack midiTrack) {
        MidiTrack midiTrack1 = new MidiTrack();
        midiTrack.getEvents().forEach(midiEvent -> {
            if (midiEvent.getClass().equals(NoteOn.class)) {
                NoteOn on = getChangedNoteOn(trans, (NoteOn)midiEvent);
                midiTrack1.getEvents().add(on);
            } else if (midiEvent.getClass().equals(NoteOff.class)) {
                NoteOff off = getChangedNoteOff(trans, (NoteOff)midiEvent);
                midiTrack1.getEvents().add(off);
            } else {
                midiTrack1.getEvents().add(midiEvent);
            }
        });
        return midiTrack1;
    }

    private static NoteOff getChangedNoteOff(int trans, NoteOff midiEvent) {
        NoteOff off = new NoteOff(midiEvent.getTick(), midiEvent.getDelta(), midiEvent.getChannel(), midiEvent.getNoteValue(), midiEvent.getVelocity());
        off.setNoteValue(off.getNoteValue() + trans);
        return off;
    }

    private static NoteOn getChangedNoteOn(int trans, NoteOn midiEvent) {
        NoteOn on = new NoteOn(midiEvent.getTick(), midiEvent.getDelta(), midiEvent.getChannel(), midiEvent.getNoteValue(), midiEvent.getVelocity());
        on.setNoteValue(on.getNoteValue() + trans);
        return on;
    }
}
