package liu.web.book.dao;


import liu.web.book.model.Permission;
import liu.web.book.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer permissionId);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer permissionId);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);


    List<Permission> queryPermissionsByUser(User user);

    List<Permission> queryAll();

    void deleteRolePermissionRsByRoleId(Integer roleId);

    List<Integer> queryPermissionIdsByRoleId(Integer roleId);
}