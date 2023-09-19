package by.shareiko.orgtool.data.config;

import java.util.Optional;

public interface DataAccessConfig {
    Optional<String> getString(String key);
    Optional<Boolean> getBoolean(String key);
}
