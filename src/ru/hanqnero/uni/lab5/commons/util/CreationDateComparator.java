package ru.hanqnero.uni.lab5.commons.util;

import ru.hanqnero.uni.lab5.commons.collection.MusicBand;

import java.util.Comparator;

public class CreationDateComparator implements Comparator<MusicBand> {
    @Override
    public int compare(MusicBand o1, MusicBand o2) {
        return o1.getCreationDate().compareTo(o2.getCreationDate());
    }
}
