package business;

import java.util.List;

public interface Workable<T> {
    void add(T t);

    void update(String id);

    void delete(String id);

    List<T> getAll();

    void loadFromFile();

    void saveToFile();
}
