package io.pivotal.pal.tracker;

import java.util.Collection;
import java.util.HashMap;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private HashMap<Long, TimeEntry> timeEntryHashMap = new HashMap<>();

    private long sequenceNbr = 1L;

    @Override
    public TimeEntry find(long timeEntryId) {
        return timeEntryHashMap.get(timeEntryId);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        long id = timeEntry.getId() == 0L ? sequenceNbr++: timeEntry.getId();
        timeEntry.setId(id);
        timeEntryHashMap.put(id, timeEntry);
        return timeEntry;
    }

    @Override
    public Collection<TimeEntry> list() {
        return timeEntryHashMap.values();
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry updatedTimeEntry = timeEntryHashMap.get(id);
        if (updatedTimeEntry != null) {
            updatedTimeEntry.setDate(timeEntry.getDate());
            updatedTimeEntry.setHours(timeEntry.getHours());
            updatedTimeEntry.setProjectId(timeEntry.getProjectId());
            updatedTimeEntry.setUserId(timeEntry.getUserId());
        }
        return updatedTimeEntry;
    }

    @Override
    public void delete(long id) {
        timeEntryHashMap.remove(id);
    }
}
