package com.sidenis.dealtracker.service;

import com.sidenis.dealtracker.model.Counterparty;
import com.sidenis.dealtracker.model.CounterpartyReplacementEvent;
import com.sidenis.dealtracker.model.Deal;
import com.sidenis.dealtracker.model.DealVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealHistoryService {

    private final DealService dealService;
    private final DealVersionService dealVersionService;
    private final CounterpartyReplacementEventSerivce counterpartyReplacementEventSerivce;


    /**
     * @return history of the specific deal version
     */
    public SortedMap<Timestamp, Counterparty> getCounterpartyHistoryForDealVersion(String dealName, String dealVersionName) {

        Deal deal = dealService.getDeal(dealName);
        DealVersion currentDealVersion = dealVersionService.getDealVersion(deal, dealVersionName);
        Optional<DealVersion> nextDealVersion = dealVersionService.getSubsequentDealVersion(currentDealVersion);
        Timestamp start = currentDealVersion.getCreatedAt();
        Timestamp end = nextDealVersion.map(DealVersion::getCreatedAt).orElse(null);

        List<CounterpartyReplacementEvent> potentialReplacements =
                end == null ?
                        counterpartyReplacementEventSerivce.getEventsAfter(start) :
                        counterpartyReplacementEventSerivce.getEventsBetween(start, end);

        return new TreeMap<>(extractRelevantReplacements(potentialReplacements, currentDealVersion.getCounterparty(), start, end));
    }

    /**
     * @return the full "history" of the deal - a sorted map of timestamps when the ownership was put into effect vs. the respective owners.
     * Owners can change as a result of two events: global partner replacement or creation of a new deal version
     */
    public SortedMap<Timestamp, Counterparty> getCounterpartyHistoryForDealName(String dealName) {

        Deal deal = dealService.getDeal(dealName);

        SortedMap<Timestamp, Counterparty> history = new TreeMap<>();

        // all deal versions should be reflected in history
        dealVersionService.getDealVersions(deal)
                .forEach(v -> history.put(v.getCreatedAt(), v.getCounterparty()));

        // prepare time intervals for processing
        List<Timestamp> timestamps = history.keySet().stream().sorted().collect(Collectors.toList());

        // process internal intervals
        for (int i = 0; i < timestamps.size(); i++) {
            Timestamp start = timestamps.get(i);
            Timestamp end =
                    (i == timestamps.size() - 1) ?
                            null :
                            timestamps.get(i + 1);
            Counterparty initialPartner = history.get(start);
            List<CounterpartyReplacementEvent> potentialReplacements =
                    end == null ?
                            counterpartyReplacementEventSerivce.getEventsAfter(start) :
                            counterpartyReplacementEventSerivce.getEventsBetween(start, end);
            history.putAll(extractRelevantReplacements(potentialReplacements, initialPartner, start, end));
        }

        // the specification requires deleting the very first entry, keeping only changes
        history.remove(history.firstKey());

        return history;
    }

    private SortedMap<Timestamp, Counterparty> extractRelevantReplacements(List<CounterpartyReplacementEvent> replacements,
                                                                           Counterparty partner,
                                                                           Timestamp startExcluded,
                                                                           Timestamp end) {
        SortedMap<Timestamp, Counterparty> result = new TreeMap<>();

        Optional<CounterpartyReplacementEvent> event = replacements.stream()
                .filter(e -> e.getDeprecatedCounterparty().equals(partner))
                .filter(e -> e.getDeprecatedAt().after(startExcluded))
                .min(Comparator.comparing(CounterpartyReplacementEvent::getDeprecatedAt));
        while (event.isPresent()) {
            Timestamp start = event.get().getDeprecatedAt();
            Counterparty currentPartner = event.get().getSurvivorCounterparty();
            result.put(start, currentPartner);
            if (end != null) {
                replacements = counterpartyReplacementEventSerivce.getEventsBetween(start, end);
            } else {
                replacements = counterpartyReplacementEventSerivce.getEventsAfter(start);
            }
            event = getNextReplacement(replacements, currentPartner, start);
        }
        return result;
    }

    private Optional<CounterpartyReplacementEvent> getNextReplacement(List<CounterpartyReplacementEvent> replacements,
                                                                      Counterparty currentCounterparty,
                                                                      Timestamp startExcluded) {
        return replacements.stream()
                .filter(r -> r.getDeprecatedCounterparty().equals(currentCounterparty))
                .filter(r -> r.getDeprecatedAt().after(startExcluded))
                .min(Comparator.comparing(CounterpartyReplacementEvent::getDeprecatedAt));
    }
}
