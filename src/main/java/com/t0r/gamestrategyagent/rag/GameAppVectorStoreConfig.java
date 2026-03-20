package com.t0r.gamestrategyagent.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class GameAppVectorStoreConfig {

    @Resource
    private GameAppDocumentLoader gameAppDocumentLoader;

    @Bean
    VectorStore gameAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        // 加载文档
        List<Document> documents = gameAppDocumentLoader.loadMarkdowns();
        try {
            simpleVectorStore.add(documents);
            log.info("Loaded {} documents into vector store.", documents.size());
        } catch (Exception e) {
            log.error("Failed to initialize vector store from embeddings, RAG will be unavailable until recovered.", e);
        }
        return simpleVectorStore;
    }
}
