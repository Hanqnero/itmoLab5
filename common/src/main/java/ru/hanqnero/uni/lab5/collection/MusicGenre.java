package ru.hanqnero.uni.lab5.collection;

@SuppressWarnings("unused")
public enum MusicGenre {
    PROGRESSIVE_ROCK,
    PSYCHEDELIC_ROCK,
    POST_ROCK,
    PUNK_ROCK;

    @Override
    public String toString() {
        return super.toString().replace("_", " ").toLowerCase();
    }
}
