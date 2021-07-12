/*
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  The OpenSearch Contributors require contributions made to
 *  this file be licensed under the Apache-2.0 license or a
 *  compatible open source license.
 *
 *  Modifications Copyright OpenSearch Contributors. See
 *  GitHub history for details.
 */

package org.opensearch.ml.common.dataset;

import java.io.IOException;
import java.util.List;

import org.opensearch.common.io.stream.StreamInput;
import org.opensearch.common.io.stream.StreamOutput;
import org.opensearch.search.builder.SearchSourceBuilder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

/**
 * Search query based input data. The client just need give the search query, and ML plugin will read the data based on it,
 * and build the data frame for algorithm execution.
 */
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SearchQueryInputDataset extends MLInputDataset {

    SearchSourceBuilder searchSourceBuilder;

    List<String> indices;

    @Builder
    public SearchQueryInputDataset(@NonNull List<String> indices, @NonNull SearchSourceBuilder searchSourceBuilder) {
        super(MLInputDataType.SEARCH_QUERY);
        if (indices.isEmpty()) {
            throw new IllegalArgumentException("indices can't be empty");
        }

        this.indices = indices;
        this.searchSourceBuilder = searchSourceBuilder;
    }

    public SearchQueryInputDataset(StreamInput streaminput) throws IOException {
        super(MLInputDataType.SEARCH_QUERY);
        this.searchSourceBuilder = new SearchSourceBuilder(streaminput);
        this.indices = streaminput.readStringList();
    }

    @Override
    public void writeTo(StreamOutput streamOutput) throws IOException {
        super.writeTo(streamOutput);
        searchSourceBuilder.writeTo(streamOutput);
        streamOutput.writeStringCollection(indices);
    }
}