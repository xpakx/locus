package io.github.xpakx.locus.settings;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class ElasticSearchConfig {
    @Bean
    public CredentialsProvider getCredentialsProvider() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "password"));
        return credentialsProvider;
    }

    @Bean
    public SSLContext getSSLContext() {
        try {
            return SSLContexts.custom()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true) // ignoring certificate
                    .build();
        } catch(NoSuchAlgorithmException e) {
            System.out.println("Wrong algorithm");
        } catch(KeyStoreException e) {
            System.out.println("Problem with keystore");
        } catch(KeyManagementException e) {
            System.out.println("Problem with key management");
        }
        return null;
    }

    @Bean
    public RestClient getRestClient() {
        return RestClient
                .builder(new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(getCredentialsProvider())
                        .setSSLContext(getSSLContext())
                )
                .build();
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport() {
        return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        return new ElasticsearchClient(getElasticsearchTransport());
    }
}
