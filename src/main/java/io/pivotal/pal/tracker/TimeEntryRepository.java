package io.pivotal.pal.tracker;

import java.util.Collection;
import java.util.List;

public interface TimeEntryRepository {
    TimeEntry find(long timeEntryId);

    TimeEntry create(TimeEntry timeEntry);

    Collection<TimeEntry> list();

    TimeEntry update(long id, TimeEntry timeEntry);

    void delete(long id);
}
