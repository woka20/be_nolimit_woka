package com.woka.elasticsearchproject.util;

import org.elasticsearch.action.ingest.PutPipelineRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


public class PipelineUtil {

    private final RestHighLevelClient client;

    @Autowired
    public PipelineUtil(RestHighLevelClient client) {
        this.client = client;
    }

    public void createSplitPipeline() throws IOException {
        String pipeline = "{\n" +
                "  \"description\": \"Split Interests field\",\n" +
                "  \"processors\": [\n" +
                "    {\n" +
                "      \"split\": {\n" +
                "        \"field\": \"Interests\",\n" +
                "        \"separator\": \",\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        PutPipelineRequest request = new PutPipelineRequest("split_interests_pipeline",
                new BytesArray(pipeline.getBytes()), XContentType.JSON);
        client.ingest().putPipeline(request, RequestOptions.DEFAULT);
        System.out.println("Pipeline created successfully.");
    }
}

