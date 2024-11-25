package demo.unjuanable.adapter.driven.persistence.orgmng;

import demo.unjuanable.domain.orgmng.org.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrgRepositoryIT {
    static final Long DEFAULT_ORG_ID = 1L;
    static final Long DEFAULT_USER_ID = 1L;
    static final long DEFAULT_TENANT_ID = 1L;
    static final long DEFAULT_EMP_ID = 1L;
    final private OrgRepository orgRepository;
    private OrgReBuilderFactory orgReBuilderFactory;

    @Autowired
    OrgRepositoryIT(OrgRepository orgRepository, OrgReBuilderFactory orgReBuilderFactory) {
        this.orgRepository = orgRepository;
        this.orgReBuilderFactory = orgReBuilderFactory;
    }

    @Test
    void save_successfully() {
        Org org = new Org(1L, "DEVCET", LocalDateTime.now(), 1L);
        org.setName("大名府");
        org.setLeaderId(1L);
        org.setSuperiorId(1L);

        Org created = orgRepository.save(org);

        assertNotNull(created.getId());
    }

    @Test
    void findById_notfound() {
        Optional<Org> org = orgRepository.findById(1L, -1L);
        // assertTrue(org.isEmpty());
    }

    @Test
    public void existsBySuperiorIdAndName_shouldBeTrue_whenExists() {
        //given
        Org org = prepareOrg();

        //when
        boolean found = orgRepository.existsBySuperiorIdAndName(
                org.getTenantId(),
                org.getSuperiorId(),
                org.getName());

        assertTrue(found);
    }

    @Test
    public void existsBySuperiorIdAndName_shouldBeFalse_whenExists() {

        boolean found = orgRepository.existsBySuperiorIdAndName(
                DEFAULT_TENANT_ID
                , DEFAULT_EMP_ID
                , "某某某");

        assertFalse(found);
    }

    private Org prepareOrg() {
        OrgReBuilder orgReBuilder = orgReBuilderFactory.build();
        Org org = orgReBuilder
                .tenantId(DEFAULT_TENANT_ID)
                .superiorId(DEFAULT_ORG_ID)
                .orgTypeCode("DEVCENT")
                .leaderId(DEFAULT_EMP_ID)
                .name("忠义堂")
                .statusCode(OrgStatus.EFFECTIVE.code())
                .createdAt(LocalDateTime.now())
                .createdBy(DEFAULT_USER_ID)
                .build();
        orgRepository.save(org);
        return org;
    }
}