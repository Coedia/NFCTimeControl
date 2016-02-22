/*
 * Copyright (c) 2016. OneOctopus www.oneoctopus.es
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.oneoctopus.nfctimecontrol;

import android.support.multidex.MultiDex;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.InstrumentationRegistry.getTargetContext;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import es.oneoctopus.nfctimecontrol.data.PlacesDAO;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest {
    private PlacesDAO db;

    @BeforeClass
    public static void setUpClass() {
        getTargetContext().deleteDatabase("places");
    }

    @Before
    public void setUp() throws Exception{
        MultiDex.install(getTargetContext());
        db = new PlacesDAO(getTargetContext());
    }

    @After
    public void tearDown() throws Exception{
    }

    @Test
    public void test1_isDatabaseEmpty() throws Exception{
        assertTrue(db.isEmpty());
    }

    @Test
    public void test2_firstInsertDataIn() throws Exception{
        db.check(DateTime.now(), "testplace");
        List<String> places = db.getPlaceNames();
        assertThat(places.size(), is(1));
    }

    @Test
    public void test3_firstInsertDataOut() throws Exception{
        db.check(DateTime.now(), "testplace");
        List<String> places = db.getPlaceNames();
        assertThat(places.size(), is(1));
    }

    @Test
    public void test4_secondInsertDataIn(){
        db.check(DateTime.now(), "testplace");
        List<String> places = db.getPlaceNames();
        assertThat(places.size(), is(1));
    }

    @Test
    public void test5_thirdInsertDataIn(){
        db.check(DateTime.now(), "testplace_second");
        List<String> places = db.getPlaceNames();
        assertThat(places.size(), is(2));
    }

    @Test public void test6_secondInsertDataOut(){
        db.check(DateTime.now(), "testplace_second");
        List<String> places = db.getPlaceNames();
        assertThat(places.size(), is(2));
    }

    @Test public void test7_isCheckOpenFirst(){
        assertFalse(db.isCheckOpen("testplace_second"));
    }

    @Test public void test8_isCheckOpenSecond(){
        assertTrue(db.isCheckOpen("testplace"));
    }

    @Test
    public void test9_openChecks(){
        db.check(DateTime.now(), "testnew");
        assertThat(db.getOpenChecks().size(), is(2));
    }
}
