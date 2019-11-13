/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.attributes.persistence.impl;

import com.amazon.ask.exception.PersistenceException;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.Device;
import com.amazon.ask.model.Person;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.User;
import com.amazon.ask.model.interfaces.system.SystemState;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PartitionKeyGeneratorsTest {

    @Test
    public void user_id_generator_persistence_id_generated_from_user_id() {
        String userId = "1234";

        User user = User.builder().withUserId(userId).build();
        SystemState system = SystemState.builder().withUser(user).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();

        Assert.assertEquals(PartitionKeyGenerators.userId().apply(env), userId);
    }

    @Test (expected = PersistenceException.class)
    public void user_id_generator_envelope_null_throws_exception() {
        PartitionKeyGenerators.userId().apply(null);
    }

    @Test (expected = PersistenceException.class)
    public void user_id_generator_context_null_throws_exception() {
        RequestEnvelope env = RequestEnvelope.builder().build();
        PartitionKeyGenerators.userId().apply(env);
    }

    @Test (expected = PersistenceException.class)
    public void user_id_generator_systemState_null_throws_exception() {
        Context context = Context.builder().build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();
        PartitionKeyGenerators.userId().apply(env);
    }

    @Test (expected = PersistenceException.class)
    public void user_id_generator_device_null_throws_exception() {
        SystemState system = SystemState.builder().build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();
        PartitionKeyGenerators.userId().apply(env);
    }

    @Test (expected = PersistenceException.class)
    public void user_id_generator_deviceId_null_throws_exception() {
        Device device = Device.builder().build();
        SystemState system = SystemState.builder().withDevice(device).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();
        PartitionKeyGenerators.userId().apply(env);
    }

    @Test
    public void persistence_id_generator_persistence_id_generated_from_device_id() {
        String deviceId = "1234";

        Device device = Device.builder().withDeviceId(deviceId).build();
        SystemState system = SystemState.builder().withDevice(device).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();

        assertEquals(PartitionKeyGenerators.deviceId().apply(env), deviceId);
    }

    @Test (expected = PersistenceException.class)
    public void persistence_id_generator_envelope_null_throws_exception() {
        PartitionKeyGenerators.deviceId().apply(null);
    }

    @Test (expected = PersistenceException.class)
    public void persistence_id_generator_context_null_throws_exception() {
        RequestEnvelope env = RequestEnvelope.builder().build();
        PartitionKeyGenerators.deviceId().apply(env);
    }

    @Test (expected = PersistenceException.class)
    public void persistence_id_generator_systemState_null_throws_exception() {
        Context context = Context.builder().build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();
        PartitionKeyGenerators.deviceId().apply(env);
    }

    @Test (expected = PersistenceException.class)
    public void persistence_id_generator_device_null_throws_exception() {
        SystemState system = SystemState.builder().build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();
        PartitionKeyGenerators.deviceId().apply(env);
    }

    @Test (expected = PersistenceException.class)
    public void persistence_id_generator_deviceId_null_throws_exception() {
        Device device = Device.builder().build();
        SystemState system = SystemState.builder().withDevice(device).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();
        PartitionKeyGenerators.deviceId().apply(env);
    }

    @Test
    public void person_id_generator_persistence_id_generated_from_person_id() {
        String personId = "1234";

        Person person = Person.builder().withPersonId(personId).build();
        SystemState system = SystemState.builder().withPerson(person).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();
        Assert.assertEquals(PartitionKeyGenerators.personId().apply(env), personId);
    }

    @Test
    public void person_id_generator_persistence_id_generated_from_user_id_person_id_null() {
        String userId = "1234";

        User user = User.builder().withUserId(userId).build();
        Person person = Person.builder().withPersonId(null).build();
        SystemState system = SystemState.builder().withPerson(person).withUser(user).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();

        Assert.assertEquals(PartitionKeyGenerators.personId().apply(env), userId);
    }

    @Test
    public void person_id_generator_persistence_id_generated_from_user_id_person_null() {
        String userId = "1234";

        User user = User.builder().withUserId(userId).build();
        SystemState system = SystemState.builder().withPerson(null).withUser(user).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();

        Assert.assertEquals(PartitionKeyGenerators.personId().apply(env), userId);
    }

    @Test(expected = PersistenceException.class)
    public void person_id_generator_persistence_id_person_id_user_id_null_throws_exception() {
        User user = User.builder().withUserId(null).build();
        Person person = Person.builder().withPersonId(null).build();
        SystemState system = SystemState.builder().withPerson(person).withUser(user).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();

        PartitionKeyGenerators.personId().apply(env);
    }

    @Test(expected = PersistenceException.class)
    public void person_id_generator_persistence_id_person_id_user_null_throws_exception() {
        Person person = Person.builder().withPersonId(null).build();
        SystemState system = SystemState.builder().withPerson(person).withUser(null).build();
        Context context = Context.builder().withSystem(system).build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();

        PartitionKeyGenerators.personId().apply(env);
    }

    @Test (expected = PersistenceException.class)
    public void person_id_generator_envelope_null_throws_exception() {
        PartitionKeyGenerators.personId().apply(null);
    }

    @Test (expected = PersistenceException.class)
    public void person_id_generator_context_null_throws_exception() {
        RequestEnvelope env = RequestEnvelope.builder().build();
        PartitionKeyGenerators.personId().apply(env);
    }

    @Test (expected = PersistenceException.class)
    public void person_id_generator_systemState_null_throws_exception() {
        Context context = Context.builder().build();
        RequestEnvelope env = RequestEnvelope.builder().withContext(context).build();
        PartitionKeyGenerators.personId().apply(env);
    }


}
