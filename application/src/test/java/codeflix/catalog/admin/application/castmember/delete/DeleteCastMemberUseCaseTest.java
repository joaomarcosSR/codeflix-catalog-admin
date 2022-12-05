package codeflix.catalog.admin.application.castmember.delete;

import codeflix.catalog.admin.application.Fixture;
import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain.castmember.entity.CastMember;
import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteCastMemberUseCaseImpl useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.castMemberGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = aMember.getId();

        doNothing()
                .when(this.castMemberGateway).deleteById(expectedId);

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // given
        final var expectedId = CastMemberID.from("123");

        doNothing()
                .when(this.castMemberGateway).deleteById(expectedId);

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));
    }

    @Test
    void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = aMember.getId();
        final var expectedIdValue = aMember.getId().getValue();

        doThrow(new IllegalStateException("Gateway error"))
                .when(this.castMemberGateway).deleteById(expectedId);

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> this.useCase.execute(expectedIdValue));
    }
}