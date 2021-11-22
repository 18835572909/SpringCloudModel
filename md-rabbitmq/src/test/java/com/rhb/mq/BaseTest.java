package com.rhb.mq;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * {desc}
 *
 * @author renhuibo
 * @date 2021/11/18 17:50
 */
@ActiveProfiles("")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MqApplication.class,webEnvironment = WebEnvironment.RANDOM_PORT)
public class BaseTest {

}
