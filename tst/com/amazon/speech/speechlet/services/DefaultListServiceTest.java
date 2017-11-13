package com.amazon.speech.speechlet.services;

import com.amazon.speech.speechlet.services.householdlist.client.AlexaList;
import com.amazon.speech.speechlet.services.householdlist.client.AlexaListItem;
import com.amazon.speech.speechlet.services.householdlist.client.AlexaListMetadata;
import com.amazon.speech.speechlet.services.householdlist.client.AlexaListsMetadata;
import com.amazon.speech.speechlet.services.householdlist.client.CreateListItemRequest;
import com.amazon.speech.speechlet.services.householdlist.client.CreateListRequest;
import com.amazon.speech.speechlet.services.householdlist.client.ListItemState;
import com.amazon.speech.speechlet.services.householdlist.client.ListState;
import com.amazon.speech.speechlet.services.householdlist.client.Status;
import com.amazon.speech.speechlet.services.householdlist.client.UpdateListItemRequest;
import com.amazon.speech.speechlet.services.householdlist.client.UpdateListRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.amazon.speech.speechlet.services.ListServiceClient.API_ENDPOINT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultListServiceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String TEST_LIST_ID = UUID.randomUUID().toString();
    private static final String TEST_LIST_ITEM_ID = UUID.randomUUID().toString();
    private static final String TEST_TOKEN = UUID.randomUUID().toString();

    @Test
    public void get_lists_metadata() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        AlexaListsMetadata testListsMetadata = getListsMetadata(ListState.ACTIVE);
        when(apiClient.get(anyString(), anyMap())).thenReturn(new ApiClientResponse(200, MAPPER.writeValueAsString(testListsMetadata)));
        AlexaListsMetadata response = listService.getListsMetadata(TEST_TOKEN);
        verify(apiClient, times(1)).get(API_ENDPOINT + "/v2/householdlists/", getExpectedHeaders());
        assertEquals(response, testListsMetadata);
    }

    @Test
    public void get_list() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        AlexaList testList = getTestList(ListState.ACTIVE);
        when(apiClient.get(anyString(), anyMap())).thenReturn(new ApiClientResponse(200, MAPPER.writeValueAsString(testList)));
        AlexaList response = listService.getList(TEST_LIST_ID, ListState.ACTIVE, TEST_TOKEN);
        verify(apiClient, times(1)).get(API_ENDPOINT + String.format("/v2/householdlists/%s/active", TEST_LIST_ID), getExpectedHeaders());
        assertEquals(response, testList);
    }

    @Test
    public void create_list() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        AlexaListMetadata testListMetadata = getListMetadata(ListState.ACTIVE);
        CreateListRequest request = CreateListRequest.builder().withName("1234").withState(ListState.ACTIVE).build();
        when(apiClient.post(anyString(), anyMap(), anyString())).thenReturn(new ApiClientResponse(200, MAPPER.writeValueAsString(testListMetadata)));
        AlexaListMetadata response = listService.createList(request, TEST_TOKEN);
        verify(apiClient, times(1)).post(API_ENDPOINT + "/v2/householdlists/", getExpectedHeaders(), MAPPER.writeValueAsString(request));
        assertEquals(response, testListMetadata);
    }

    @Test
    public void update_list() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        AlexaListMetadata testListMetadata = getListMetadata(ListState.ACTIVE);
        UpdateListRequest request = UpdateListRequest.builder().withName("1234").withState(ListState.ACTIVE).withVersion(1).build();
        when(apiClient.put(anyString(), anyMap(), anyString())).thenReturn(new ApiClientResponse(200, MAPPER.writeValueAsString(testListMetadata)));
        AlexaListMetadata response = listService.updateList(TEST_LIST_ID, request, TEST_TOKEN);
        verify(apiClient, times(1)).put(API_ENDPOINT + "/v2/householdlists/" + TEST_LIST_ID, getExpectedHeaders(), MAPPER.writeValueAsString(request));
        assertEquals(response, testListMetadata);
    }

    @Test
    public void delete_list() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        when(apiClient.delete(anyString(), anyMap())).thenReturn(new ApiClientResponse(200, null));
        listService.deleteList(TEST_LIST_ID, TEST_TOKEN);
        verify(apiClient, times(1)).delete(API_ENDPOINT + "/v2/householdlists/" + TEST_LIST_ID, getExpectedHeaders());
    }

    @Test
    public void get_list_item() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        AlexaListItem testListItem = getTestListItem(ListItemState.ACTIVE);
        when(apiClient.get(anyString(), anyMap())).thenReturn(new ApiClientResponse(200, MAPPER.writeValueAsString(testListItem)));
        AlexaListItem response = listService.getListItem(TEST_LIST_ID, TEST_LIST_ITEM_ID, TEST_TOKEN);
        verify(apiClient, times(1)).get(API_ENDPOINT + String.format("/v2/householdlists/%s/items/%s", TEST_LIST_ID, TEST_LIST_ITEM_ID), getExpectedHeaders());
        assertEquals(response, testListItem);
    }

    @Test
    public void create_list_item() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        AlexaListItem testListItem = getTestListItem(ListItemState.ACTIVE);
        CreateListItemRequest request = CreateListItemRequest.builder().withValue("1234").withStatus(ListItemState.ACTIVE).build();
        when(apiClient.post(anyString(), anyMap(), anyString())).thenReturn(new ApiClientResponse(200, MAPPER.writeValueAsString(testListItem)));
        AlexaListItem response = listService.createListItem(TEST_LIST_ID, request, TEST_TOKEN);
        verify(apiClient, times(1)).post(API_ENDPOINT + String.format("/v2/householdlists/%s/items", TEST_LIST_ID), getExpectedHeaders(), MAPPER.writeValueAsString(request));
        assertEquals(response, testListItem);
    }

    @Test
    public void update_list_item() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        AlexaListItem testListItem = getTestListItem(ListItemState.ACTIVE);
        UpdateListItemRequest request = UpdateListItemRequest.builder().withValue("1234").withStatus(ListItemState.ACTIVE).withVersion(1).build();
        when(apiClient.put(anyString(), anyMap(), anyString())).thenReturn(new ApiClientResponse(200, MAPPER.writeValueAsString(testListItem)));
        AlexaListItem response = listService.updateListItem(TEST_LIST_ID, TEST_LIST_ITEM_ID, request, TEST_TOKEN);
        verify(apiClient, times(1)).put(API_ENDPOINT + String.format("/v2/householdlists/%s/items/%s", TEST_LIST_ID, TEST_LIST_ITEM_ID), getExpectedHeaders(), MAPPER.writeValueAsString(request));
        assertEquals(response, testListItem);
    }

    @Test
    public void delete_list_item() throws Exception {
        ApiClient apiClient = mock(ApiClient.class);
        ListService listService = new ListServiceClient(apiClient);
        when(apiClient.delete(anyString(), anyMap())).thenReturn(new ApiClientResponse(200, null));
        listService.deleteListItem(TEST_LIST_ID, TEST_LIST_ITEM_ID, TEST_TOKEN);
        verify(apiClient, times(1)).delete(API_ENDPOINT + String.format("/v2/householdlists/%s/items/%s", TEST_LIST_ID, TEST_LIST_ITEM_ID), getExpectedHeaders());
    }

    private Map<String, String> getExpectedHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + TEST_TOKEN);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private AlexaListsMetadata getListsMetadata(final ListState state) {
        AlexaListsMetadata listsMetadata = new AlexaListsMetadata();
        listsMetadata.setLists(Collections.singletonList(getListMetadata(state)));
        return listsMetadata;
    }

    private AlexaListMetadata getListMetadata(final ListState state) {
        AlexaListMetadata metadata = new AlexaListMetadata();
        metadata.setName("FooList");
        metadata.setListId("1234");
        metadata.setState(state);
        metadata.setVersion(1);
        Status status = new Status();
        status.setUrl("http://foo.bar");
        status.setStatus(ListItemState.COMPLETED);
        return metadata;
    }

    private AlexaList getTestList(final ListState state) {
        AlexaList list = new AlexaList();
        list.setName("FooList");
        list.setListId("1234");
        list.setState(state);
        list.setVersion(1);
        list.setItems(Collections.<AlexaListItem>emptyList());
        list.setLinks(null);
        return list;
    }

    private AlexaListItem getTestListItem(final ListItemState state) {
        AlexaListItem item = new AlexaListItem();
        item.setValue("FooValue");
        item.setId("1234");
        item.setState(state);
        item.setCreatedTime("Tue Nov 07 00:30:12 UTC 2017");
        item.setUpdatedTime("Tue Nov 07 00:49:57 UTC 2017");
        item.setUrl("http://foo.bar");
        item.setVersion(1);
        return item;
    }

}
