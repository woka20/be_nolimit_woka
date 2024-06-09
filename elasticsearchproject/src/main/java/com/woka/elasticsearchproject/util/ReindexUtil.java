package com.woka.elasticsearchproject.util;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;

public class ReindexUtil {

    private final RestHighLevelClient client;

    @Autowired
    public ReindexUtil(RestHighLevelClient client) {
        this.client = client;
    }

    public void reindex(String sourceIndex, String destIndex) throws IOException {
        ReindexRequest request = new ReindexRequest();
        request.setSourceIndices(sourceIndex);
        request.setDestIndex(destIndex.toLowerCase());
        request.setDestPipeline("split_interests_pipeline");
        request.setConflicts("proceed");

        BulkByScrollResponse bulkResponse = client.reindex(request, RequestOptions.DEFAULT);
        if (bulkResponse.isTimedOut()) {
            System.out.println("Reindexing timed out");
        } else {
            System.out.println("Reindexing completed");
        }
    }
}

