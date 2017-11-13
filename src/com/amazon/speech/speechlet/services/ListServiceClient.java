/*
 * Copyright 2016-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 * this file except in compliance with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amazon.speech.speechlet.services;

import com.amazon.speech.speechlet.services.householdlist.client.AlexaList;
import com.amazon.speech.speechlet.services.householdlist.client.AlexaListItem;
import com.amazon.speech.speechlet.services.householdlist.client.AlexaListMetadata;
import com.amazon.speech.speechlet.services.householdlist.client.AlexaListsMetadata;
import com.amazon.speech.speechlet.services.householdlist.client.CreateListItemRequest;
import com.amazon.speech.speechlet.services.householdlist.client.CreateListRequest;
import com.amazon.speech.speechlet.services.householdlist.client.ListState;
import com.amazon.speech.speechlet.services.householdlist.client.UpdateListItemRequest;
import com.amazon.speech.speechlet.services.householdlist.client.UpdateListRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ListServiceClient implements ListService {

    static final String API_ENDPOINT = "https://api.amazonalexa.com";

    private final ObjectMapper mapper;
    private final ApiClient client;

    public ListServiceClient() {
        this(new ApiClient());
    }

    ListServiceClient(final ApiClient client) {
        this.client = client;
        this.mapper = new ObjectMapper();
    }

    @Override
    public AlexaListsMetadata getListsMetadata(final String token) throws ServiceException {
        String uri = API_ENDPOINT + "/v2/householdlists/";
        try {
            ApiClientResponse response = client.get(uri, getRequestHeaders(token));
            if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                return mapper.readValue(response.getResponseBody(), AlexaListsMetadata.class);
            } else {
                throw new ServiceException("Got a non-successful response for lists metadata query");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to retrieve lists metadata", e);
        }
    }

    @Override
    public AlexaList getList(final String listId, final ListState status, final String token) throws ServiceException {
        String uri = API_ENDPOINT + String.format("/v2/householdlists/%s/%s", listId, status.toString());
        try {
            ApiClientResponse response = client.get(uri, getRequestHeaders(token));
            if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                return mapper.readValue(response.getResponseBody(), AlexaList.class);
            } else {
                throw new ServiceException("Got a non-successful response for list query");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to retrieve list", e);
        }
    }

    @Override
    public AlexaListMetadata createList(final CreateListRequest request, final String token) throws ServiceException {
        String uri = API_ENDPOINT + "/v2/householdlists/";
        try {
            ApiClientResponse response = client.post(uri, getRequestHeaders(token), mapper.writeValueAsString(request));
            if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                return mapper.readValue(response.getResponseBody(), AlexaListMetadata.class);
            } else {
                throw new ServiceException("Got a non-successful response for create list request");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to create list", e);
        }
    }

    @Override
    public AlexaListMetadata updateList(final String listId, final UpdateListRequest request, final String token) throws ServiceException {
        String uri = API_ENDPOINT + "/v2/householdlists/" + listId;
        try {
            ApiClientResponse response = client.put(uri, getRequestHeaders(token), mapper.writeValueAsString(request));
            if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                return mapper.readValue(response.getResponseBody(), AlexaListMetadata.class);
            } else {
                throw new ServiceException("Got a non-successful response for update list request");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to update list", e);
        }
    }

    @Override
    public void deleteList(final String listId, final String token) throws ServiceException {
        String uri = API_ENDPOINT + String.format("/v2/householdlists/%s", listId);
        try {
            ApiClientResponse response = client.delete(uri, getRequestHeaders(token));
            if (response.getResponseCode() < 200 || response.getResponseCode() >= 300) {
                throw new ServiceException("Got a non-successful response for delete list request");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to delete list", e);
        }
    }

    @Override
    public AlexaListItem getListItem(final String listId, final String itemId, final String token) throws ServiceException {
        String uri = String.format(API_ENDPOINT + "/v2/householdlists/%s/items/%s", listId, itemId);
        try {
            ApiClientResponse response = client.get(uri, getRequestHeaders(token));
            if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                return mapper.readValue(response.getResponseBody(), AlexaListItem.class);
            } else {
                throw new ServiceException("Got a non-successful response for list item query");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to retrieve list item", e);
        }
    }

    @Override
    public AlexaListItem createListItem(final String listId, final CreateListItemRequest request, final String token) throws ServiceException {
        String uri = API_ENDPOINT + String.format("/v2/householdlists/%s/items", listId);
        try {
            ApiClientResponse response = client.post(uri, getRequestHeaders(token), mapper.writeValueAsString(request));
            if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                return mapper.readValue(response.getResponseBody(), AlexaListItem.class);
            } else {
                throw new ServiceException("Got a non-successful response for create list item request");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to create list item", e);
        }
    }

    @Override
    public AlexaListItem updateListItem(final String listId, final String itemId, final UpdateListItemRequest request, final String token) throws ServiceException {
        String uri = API_ENDPOINT + String.format("/v2/householdlists/%s/items/%s", listId, itemId);
        try {
            ApiClientResponse response = client.put(uri, getRequestHeaders(token), mapper.writeValueAsString(request));
            if (response.getResponseCode() >= 200 && response.getResponseCode() < 300) {
                return mapper.readValue(response.getResponseBody(), AlexaListItem.class);
            } else {
                throw new ServiceException("Got a non-successful response for update list item request");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to update list item", e);
        }
    }

    @Override
    public void deleteListItem(final String listId, final String itemId, final String token) throws ServiceException {
        String uri = API_ENDPOINT + String.format("/v2/householdlists/%s/items/%s", listId, itemId);
        try {
            ApiClientResponse response = client.delete(uri, getRequestHeaders(token));
            if (response.getResponseCode() < 200 || response.getResponseCode() >= 300) {
                throw new ServiceException("Got a non-successful response for delete list item request");
            }
        } catch (IOException e) {
            throw new ServiceException("Encountered an IOException while attempting to delete list item", e);
        }
    }

    private Map<String, String> getRequestHeaders(final String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
