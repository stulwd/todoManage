package org.sleekflow.lwd.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.sleekflow.lwd.model.Todo;
import org.sleekflow.lwd.model.TodoExample;

public interface TodoMapper {
    long countByExample(TodoExample example);

    int deleteByExample(TodoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Todo record);

    int insertSelective(Todo record);

    List<Todo> selectByExampleWithBLOBs(TodoExample example);

    List<Todo> selectByExample(TodoExample example);

    Todo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Todo record, @Param("example") TodoExample example);

    int updateByExampleWithBLOBs(@Param("record") Todo record, @Param("example") TodoExample example);

    int updateByExample(@Param("record") Todo record, @Param("example") TodoExample example);

    int updateByPrimaryKeySelective(Todo record);

    int updateByPrimaryKeyWithBLOBs(Todo record);

    int updateByPrimaryKey(Todo record);
}