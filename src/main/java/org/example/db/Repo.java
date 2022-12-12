package org.example.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface Repo extends CrudRepository<MyEntity, Integer> {

    @Transactional
    default void addNewTwoEntities() {
        var e1 = new MyEntity();
        var e2 = new MyEntity();
        save(e1);
        save(e2);
    }
}
