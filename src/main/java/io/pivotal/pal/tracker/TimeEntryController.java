package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping(value = "/time-entries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeEntry);
    }

    @GetMapping(value = "/time-entries/{timeEntryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeEntry> read(@PathVariable("timeEntryId") long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        HttpStatus status = timeEntry == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        if (timeEntry != null) {
            actionCounter.increment();
        }
        return ResponseEntity.status(status).body(timeEntry);
    }

    @GetMapping(value = "/time-entries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntries = timeEntryRepository.list();
        actionCounter.increment();
        return ResponseEntity.ok(timeEntries);
    }

    @RequestMapping(value = "/time-entries/{timeEntryId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeEntry> update(@PathVariable("timeEntryId") long timeEntryId
            ,@RequestBody TimeEntry timeEntryToUpdate) {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(timeEntryId, timeEntryToUpdate);
        HttpStatus status = updatedTimeEntry == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        if (updatedTimeEntry != null) {
            actionCounter.increment();
        }
        return ResponseEntity.status(status).body(updatedTimeEntry);
    }

    @RequestMapping(value = "/time-entries/{timeEntryId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable("timeEntryId") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
