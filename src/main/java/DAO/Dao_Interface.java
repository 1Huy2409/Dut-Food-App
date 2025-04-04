package DAO;

import java.util.List;

public interface Dao_Interface<T> {
    public List<T> getAll();
    public void create(T t);
    public int update(T t);
    public int delete(T t);
    public T selectedById(int id);
    public List<T> selectByCondition(String condition);
}
