package ru.liga;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.liga.songtask.util.Changer;

import static org.junit.Assert.*;

public class AppTest {
    @BeforeClass
    public static void setup(){
    }
    @Test
    public void testGetArgs(){
        //Assert.assertEquals(App.getArgs(););
    }
}
