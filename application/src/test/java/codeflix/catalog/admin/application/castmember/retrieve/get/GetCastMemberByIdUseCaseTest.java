package codeflix.catalog.admin.application.castmember.retrieve.get;

import codeflix.catalog.admin.application.Fixture;
import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.castmember.entity.CastMember;
import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class GetCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetCastMemberByIdUseCaseImpl useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.castMemberGateway);
    }

    @Test
    void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        when(this.castMemberGateway.findById(expectedId))
                .thenReturn(Optional.of(aMember));

        // when
        final var actualOutput = this.useCase.execute(expectedId.getValue());

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(aMember.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualOutput.updatedAt());
    }

    @Test
    void givenAInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        // given
        final var expectedId = CastMemberID.from("123");
        final String expectedIdValue = expectedId.getValue();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        when(this.castMemberGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        // when
        final var actualOutput = Assertions.assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(expectedIdValue);
        });

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());
    }
}