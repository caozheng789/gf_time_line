//package per.sc.controller;
//
//
//import com.google.common.collect.Lists;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import per.sc.pojo.Permission;
//import per.sc.pojo.dto.LoginUser;
//import per.sc.util.UserUtil;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * 权限相关接口
// *
// * @author 小威老师 xiaoweijiagou@163.com
// *
// */
//@RestController
//@RequestMapping("/permissions")
//public class PermissionController {
//
//	@Autowired
//	private PermissionDao permissionDao;
//	@Autowired
//	private PermissionService permissionService;
//
//	/**
//	 * 当前登录用户拥有的权限
//	 */
//	@GetMapping("/current")
//	public List<Permission> permissionsCurrent() {
//		LoginUser loginUser = UserUtil.getLoginUser();
//		List<Permission> list = loginUser.getPermissions();
//		final List<Permission> permissions = list.stream().filter(l -> l.getType().equals(1))
//				.collect(Collectors.toList());
//
////		setChild(permissions);
////
////		return permissions.stream().filter(p -> p.getParentId().equals(0L)).collect(Collectors.toList());
//		// 2018.06.09 支持多级菜单
//        List<Permission> firstLevel = permissions.stream().filter(p -> p.getParentId().equals(0L)).collect(Collectors.toList());
//        firstLevel.parallelStream().forEach(p -> {
//            setChild(p, permissions);
//        });
//
//        return firstLevel;
//	}
//
//	/**
//	 * 设置子元素
//	 * 2018.06.09
//	 *
//	 * @param p
//	 * @param permissions
//	 */
//	private void setChild(Permission p, List<Permission> permissions) {
//		List<Permission> child = permissions.parallelStream().filter(a -> a.getParentId().equals(p.getId())).collect(Collectors.toList());
//		p.setChild(child);
//		if (!CollectionUtils.isEmpty(child)) {
//			child.parallelStream().forEach(c -> {
//				//递归设置子元素，多级菜单支持
//				setChild(c, permissions);
//			});
//		}
//	}
//
////	private void setChild(List<Permission> permissions) {
////		permissions.parallelStream().forEach(per -> {
////			List<Permission> child = permissions.stream().filter(p -> p.getParentId().equals(per.getId()))
////					.collect(Collectors.toList());
////			per.setChild(child);
////		});
////	}
//
//	/**
//	 * 菜单列表
//	 *
//	 * @param pId
//	 * @param permissionsAll
//	 * @param list
//	 */
//	private void setPermissionsList(Long pId, List<Permission> permissionsAll, List<Permission> list) {
//		for (Permission per : permissionsAll) {
//			if (per.getParentId().equals(pId)) {
//				list.add(per);
//				if (permissionsAll.stream().filter(p -> p.getParentId().equals(per.getId())).findAny() != null) {
//					setPermissionsList(per.getId(), permissionsAll, list);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 菜单列表
//	 * @return
//	 */
//	@GetMapping
//	@PreAuthorize("hasAuthority('sys:menu:query')")
//	public List<Permission> permissionsList() {
//		List<Permission> permissionsAll = permissionDao.listAll();
//
//		List<Permission> list = Lists.newArrayList();
//		setPermissionsList(0L, permissionsAll, list);
//
//		return list;
//	}
//
//	/**
//	 * 所有菜单
//	 * @return
//	 */
//	@GetMapping("/all")
//	@PreAuthorize("hasAuthority('sys:menu:query')")
//	public JSONArray permissionsAll() {
//		List<Permission> permissionsAll = permissionDao.listAll();
//		JSONArray array = new JSONArray();
//		setPermissionsTree(0L, permissionsAll, array);
//
//		return array;
//	}
//
//	/**
//	 * 一级菜单
//	 * @return
//	 */
//	@GetMapping("/parents")
//	@PreAuthorize("hasAuthority('sys:menu:query')")
//	public List<Permission> parentMenu() {
//		List<Permission> parents = permissionDao.listParents();
//
//		return parents;
//	}
//
//	/**
//	 * 菜单树
//	 *
//	 * @param pId
//	 * @param permissionsAll
//	 * @param array
//	 */
//	private void setPermissionsTree(Long pId, List<Permission> permissionsAll, JSONArray array) {
//		for (Permission per : permissionsAll) {
//			if (per.getParentId().equals(pId)) {
//				String string = JSONObject.toJSONString(per);
//				JSONObject parent = (JSONObject) JSONObject.parse(string);
//				array.add(parent);
//
//				if (permissionsAll.stream().filter(p -> p.getParentId().equals(per.getId())).findAny() != null) {
//					JSONArray child = new JSONArray();
//					parent.put("child", child);
//					setPermissionsTree(per.getId(), permissionsAll, child);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 根据角色id获取权限
//	 * @param roleId
//	 * @return
//	 */
//	@GetMapping(params = "roleId")
//	@PreAuthorize("hasAnyAuthority('sys:menu:query','sys:role:query')")
//	public List<Permission> listByRoleId(Long roleId) {
//		return permissionDao.listByRoleId(roleId);
//	}
//
//	/**
//	 * 保存菜单
//	 * @param permission
//	 */
//	@PostMapping
//	@PreAuthorize("hasAuthority('sys:menu:add')")
//	public void save(@RequestBody Permission permission) {
//		permissionDao.save(permission);
//	}
//
//	/**
//	 * 根据菜单id获取菜单
//	 * @param id
//	 * @return
//	 */
//	@GetMapping("/{id}")
//	@PreAuthorize("hasAuthority('sys:menu:query')")
//	public Permission get(@PathVariable Long id) {
//		return permissionDao.getById(id);
//	}
//
//	/**
//	 * 修改菜单
//	 * @param permission
//	 */
//	@PutMapping
//	@PreAuthorize("hasAuthority('sys:menu:add')")
//	public void update(@RequestBody Permission permission) {
//		permissionService.update(permission);
//	}
//
//	/**
//	 * 校验权限
//	 *
//	 * @return
//	 */
//	@GetMapping("/owns")
//	public Set<String> ownsPermission() {
//		List<Permission> permissions = UserUtil.getLoginUser().getPermissions();
//		if (CollectionUtils.isEmpty(permissions)) {
//			return Collections.emptySet();
//		}
//
//		return permissions.parallelStream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
//				.map(Permission::getPermission).collect(Collectors.toSet());
//	}
//
//	/**
//	 * 删除菜单
//	 * @param id
//	 */
//	@DeleteMapping("/{id}")
//	@PreAuthorize("hasAuthority('sys:menu:del')")
//	public void delete(@PathVariable Long id) {
//		permissionService.delete(id);
//	}
//}
