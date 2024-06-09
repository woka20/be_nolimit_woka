package com.woka.elasticsearchproject.util;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

public class MappingUtil {

    private final RestHighLevelClient restHighLevelClient;

    public MappingUtil(RestHighLevelClient client) {
        this.restHighLevelClient = client;
    }

    public void updateFieldToKeyword(String indexName, String fieldName, String type) throws IOException {
        try {
            // Create a mapping request for the index
            PutMappingRequest request = new PutMappingRequest(indexName);
            request.type("employees");
            // Define the mapping for the fieldname field with a ".keyword" subfield
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject(fieldName)
                    .field("type", type)
                    .field("fielddata", true)
                    .endObject()
                    .endObject()
                    .endObject();


            // Set the mapping source on the request
            request.source(mapping);

            // Execute the mapping update request
            restHighLevelClient.indices().putMapping(request, RequestOptions.DEFAULT);

            System.out.println("Mapping updated successfully to include a '.keyword' subfield for 'Interests'.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to update mapping: " + e.getMessage());
        }

    }
}
