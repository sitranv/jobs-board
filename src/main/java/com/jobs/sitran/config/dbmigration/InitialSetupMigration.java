package com.jobs.sitran.config.dbmigration;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.jobs.sitran.domain.Authority;
import com.jobs.sitran.domain.User;
import com.jobs.sitran.security.AuthorityConstants;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;

@ChangeLog(order = "001")
public class InitialSetupMigration {

    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthorityConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthorityConstants.USER);
        mongoTemplate.save(adminAuthority);
        mongoTemplate.save(userAuthority);
    }

    @ChangeSet(order = "02", author = "initiator", id = "02-addUsers")
    public void addUsers(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthorityConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthorityConstants.USER);

        //create admin
        User adminUser = new User();
        adminUser.setId("user-1");
//        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
//        adminUser.setLangKey("en");
        adminUser.setCreatedBy("system");
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        //create anonymous user
        User anonymousUser = new User();
        anonymousUser.setId("user-2");
//        anonymousUser.setLogin("anonymoususer");
        anonymousUser.setPassword("$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO");
        anonymousUser.setFirstName("Anonymous");
        anonymousUser.setLastName("User");
        anonymousUser.setEmail("anonymous@localhost");
        anonymousUser.setActivated(true);
//        anonymousUser.setLangKey("en");
//        anonymousUser.setCreatedBy(systemUser.getLogin());
        anonymousUser.setCreatedDate(Instant.now());
        mongoTemplate.save(anonymousUser);

        for(int i = 3 ; i < 10; i++) {
            User userUser = new User();
            userUser.setId("user-" + i );
//            userUser.setLogin("user");
            userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
            userUser.setFirstName("");
            userUser.setLastName("User");
            userUser.setEmail("user@localhost");
            userUser.setActivated(true);
//            userUser.setLangKey("en");
            userUser.setCreatedBy("system");
            userUser.setCreatedDate(Instant.now());
            userUser.getAuthorities().add(userAuthority);
            mongoTemplate.save(userUser);
        }
    }
}
