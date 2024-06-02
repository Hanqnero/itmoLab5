package ru.hanqnero.uni.lab5.util;

import ru.hanqnero.uni.lab5.collection.MusicBand;

import java.util.Comparator;

public class CreationDateComparator implements Comparator<MusicBand> {
    @Override
    public int compare(MusicBand o1, MusicBand o2) {
        return o1.getCreationDate().compareTo(o2.getCreationDate());
    }
}
