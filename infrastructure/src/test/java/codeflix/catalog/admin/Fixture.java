package codeflix.catalog.admin;

import codeflix.catalog.admin.domain.castmember.enums.CastMemberType;
import com.github.javafaker.Faker;

public final class Fixture {
    public static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static final class CastMembers {
        public static CastMemberType type() {
            return FAKER.options()
                    .option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }
    }
}