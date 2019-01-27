package org.aminism.spider;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootTestInitializer.class)
//@DirtiesContext
public abstract class AbstractSpringContextTest {

@Rule
public TestRule testProgress = new TestProgress();

}