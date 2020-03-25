package ru.liga.songtask.util;

import com.leff.midi.MidiFile;
import com.leff.midi.event.meta.Tempo;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.App;
import ru.liga.songtask.domain.Note;

public class Analyzer {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    private MidiFile file;

    List<Note> notes;

    public Analyzer(MidiFile file) {
        this.file = file;
        this.notes = SongUtils.getVoiceTrack(file);
    }

    public Map<Integer, String> analyzisDiapozon() {
        logger.trace("Track Range Analysis");
        Map<Integer, String> analyzis = new LinkedHashMap<>();
        int[] min = { Integer.MAX_VALUE };
        int[] max = { Integer.MIN_VALUE };
        this.notes.forEach(note -> {
            if (note.sign().getMidi().intValue() > max[0]) {
                max[0] = note.sign().getMidi().intValue();
                analyzis.put(Integer.valueOf(0), note.sign().fullName());
            }
            if (note.sign().getMidi().intValue() < min[0]) {
                min[0] = note.sign().getMidi().intValue();
                analyzis.put(Integer.valueOf(1), note.sign().fullName());
            }
        });
        analyzis.put(Integer.valueOf(2), String.valueOf(max[0] - min[0]));
        logger.info("Range :");
        logger.info(" Upper: {}", analyzis.get(Integer.valueOf(0)));
        logger.info(" Lower: {}", analyzis.get(Integer.valueOf(1)));
        logger.info(" Range: {}", analyzis.get(Integer.valueOf(2)));
        return analyzis;
    }

    public Map<Integer, Integer> analyzisDuration() {
        logger.trace("Analysis of track notes by duration.");
        Map<Integer, Integer> analysis = new HashMap<>();
        Tempo tempo = SongUtils.getTempo(this.file);
        this.notes.forEach(note -> {
            int noteMs = SongUtils.tickToMs(tempo.getBpm(), this.file.getResolution(), note.durationTicks().longValue());
            if (analysis.containsKey(Integer.valueOf(noteMs))) {
                analysis.put(Integer.valueOf(noteMs), Integer.valueOf(((Integer)analysis.get(Integer.valueOf(noteMs))).intValue() + 1));
            } else {
                analysis.put(Integer.valueOf(noteMs), Integer.valueOf(1));
            }
        });
        logger.info("The number of notes by duration");
        for (Map.Entry<Integer, Integer> durations : analysis.entrySet())
            logger.info((new StringBuilder()).append(durations.getKey()).append("ms: ").append(durations.getValue()).toString());
        return analysis;
    }

    public Map<String, Long> analyzisHeigh() {
        logger.trace("Analysis of notes by the number of occurrences.");
        Map<String, Long> analysis = new HashMap<>();
        this.notes.forEach(note -> {
            if (analysis.containsKey(note.sign().fullName())) {
                Long buf = (Long)analysis.get(note.sign().fullName());
                analysis.put(note.sign().fullName(), Long.valueOf(buf.longValue() + 1L));
            } else {
                analysis.put(note.sign().fullName(), Long.valueOf(1L));
            }
        });
        logger.info("List of notes by number of occurrences");
        for (Map.Entry<String, Long> heigh : analysis.entrySet())
            logger.info((String)heigh.getKey() + ": " + heigh.getValue());
        return analysis;
    }

    public void fullAnalize() {
        if (this.notes != null) {
            logger.info("Start analyze track");
            analyzisDiapozon();
            analyzisDuration();
            analyzisHeigh();
        } else {
            logger.info("No voice track");
        }
    }
}
