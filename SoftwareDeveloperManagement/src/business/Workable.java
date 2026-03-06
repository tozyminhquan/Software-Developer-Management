package business;

import java.util.List;

// Interface chung cho Developer và Project management
// Thể hiện tính Da hinh (Polymorphism) trong OOP
public interface Workable<T> {
    void add(T t);

    void update(String id);

    void delete(String id);

    List<T> getAll();

    void loadFromFile();

    void saveToFile();
}
