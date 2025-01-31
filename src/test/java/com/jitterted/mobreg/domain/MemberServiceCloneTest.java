package com.jitterted.mobreg.domain;

import com.jitterted.mobreg.application.TestMemberBuilder;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class MemberServiceCloneTest {

    @Test
    public void memberReturnsMementoRepresentingInternalState() throws Exception {
        String[] roles = {"ROLE_USER", "ROLE_MEMBER"};
        Member member = new Member("first", "github", roles);
        member.changeEmailTo("first@example.com");
        member.changeTimeZoneTo(ZoneId.of("America/Los_Angeles"));
        member.setId(MemberId.of(23L));
        MemberState expectedMemberState =
                new MemberState("first",
                                "github",
                                Set.of("ROLE_USER", "ROLE_MEMBER"),
                                "first@example.com",
                                ZoneId.of("America/Los_Angeles"),
                                MemberId.of(23L));

        MemberState memberState = member.memento();

        assertThat(memberState)
                .isEqualTo(expectedMemberState);
    }

    @Test
    public void memberFromMementoIsContentEqualToOriginal() throws Exception {
        MemberState memberState =
                new MemberState("ted",
                                "jitterted",
                                Set.of("ROLE_USER", "ROLE_MEMBER", "ROLE_ADMIN"),
                                "jitterted@example.com",
                                ZoneId.of("America/Los_Angeles"),
                                MemberId.of(37L));

        Member memberFromState = new Member(memberState);

        assertThat(memberFromState.getId())
                .isEqualTo(MemberId.of(37L));
        assertThat(memberFromState.firstName())
                .isEqualTo("ted");
        assertThat(memberFromState.githubUsername())
                .isEqualTo("jitterted");
        assertThat(memberFromState.roles())
                .isEqualTo(Set.of("ROLE_USER", "ROLE_MEMBER", "ROLE_ADMIN"));
        assertThat(memberFromState.email())
                .isEqualTo("jitterted@example.com");
        assertThat(memberFromState.timeZone())
                .isEqualTo(ZoneId.of("America/Los_Angeles"));
    }

    @Test
    void clonedObjectNotSameInstanceAsOriginal() throws Exception {
        Member originalMember = new TestMemberBuilder().buildAndSave();

        Member clonedMember = new Member(originalMember.memento());
        clonedMember.changeEmailTo("cloned@example.com");

        assertThat(clonedMember)
                .isNotSameAs(originalMember);
        assertThat(clonedMember.email())
                .isNotEqualTo(originalMember.email());
    }

}