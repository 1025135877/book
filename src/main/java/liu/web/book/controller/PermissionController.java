package liu.web.book.controller;

import liu.web.book.annotation.LoginRequired;
import liu.web.book.common.DataGridDataSource;
import liu.web.book.model.Permission;
import liu.web.book.service.PermissionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: luhailiang
 * @Date: 2019-12-29 20:06
 * @Description:
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * @param roleId
     * @return : java.lang.Object
     * @author: luhailiang
     * @date: 2019-12-29 20:19
     * @description: 加载权限树ztree(当前角色已经分配的权限信息会被选中)
     */
    @PostMapping("/loadRolePermissionData")
    @LoginRequired
    public Object loadRolePermissionData(Integer roleId) {
        List<Permission> permissions = new ArrayList<>();
        List<Permission> ps = permissionService.queryAll();

        // 获取当前角色已经分配的权限信息
        List<Integer> permissionids = permissionService.queryPermissionIdsByRoleId(roleId);

        Map<Integer, Permission> permissionMap = new HashMap<>();
        for (Permission p : ps) {
            if (permissionids.contains(p.getPermissionId())) {
                p.setChecked(true);
            } else {
                p.setChecked(false);
            }
            permissionMap.put(p.getPermissionId(), p);
        }
        for (Permission p : ps) {
            Permission child = p;
            if (child.getPermissionParentId() == null) {
                permissions.add(p);
            } else {
                Permission parent = permissionMap.get(child.getPermissionParentId());
                parent.getChildren().add(child);
            }
        }
        return permissions;
    }

    /**
     * @return : liu.web.book.common.DataGridDataSource<liu.web.book.common.PermissionTreeGrid>
     * @author: luhailiang
     * @date: 2019-04-10 18:23
     * @description: 应用列表(easyui tree grid)
     */
    @PostMapping("/list")
    @LoginRequired
    public DataGridDataSource<Permission> list() {

        List<Permission> permissionTreeGridList = new ArrayList<>();
        List<Permission> permissionList = permissionService.queryAll();
        for (Permission permission : permissionList) {
            Permission permissionTreeGrid = new Permission();
            permissionTreeGrid.setPermissionId(permission.getPermissionId());
            permissionTreeGrid.setPermissionName(permission.getPermissionName());
            permissionTreeGrid.setPermissionUrl(permission.getPermissionUrl());
            permissionTreeGrid.setPermissionIcon(permission.getPermissionIcon());
            permissionTreeGrid.setPermissionCreateTime(permission.getPermissionCreateTime());
            permissionTreeGrid.setPermissionLastModifyTime(permission.getPermissionLastModifyTime());
            permissionTreeGrid.set_parentId(permission.getPermissionParentId());
            permissionTreeGridList.add(permissionTreeGrid);
        }
        DataGridDataSource<Permission> permissionDataGridDataSource = new DataGridDataSource<>();
        permissionDataGridDataSource.setTotal(permissionTreeGridList.size());
        permissionDataGridDataSource.setRows(permissionTreeGridList);
        return permissionDataGridDataSource;
    }
}
