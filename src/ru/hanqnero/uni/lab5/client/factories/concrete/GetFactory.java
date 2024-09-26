package ru.hanqnero.uni.lab5.client.factories.concrete;

import ru.hanqnero.uni.lab5.client.factories.CommandFactory;
import ru.hanqnero.uni.lab5.commons.contract.commands.Command;
import ru.hanqnero.uni.lab5.commons.contract.commands.concrete.GetByDate;
import ru.hanqnero.uni.lab5.commons.exceptions.CommandCreationError;
import ru.hanqnero.uni.lab5.commons.exceptions.SubtypeScanError;

public class GetFactory implements CommandFactory {
    @Override
    public Command createCommand(String[] tokens) throws SubtypeScanError, CommandCreationError {
        if (tokens.length == 1)
            throw new CommandCreationError("Wrong argument count");

        GetByDate.Variant var = null;
        GetByDate.DateType type = null;

        for (int i = 1; i < tokens.length; ++i) {
            String token = tokens[i].toLowerCase();
            switch (token) {
                case "--min":
                    var = GetByDate.Variant.MIN;
                    break;
                case "--max":
                    var = GetByDate.Variant.MAX;
                    break;
                case "--establishment":
                    type = GetByDate.DateType.ESTABLISHMENT;
                    break;
                case "--creation":
                    type = GetByDate.DateType.CREATION;
                    break;
                default:
                    throw new CommandCreationError("Unknown flag: " + token);
            }
        }
        if (var == null || type == null)
            throw new CommandCreationError("Not enough arguments");

        return new GetByDate(var, type);
    }
}
