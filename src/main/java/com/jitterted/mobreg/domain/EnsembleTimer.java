package com.jitterted.mobreg.domain;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

public class EnsembleTimer {
    private static final Duration DEFAULT_TIMER_DURATION = Duration.ofMinutes(4);
    private final EnsembleId ensembleId;
    private final String ensembleName;
    private final Stream<MemberId> participants;
    private final Duration timerDuration;
    private TimerState currentState;
    private Instant startedAt;

    public EnsembleTimer(EnsembleId ensembleId,
                         String ensembleName,
                         Stream<MemberId> participants) {
        this(ensembleId, ensembleName, participants, DEFAULT_TIMER_DURATION);
    }

    public EnsembleTimer(EnsembleId ensembleId, String ensembleName, Stream<MemberId> participants, Duration timerDuration) {
        this.ensembleId = ensembleId;
        this.ensembleName = ensembleName;
        this.participants = participants;
        this.timerDuration = timerDuration;
        this.currentState = TimerState.WAITING_TO_START;
    }

    public EnsembleId ensembleId() {
        return ensembleId;
    }

    public Stream<MemberId> participants() {
        return participants;
    }

    public String ensembleName() {
        return ensembleName;
    }

    public TimerState state() {
        return currentState;
    }

    public void startTimerAt(Instant timeStarted) {
        requireNotRunning();
        startedAt = timeStarted;
        currentState = TimerState.RUNNING;
    }

    private void requireNotRunning() {
        if (isRunning()) {
            throw new IllegalStateException("Can't Start Timer when Running");
        }
    }

    private boolean isRunning() {
        return currentState == TimerState.RUNNING;
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                "EnsembleTimer [ensembleId={0}, participants={1}]",
                ensembleId, participants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EnsembleTimer that = (EnsembleTimer) o;

        return ensembleId.equals(that.ensembleId);
    }

    @Override
    public int hashCode() {
        return ensembleId.hashCode();
    }

    public void tick(Instant now) {
        throw new IllegalStateException();
    }

    public enum TimerState {
        RUNNING, WAITING_TO_START
    }
}
