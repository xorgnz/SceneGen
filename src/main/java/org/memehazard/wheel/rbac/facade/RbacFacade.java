package org.memehazard.wheel.rbac.facade;

import java.util.List;

import org.memehazard.wheel.core.security.WheelPasswordEncoder;
import org.memehazard.wheel.rbac.dao.PermissionDAO;
import org.memehazard.wheel.rbac.dao.RoleDAO;
import org.memehazard.wheel.rbac.dao.UserDAO;
import org.memehazard.wheel.rbac.dao.UserGroupDAO;
import org.memehazard.wheel.rbac.model.Permission;
import org.memehazard.wheel.rbac.model.Role;
import org.memehazard.wheel.rbac.model.User;
import org.memehazard.wheel.rbac.model.UserGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RbacFacade
{
    @Autowired
    private PermissionDAO        dao_permission;

    @Autowired
    private RoleDAO              dao_role;
    @Autowired
    private UserDAO              dao_user;
    @Autowired
    private UserGroupDAO         dao_usergrp;
    @SuppressWarnings("unused")
    private Logger               log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private WheelPasswordEncoder passwordEncoder;


    public void addPermission(Permission p)
    {
        dao_permission.add(p);
    }


    public void addRole(Role r)
    {
        dao_role.add(r);
    }


    public void addUser(User u)
    {
        dao_user.add(u);
    }


    public void addUserGroup(UserGroup ug)
    {
        dao_usergrp.add(ug);
    }


    public void assignPermissionToRole(int permId, int roleId)
    {
        // Deassign to prevent duplicate links
        dao_permission.deassignFromRole(permId, roleId);

        // Assign permission
        dao_permission.assignToRole(permId, roleId);
    }


    public void assignRoleToUser(int roleId, int userId)
    {
        // Deassign to prevent duplicate links
        dao_role.deassignFromUser(roleId, userId);

        // Assign role
        dao_role.assignToUser(roleId, userId);
    }


    public void assignUserToUserGroup(int userId, int userGroupId)
    {
        // Deassign to prevent duplicate links
        dao_user.deassignFromUserGroup(userId, userGroupId);

        // Assign user
        dao_user.assignToUserGroup(userId, userGroupId);
    }


    public void deassignPermissionFromRole(int permId, int roleId)
    {
        dao_permission.deassignFromRole(permId, roleId);
    }


    public void deassignRoleFromUser(int roleId, int userId)
    {
        dao_role.deassignFromUser(roleId, userId);
    }


    public void deassignUserFromUserGroup(int userId, int userGroupId)
    {
        dao_user.deassignFromUserGroup(userId, userGroupId);
    }


    public void deletePermission(int permId)
    {
        dao_permission.delete(permId);
    }


    public void deleteRole(int roleId)
    {
        dao_role.delete(roleId);
    }


    public void deleteUser(int userId)
    {
        dao_user.delete(userId);
    }


    public void deleteUserGroup(int groupId)
    {
        dao_usergrp.delete(groupId);
    }


    public Permission getPermission(int permId)
    {
        return dao_permission.get(permId);
    }


    public Permission getPermissionByName(String name)
    {
        return dao_permission.getByName(name);
    }


    public Role getRole(int roleId)
    {
        return dao_role.get(roleId);
    }


    public Role getRoleByName(String s)
    {
        return dao_role.getByName(s);
    }


    public User getUser(long userId)
    {
        return dao_user.get(userId);
    }


    public User getUserByEmail(String email)
    {
        return dao_user.getByEmail(email);
    }


    public User getUserByUsername(String username)
    {
        return dao_user.getByUsername(username);
    }


    public UserGroup getUserGroup(int groupId)
    {
        return dao_usergrp.get(groupId);
    }


    public UserGroup getUserGroupByName(String name)
    {
        return dao_usergrp.getByName(name);
    }


    public List<Permission> listPermissions()
    {
        return dao_permission.listAll();
    }


    public List<Permission> listPermissionsByRole(int roleId)
    {
        return dao_permission.listByRole(roleId);
    }


    public List<Role> listRoles()
    {
        return dao_role.listAll();
    }


    public List<Role> listRolesByPermission(int permissionId)
    {
        return dao_role.listByPermission(permissionId);
    }


    public List<Role> listRolesByUser(int userId)
    {
        return dao_role.listByUser(userId);
    }


    public List<UserGroup> listUserGroups()
    {
        return dao_usergrp.listAll();
    }


    public List<UserGroup> listUserGroupsByUser(int userId)
    {
        return dao_usergrp.listByUser(userId);
    }


    public List<User> listUsers()
    {
        return dao_user.listAll();
    }


    public List<User> listUsersByRole(int roleId)
    {
        return dao_user.listByRole(roleId);
    }


    public List<User> listUsersByUserGroup(int userGroupId)
    {
        return dao_user.listByUserGroup(userGroupId);
    }


    public void updatePermission(Permission p)
    {
        dao_permission.update(p);
    }


    public void updateRole(Role r)
    {
        dao_role.update(r);
    }


    public void updateUser(User u)
    {
        dao_user.update(u);
    }


    public void updateUserGroup(UserGroup ug)
    {
        dao_usergrp.update(ug);
    }
}