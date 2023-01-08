package codeflix.catalog.admin.domain._share.entity;

import codeflix.catalog.admin.domain._share.utils.IdUtils;
import codeflix.catalog.admin.domain._share.utils.InstantUtils;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.value.object.Identifier;
import codeflix.catalog.admin.domain.events.DomainEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class EntityTest {
    @Test
    void givenNullAsEvents_whenInstantiate_shouldBeOk() {
        final List<DomainEvent> events = null;

        final var anEntity = new DummyEntity(new DummyID(), events);

        Assertions.assertNotNull(anEntity.getDomainEvents());
        Assertions.assertTrue(anEntity.getDomainEvents().isEmpty());
    }

    @Test
    void givenDomainEvents_whenInstantiate_shouldCreateADefensiveClone() {
        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());

        final var anEntity = new DummyEntity(new DummyID(), events);

        final List<DomainEvent> domainEvents = anEntity.getDomainEvents();

        Assertions.assertNotNull(domainEvents);
        Assertions.assertEquals(1, domainEvents.size());

        final DummyEvent dummyEvent = new DummyEvent();
        Assertions.assertThrows(RuntimeException.class, () -> {
            domainEvents.add(dummyEvent);
        });
    }

    @Test
    void givenEmptyDomainEvents_whenCallsRegisterEvents_shouldAddEventToList() {
        final int expectedEvents = 1;
        final var anEntity = new DummyEntity();

        anEntity.registerEvent(new DummyEvent());

        final List<DomainEvent> domainEvents = anEntity.getDomainEvents();

        Assertions.assertNotNull(domainEvents);
        Assertions.assertEquals(expectedEvents, domainEvents.size());
    }

    @Test
    void givenAFewDomainEvents_whenCallsPublishEvents_shouldCallPublishAnClearTheList() {
        final int expectedEvents = 0;
        final int expectedSentEvents = 2;
        final AtomicInteger counter = new AtomicInteger(0);
        final var anEntity = new DummyEntity();
        anEntity.registerEvent(new DummyEvent());
        anEntity.registerEvent(new DummyEvent());

        Assertions.assertEquals(2, anEntity.getDomainEvents().size());

        anEntity.publishDomainEvents(event -> {
            counter.incrementAndGet();
        });

        Assertions.assertNotNull(anEntity.getDomainEvents());
        Assertions.assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        Assertions.assertEquals(expectedSentEvents, counter.get());
    }

    public static class DummyEvent implements DomainEvent {

        @Override
        public Instant occurredOn() {
            return InstantUtils.now();
        }
    }

    public static class DummyID extends Identifier {

        private final String id;

        public DummyID() {
            this.id = IdUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity() {
            this(new DummyID(), Collections.emptyList());
        }

        public DummyEntity(final DummyID dummyID, final List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(final ValidationHandler aHandler) {

        }
    }
}