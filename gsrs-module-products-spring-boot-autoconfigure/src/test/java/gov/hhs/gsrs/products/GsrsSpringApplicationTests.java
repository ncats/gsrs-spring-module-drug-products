package gov.hhs.gsrs.products;

import gsrs.startertests.GsrsEntityTestConfiguration;
import gsrs.startertests.jupiter.AbstractGsrsJpaEntityJunit5Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.autoconfigure.WebMvcRegistrations;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

//@ActiveProfiles("test")
@SpringBootTest(classes = {GsrsSpringApplication.class,  GsrsEntityTestConfiguration.class})
class GsrsSpringApplicationTests extends AbstractGsrsJpaEntityJunit5Test {

    @MockitoBean
    WebMvcRegistrations webMvcRegistrations;

  //  @Test
  //  void contextLoads() {
  //  }

}
