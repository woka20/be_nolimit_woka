package com.woka.elasticsearchproject.util;

import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.CloseIndexResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;

public class IndexUtil {

        private final RestHighLevelClient client;

        public IndexUtil(RestHighLevelClient client) {
            this.client = client;
        }

        public void allowIndexModification(String indexName) {
            try {
                // Create update settings request
                UpdateSettingsRequest request = new UpdateSettingsRequest(indexName);

                // Disable read-only mode for deletions
                Settings settings = Settings.builder()
                        .put("index.blocks.write", false)
                        .put("index.blocks.read_only_allow_delete", false)
                        .build();
                request.settings(settings);

                // Execute the update settings request
                client.indices().putSettings(request, RequestOptions.DEFAULT);

                // Check if the request was acknowledged

                System.out.println("Index settings updated successfully.");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to update index settings: " + e.getMessage());
            }
        }

}
