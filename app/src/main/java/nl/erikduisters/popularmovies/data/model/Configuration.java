package nl.erikduisters.popularmovies.data.model;

import java.util.List;

/**
 * Created by Erik Duisters on 17-02-2018.
 */

public class Configuration {
    private Images images;
    private List<String> changeKeys;

    public Configuration() {}

    public static class Images {
        private String baseUrl;
        private String secureBaseUrl;
        private List<String> backdropSizes;
        private List<String> logoSizes;
        private List<String> posterSizes;
        private List<String> profileSizes;
        private List<String> stillSizes;

        public Images() {}

        public String getBaseUrl() { return baseUrl; }
        public List<String> getPosterSizes() { return posterSizes; }
    }

    public Images getImages() { return images; }
}
