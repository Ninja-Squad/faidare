package fr.inra.urgi.gpds.repository.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inra.urgi.gpds.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.gpds.domain.datadiscovery.data.DataSourceImpl;
import fr.inra.urgi.gpds.domain.jsonld.data.HasGraph;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gcornut
 */
@Repository
public class DataSourceRepositoryImpl implements DataSourceRepository {

    private final Map<String, DataSource> dataSourceByUri;
    private final ObjectMapper objectMapper;

    public DataSourceRepositoryImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.dataSourceByUri = new HashMap<>();
        loadFromResources();
    }

    @Override
    public DataSource getByUri(String uri) {
        return dataSourceByUri.get(uri);
    }

    private void loadFromResources() {
        try {
            InputStream dataSourceInputStream = getClass().getResourceAsStream("./datasources.jsonld");
            DataSourceList dataSourceList = objectMapper.readValue(dataSourceInputStream, DataSourceList.class);
            for (DataSourceImpl dataSource : dataSourceList.getGraph()) {

                String imageUrl = dataSource.getImageUrl();
                if (imageUrl != null && imageUrl.startsWith("./")) {
                    InputStream logo = getClass().getResourceAsStream(imageUrl);
                    if (logo != null) {
                        byte[] imageBytes = new byte[logo.available()];
                        logo.read(imageBytes);
                        String base64 = Base64.encodeBase64String(imageBytes);
                        String format = imageUrl.replaceAll(".*\\.([^\\.]+)$", "$1");
                        dataSource.setImageUrl("data:image/" + format + ";base64," + base64);
                    }
                }
                dataSourceByUri.put(dataSource.getUri(), dataSource);
            }
        } catch (IOException e) {
            throw new DataSourceRepositoryException(e);
        }
    }

    @Override
    public Collection<DataSource> listAll() {
        return dataSourceByUri.values();
    }

    /**
     * JSON-LD wrapper around a list of DataSourceDocument
     */
    public static class DataSourceList implements HasGraph {
        private List<DataSourceImpl> graph;

        public DataSourceList() {
        }

        @Override
        public List<DataSourceImpl> getGraph() {
            return graph;
        }

        public void setGraph(List<DataSourceImpl> graph) {
            this.graph = graph;
        }

    }

    private static class DataSourceRepositoryException extends RuntimeException {
        public DataSourceRepositoryException(IOException e) {
            super("Could not load data sources from resources.", e);
        }
    }
}
