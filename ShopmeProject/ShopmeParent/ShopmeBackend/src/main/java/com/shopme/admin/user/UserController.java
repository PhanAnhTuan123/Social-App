package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.apache.catalina.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/users")
    public String listFirstPage(Model model){
//        List<User>listUsers =  service.listAll();
//        model.addAttribute("listUsers",listUsers);
        return listByPage(1,model,"firstName","asc",null);
    }
    @GetMapping("/users/new")
    public String newUser(Model model){
        List<Role>listRoles = service.listRoles();
        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user",user);
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("pageTitle","Create New Users");

        return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image")MultipartFile multipartFile) throws Exception {
        System.out.println(user.toString());
        System.out.println(multipartFile.getOriginalFilename());
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = service.save(user);
            String upLoadDir = "user-photos/"+savedUser.getId();
            FileUploadUtil.cleanDir(upLoadDir);
            FileUploadUtil.saveFile(upLoadDir,fileName,multipartFile);
        }else{
            if(user.getPhotos().isEmpty()){
                user.setPhotos(null);
            }
            service.save(user);
        }


//        service.save(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully.");
        return "redirect:/users";
    }
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try{
            User user =  service.get(id);
            List<Role>listRoles = service.listRoles();
            model.addAttribute("user",user);
            model.addAttribute("pageTitle","Edit user (ID: "+id+")");
            model.addAttribute("listRoles",listRoles);
            return "user_form";

        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/users";
        }
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
    try{
        service.delete(id);
        redirectAttributes.addFlashAttribute("message",
                "The user ID: "+id+ " has been deleted successfully.");
    }catch (UserNotFoundException ex){
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
     return "redirect:/users";
    }
    @GetMapping("/users/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable("id") Integer id,
                                      @PathVariable("status") boolean enabled,
                                      RedirectAttributes redirectAttributes){
        service.updateUserEnaibledStatus(id,enabled);
        String status = enabled ? "enabled" : "disabled" ;
        String message = "The user ID: " +id+" has been "+status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/users";
    }
    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") Integer pagenum,
                             Model model,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){
        Page<User>page = service.listByPage(pagenum,sortField,sortDir,keyword);
        List<User>listUsers = page.getContent();
        System.out.println("Pagenum = "+pagenum);
        System.out.println("Total elements = "+page.getTotalElements());
        System.out.println("Total pages= "+page.getTotalPages());
        long startCount = (pagenum -1)*UserService.USER_PER_PAGE + 1;
        long endCount = startCount + UserService.USER_PER_PAGE - 1;
        if(endCount >page.getTotalElements()){
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";


        model.addAttribute("currentPage",pagenum);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listUsers",listUsers);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("reverseSortDir",reverseSortDir);
        model.addAttribute("keyword",keyword);


        return "users";
    }

}

