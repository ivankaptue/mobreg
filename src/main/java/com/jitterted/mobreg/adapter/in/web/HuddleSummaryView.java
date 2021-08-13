package com.jitterted.mobreg.adapter.in.web;

import com.jitterted.mobreg.adapter.DateTimeFormatting;
import com.jitterted.mobreg.domain.Huddle;
import com.jitterted.mobreg.domain.MemberId;

import java.util.List;

public record HuddleSummaryView(long id,
                                String name,
                                String dateTime,
                                int numberRegistered,
                                boolean memberRegistered) {

    // GOAL: use MemberId and not username
    @Deprecated
    public static List<HuddleSummaryView> from(List<Huddle> huddles, String username) {
        return huddles.stream()
                      .map(huddle -> toView(huddle, username))
                      .toList();
    }

    public static List<HuddleSummaryView> from(List<Huddle> huddles, MemberId memberId) {
        return huddles.stream()
                      .map(huddle -> toView(huddle, memberId))
                      .toList();
    }

    // GOAL: use the toView that takes a MemberId and not this one
    @Deprecated
    public static HuddleSummaryView toView(Huddle huddle, String username) {
        return new HuddleSummaryView(huddle.getId().id(),
                                     huddle.name(),
                                     DateTimeFormatting.formatAsDateTime(huddle.startDateTime()),
                                     huddle.numberRegistered(),
                                     huddle.isRegisteredByUsername(username));
    }

    public static HuddleSummaryView toView(Huddle huddle, MemberId memberId) {
        return new HuddleSummaryView(huddle.getId().id(),
                                     huddle.name(),
                                     DateTimeFormatting.formatAsDateTime(huddle.startDateTime()),
                                     huddle.registeredMemberCount(),
                                     huddle.isRegisteredById(memberId));
    }
}
