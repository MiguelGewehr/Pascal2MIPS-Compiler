package entries;

import typing.Type;

public interface Entry {
    String getName();
    int getLine();
    Type getEntryType();
}