package codeflix.catalog.admin.infrastructure.configuration.usecases;

import codeflix.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import codeflix.catalog.admin.application.castmember.create.CreateCastMemberUseCaseImpl;
import codeflix.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import codeflix.catalog.admin.application.castmember.delete.DeleteCastMemberUseCaseImpl;
import codeflix.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import codeflix.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIdUseCaseImpl;
import codeflix.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCase;
import codeflix.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCaseImpl;
import codeflix.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import codeflix.catalog.admin.application.castmember.update.UpdateCastMemberUseCaseImpl;
import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new CreateCastMemberUseCaseImpl(this.castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new UpdateCastMemberUseCaseImpl(this.castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new GetCastMemberByIdUseCaseImpl(this.castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DeleteCastMemberUseCaseImpl(this.castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new ListCastMembersUseCaseImpl(this.castMemberGateway);
    }
}
