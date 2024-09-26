package ru.hanqnero.uni.lab5.commons.contract.commands.concrete;

import ru.hanqnero.uni.lab5.commons.collection.MusicBandBuilder;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;

public record RemoveGreaterCommand(MusicBandBuilder builder) implements Command {
}
